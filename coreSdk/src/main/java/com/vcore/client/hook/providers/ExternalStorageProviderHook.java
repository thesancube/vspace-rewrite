package com.vcore.client.hook.providers;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.content.res.AssetFileDescriptor;

import com.vcore.client.VClientImpl;
import com.vcore.client.core.VirtualCore;
import com.vcore.client.hook.base.MethodBox;
import com.vcore.helper.utils.VLog;
import com.vcore.os.VUserHandle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Enhanced hook for external storage document provider to redirect calls to virtual storage
 * Features:
 * - URI caching for performance
 * - Comprehensive URI pattern support
 * - Better error handling and logging
 * - Support for all content provider operations
 * - Seamless integration with VirtualCore
 * 
 * @author Lody
 */
public class ExternalStorageProviderHook extends ProviderHook {

    private static final String TAG = "ExternalStorageProviderHook";
    private static final String EXTERNAL_STORAGE_AUTHORITY = "com.android.externalstorage.documents";
    
    // URI patterns we support
    private static final String TREE_PATTERN = "/tree/primary:";
    private static final String DOCUMENT_PATTERN = "/document/primary:";
    private static final String CHILDREN_PATTERN = "/children";
    private static final String DOCUMENT_SUFFIX = "/document";
    
    // Cache for URI redirections to improve performance
    private static final Map<String, Uri> uriCache = new ConcurrentHashMap<>();
    private static final int MAX_CACHE_SIZE = 1000;
    
    // Cached values for performance
    private static volatile String cachedHostPackage = null;
    private static volatile Integer cachedUserId = null;
    private static volatile String cachedPackageName = null;
    
    public ExternalStorageProviderHook(Object base) {
        super(base);
    }

    @Override
    public Cursor query(MethodBox methodBox, Uri url, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder, Bundle originQueryArgs) throws InvocationTargetException {
        return executeWithRedirection(methodBox, "query", url);
    }

    @Override
    public Uri insert(MethodBox methodBox, Uri url, ContentValues initialValues) throws InvocationTargetException {
        return executeWithRedirection(methodBox, "insert", url);
    }

    @Override
    public int delete(MethodBox methodBox, Uri url, String selection, String[] selectionArgs) throws InvocationTargetException {
        return executeWithRedirection(methodBox, "delete", url);
    }

    @Override
    public int update(MethodBox methodBox, Uri url, ContentValues values, String selection, String[] selectionArgs) throws InvocationTargetException {
        return executeWithRedirection(methodBox, "update", url);
    }

    @Override
    public String getType(MethodBox methodBox, Uri url) throws InvocationTargetException {
        return executeWithRedirection(methodBox, "getType", url);
    }

    @Override
    public ParcelFileDescriptor openFile(MethodBox methodBox, Uri url, String mode) throws InvocationTargetException {
        return executeWithRedirection(methodBox, "openFile", url);
    }

    @Override
    public AssetFileDescriptor openAssetFile(MethodBox methodBox, Uri url, String mode) throws InvocationTargetException {
        return executeWithRedirection(methodBox, "openAssetFile", url);
    }

    @Override
    public Bundle call(MethodBox methodBox, String method, String arg, Bundle extras) throws InvocationTargetException {
        // Handle special call methods that might contain URIs
        if (extras != null && extras.containsKey("uri")) {
            Uri uri = extras.getParcelable("uri");
            if (uri != null) {
                Uri redirectedUri = redirectExternalStorageUri(uri);
                if (redirectedUri != null) {
                    extras.putParcelable("uri", redirectedUri);
                }
            }
        }
        return (Bundle) methodBox.call();
    }

    @Override
    public int bulkInsert(MethodBox methodBox, Uri url, ContentValues[] initialValues) throws InvocationTargetException {
        return executeWithRedirection(methodBox, "bulkInsert", url);
    }

    /**
     * Generic method to execute content provider operations with URI redirection
     */
    @SuppressWarnings("unchecked")
    private <T> T executeWithRedirection(MethodBox methodBox, String operation, Uri url) throws InvocationTargetException {
        if (!VirtualCore.get().isVAppProcess()) {
            return (T) methodBox.call();
        }

        try {
            Uri redirectedUri = redirectExternalStorageUri(url);
            if (redirectedUri != null) {
                VLog.d(TAG, "Redirecting %s from %s to %s", operation, url, redirectedUri);
                // Modify the arguments in the MethodBox to use the redirected URI
                if (methodBox.args.length > 0) {
                    methodBox.args[0] = redirectedUri;
                }
            }
        } catch (Exception e) {
            VLog.w(TAG, "Failed to redirect URI for %s operation: %s", operation, url, e);
        }

        return (T) methodBox.call();
    }

    /**
     * Enhanced URI redirection with caching and comprehensive pattern support
     */
    private Uri redirectExternalStorageUri(Uri originalUri) {
        if (originalUri == null) {
            return null;
        }

        String authority = originalUri.getAuthority();
        if (!EXTERNAL_STORAGE_AUTHORITY.equals(authority)) {
            return null;
        }

        // Check cache first for performance
        String uriString = originalUri.toString();
        Uri cachedUri = uriCache.get(uriString);
        if (cachedUri != null) {
            return cachedUri;
        }

        try {
            String path = originalUri.getPath();
            if (path == null) {
                return null;
            }

            String documentPath = extractDocumentPath(path);
            if (documentPath == null) {
                return null;
            }

            // Get cached values or fetch them
            String packageName = getCachedPackageName();
            int userId = getCachedUserId();
            String hostPackage = getCachedHostPackage();

            if (packageName == null || hostPackage == null) {
                VLog.w(TAG, "Missing package information for redirection");
                return null;
            }

            // Build the virtual storage path
            String virtualPath = buildVirtualStoragePath(documentPath, packageName, userId, hostPackage);
            if (virtualPath != null) {
                Uri redirectedUri = Uri.parse("content://" + EXTERNAL_STORAGE_AUTHORITY + "/tree/primary:" + virtualPath);
                
                // Cache the result (with size limit)
                if (uriCache.size() < MAX_CACHE_SIZE) {
                    uriCache.put(uriString, redirectedUri);
                }
                
                return redirectedUri;
            }
        } catch (Exception e) {
            VLog.w(TAG, "Error redirecting external storage URI: " + originalUri, e);
        }

        return null;
    }

    /**
     * Extract document path from various URI patterns
     */
    private String extractDocumentPath(String path) {
        if (path.startsWith(TREE_PATTERN)) {
            return path.substring(TREE_PATTERN.length());
        } else if (path.startsWith(DOCUMENT_PATTERN)) {
            return path.substring(DOCUMENT_PATTERN.length());
        }
        return null;
    }

    /**
     * Enhanced virtual storage path building with better error handling
     */
    private String buildVirtualStoragePath(String externalPath, String packageName, int userId, String hostPackage) {
        try {
            // Normalize the path
            String normalizedPath = externalPath;
            
            // Remove common suffixes
            if (normalizedPath.endsWith(CHILDREN_PATTERN)) {
                normalizedPath = normalizedPath.substring(0, normalizedPath.length() - CHILDREN_PATTERN.length());
            }
            if (normalizedPath.endsWith(DOCUMENT_SUFFIX)) {
                normalizedPath = normalizedPath.substring(0, normalizedPath.length() - DOCUMENT_SUFFIX.length());
            }

            // Build base virtual path
            String baseVirtualPath = "Android/media/" + hostPackage + "/vsdcard/" + userId;
            
            // Handle different path patterns
            if (normalizedPath.startsWith("Android/data/")) {
                String subPath = normalizedPath.substring("Android/data/".length());
                String virtualPath = baseVirtualPath + "/Android/data/" + packageName + "/virtual/" + userId + "/" + subPath;
                VLog.d(TAG, "Redirecting Android/data/ access: %s -> %s", externalPath, virtualPath);
                return virtualPath;
            } else if (normalizedPath.startsWith("Android/obb/")) {
                String subPath = normalizedPath.substring("Android/obb/".length());
                String virtualPath = baseVirtualPath + "/Android/data/" + packageName + "/virtual/" + userId + "/obb/" + subPath;
                VLog.d(TAG, "Redirecting Android/obb/ access: %s -> %s", externalPath, virtualPath);
                return virtualPath;
            } else if (normalizedPath.equals("Android/data") || normalizedPath.equals("Android/obb")) {
                String virtualPath = baseVirtualPath + "/Android/data/" + packageName + "/virtual/" + userId;
                VLog.d(TAG, "Redirecting Android directory access: %s -> %s", externalPath, virtualPath);
                return virtualPath;
            } else if (normalizedPath.startsWith("Android/")) {
                // Other Android directories
                String virtualPath = baseVirtualPath + "/" + normalizedPath;
                VLog.d(TAG, "Redirecting Android subdirectory access: %s -> %s", externalPath, virtualPath);
                return virtualPath;
            } else {
                // Root level directories (Downloads, Pictures, etc.)
                String virtualPath = baseVirtualPath + "/" + normalizedPath;
                VLog.d(TAG, "Redirecting general storage access: %s -> %s", externalPath, virtualPath);
                return virtualPath;
            }
        } catch (Exception e) {
            VLog.w(TAG, "Error building virtual storage path for: " + externalPath, e);
            return null;
        }
    }

    /**
     * Get cached package name with fallback
     */
    private String getCachedPackageName() {
        if (cachedPackageName == null) {
            try {
                cachedPackageName = VClientImpl.get().getCurrentPackage();
            } catch (Exception e) {
                VLog.w(TAG, "Failed to get current package name", e);
            }
        }
        return cachedPackageName;
    }

    /**
     * Get cached user ID with fallback
     */
    private int getCachedUserId() {
        if (cachedUserId == null) {
            try {
                cachedUserId = VUserHandle.myUserId();
            } catch (Exception e) {
                VLog.w(TAG, "Failed to get user ID, using 0", e);
                cachedUserId = 0;
            }
        }
        return cachedUserId;
    }

    /**
     * Get cached host package with fallback
     */
    private String getCachedHostPackage() {
        if (cachedHostPackage == null) {
            try {
                cachedHostPackage = VirtualCore.get().getHostPkg();
            } catch (Exception e) {
                VLog.w(TAG, "Failed to get host package", e);
            }
        }
        return cachedHostPackage;
    }

    /**
     * Clear caches when needed (e.g., when switching apps)
     */
    public static void clearCache() {
        uriCache.clear();
        cachedHostPackage = null;
        cachedUserId = null;
        cachedPackageName = null;
        VLog.d(TAG, "ExternalStorageProviderHook cache cleared");
    }

    @Override
    protected void processArgs(Method method, Object... args) {
        // Enhanced argument processing
        super.processArgs(method, args);
        
        // Process any URI arguments that might need redirection
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Uri) {
                Uri uri = (Uri) args[i];
                if (EXTERNAL_STORAGE_AUTHORITY.equals(uri.getAuthority())) {
                    Uri redirectedUri = redirectExternalStorageUri(uri);
                    if (redirectedUri != null) {
                        args[i] = redirectedUri;
                    }
                }
            }
        }
    }
}
