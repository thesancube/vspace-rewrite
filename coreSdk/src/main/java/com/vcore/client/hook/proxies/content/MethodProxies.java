package com.vcore.client.hook.proxies.content;

import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.vcore.client.VClientImpl;
import com.vcore.client.core.VirtualCore;
import com.vcore.client.hook.base.MethodProxy;
import com.vcore.helper.utils.VLog;
import com.vcore.os.VUserHandle;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * author: weishu on 18/3/13.
 */
class MethodProxies {

    static class NotifyChange extends MethodProxy {

        @Override
        public String getMethodName() {
            return "notifyChange";
        }

        @Override
        public boolean beforeCall(Object who, Method method, Object... args) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                return super.beforeCall(who, method, args);
            }
            ApplicationInfo currentApplicationInfo = VClientImpl.get().getCurrentApplicationInfo();
            if (currentApplicationInfo == null) {
                return super.beforeCall(who, method, args);
            }
            int targetSdkVersion = currentApplicationInfo.targetSdkVersion;

            int length = args.length;
            int index = -1;
            for (int i = 0; i < length; i++) {
                Object obj = args[length - 1];
                if (obj != null && obj.getClass() == Integer.class) {
                    if ((int) obj == targetSdkVersion) {
                        index = i;
                    }
                }
            }
            /*
            In ContentService, it contains this code:

            if (targetSdkVersion >= Build.VERSION_CODES.O) {
                throw new SecurityException(msg);
            } else {
                if (msg.startsWith("Failed to find provider")) {
                    // Sigh, we need to quietly let apps targeting older API
                    // levels notify on non-existent providers.
                } else {
                    Log.w(TAG, "Ignoring notify for " + uri + " from " + uid + ": " + msg);
                    return;
                }
            }
            we just modify the targetSdkVersion dynamic to fake it.
            */
            if (index != -1) {
                args[index] = Build.VERSION_CODES.N_MR1;
            }

            return super.beforeCall(who, method, args);
        }

        @Override
        public boolean isEnable() {
            return isAppProcess();
        }
    }

    static class GetPersistedUriPermissions extends MethodProxy {

        @Override
        public String getMethodName() {
            return "getPersistedUriPermissions";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            // For virtual apps, handle URI permissions safely
            if (VirtualCore.get().isVAppProcess()) {
                try {
                    // Try to get URI permissions with the virtual app's UID
                    int vuid = VClientImpl.get().getVUid();
                    if (args.length > 0) {
                        // Replace the UID parameter if it exists
                        for (int i = 0; i < args.length; i++) {
                            if (args[i] instanceof Integer) {
                                args[i] = vuid;
                                break;
                            }
                        }
                    }
                    return method.invoke(who, args);
                } catch (Exception e) {
                    // If URI permissions fail, return empty list instead of crashing
                    VLog.w("GetPersistedUriPermissions", "Failed to get URI permissions in ContentResolver, returning empty list", e);
                    return new ArrayList<>();
                }
            }
            
            return method.invoke(who, args);
        }

        @Override
        public boolean isEnable() {
            return isAppProcess();
        }
    }

    static class Query extends MethodProxy {

        @Override
        public String getMethodName() {
            return "query";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            return handleContentResolverCall(who, method, args, "query");
        }

        @Override
        public boolean isEnable() {
            return isAppProcess();
        }
    }

    static class Insert extends MethodProxy {

        @Override
        public String getMethodName() {
            return "insert";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            return handleContentResolverCall(who, method, args, "insert");
        }

        @Override
        public boolean isEnable() {
            return isAppProcess();
        }
    }

    static class Update extends MethodProxy {

        @Override
        public String getMethodName() {
            return "update";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            return handleContentResolverCall(who, method, args, "update");
        }

        @Override
        public boolean isEnable() {
            return isAppProcess();
        }
    }

    static class Delete extends MethodProxy {

        @Override
        public String getMethodName() {
            return "delete";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            return handleContentResolverCall(who, method, args, "delete");
        }

        @Override
        public boolean isEnable() {
            return isAppProcess();
        }
    }

    static class GetType extends MethodProxy {

        @Override
        public String getMethodName() {
            return "getType";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            return handleContentResolverCall(who, method, args, "getType");
        }

        @Override
        public boolean isEnable() {
            return isAppProcess();
        }
    }

    /**
     * Generic handler for ContentResolver calls with URI redirection
     */
    private static Object handleContentResolverCall(Object who, Method method, Object[] args, String operation) throws Throwable {
        if (VirtualCore.get().isVAppProcess() && args.length >= 1 && args[0] instanceof Uri) {
            Uri originalUri = (Uri) args[0];
            
            // Check if this is an external storage document query
            if (isExternalStorageUri(originalUri)) {
                try {
                    // Redirect to virtual storage
                    Uri redirectedUri = redirectExternalStorageUri(originalUri);
                    if (redirectedUri != null) {
                        VLog.d("ContentResolver", "Redirecting %s from %s to %s", operation, originalUri, redirectedUri);
                        args[0] = redirectedUri;
                    }
                } catch (Exception e) {
                    VLog.w("ContentResolver", "Failed to redirect external storage %s", operation, e);
                }
            }
        }
        
        return method.invoke(who, args);
    }

    private static boolean isExternalStorageUri(Uri uri) {
        return uri != null && "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static Uri redirectExternalStorageUri(Uri originalUri) {
        if (originalUri == null) {
            return null;
        }

        try {
            String path = originalUri.getPath();
            if (path == null) {
                return null;
            }

            // Parse the document tree path
            if (path.startsWith("/tree/primary:")) {
                String documentPath = path.substring("/tree/primary:".length());
                
                // Remove any trailing /children or /document
                if (documentPath.endsWith("/children")) {
                    documentPath = documentPath.substring(0, documentPath.length() - "/children".length());
                }
                if (documentPath.endsWith("/document")) {
                    documentPath = documentPath.substring(0, documentPath.length() - "/document".length());
                }
                
                // Get the virtual app's package name and user ID
                String packageName = VClientImpl.get().getCurrentPackage();
                int userId = VUserHandle.myUserId();
                
                if (packageName == null) {
                    VLog.w("ContentResolver", "No current package name available for redirection");
                    return null;
                }

                // Build the virtual storage path
                String virtualPath = buildVirtualStoragePath(documentPath, packageName, userId);
                if (virtualPath != null) {
                    // Create a new URI pointing to the virtual storage
                    return Uri.parse("content://com.android.externalstorage.documents/tree/primary:" + virtualPath);
                }
            }
        } catch (Exception e) {
            VLog.w("ContentResolver", "Error redirecting external storage URI: " + originalUri, e);
        }

        return null;
    }

    private static String buildVirtualStoragePath(String externalPath, String packageName, int userId) {
        try {
            // Handle different external storage paths
            if (externalPath.startsWith("Android/data/")) {
                // Redirect Android/data/ to virtual storage
                String subPath = externalPath.substring("Android/data/".length());
                return "Android/media/" + VirtualCore.get().getHostPkg() + "/vsdcard/" + userId + "/Android/data/" + packageName + "/virtual/" + userId + "/" + subPath;
            } else if (externalPath.startsWith("Android/obb/")) {
                // Redirect Android/obb/ to virtual storage
                String subPath = externalPath.substring("Android/obb/".length());
                return "Android/media/" + VirtualCore.get().getHostPkg() + "/vsdcard/" + userId + "/Android/data/" + packageName + "/virtual/" + userId + "/obb/" + subPath;
            } else if (externalPath.equals("Android/data") || externalPath.equals("Android/obb")) {
                // Root directories
                return "Android/media/" + VirtualCore.get().getHostPkg() + "/vsdcard/" + userId + "/Android/data/" + packageName + "/virtual/" + userId;
            } else {
                // Other paths (like Downloads, Pictures, etc.)
                return "Android/media/" + VirtualCore.get().getHostPkg() + "/vsdcard/" + userId + "/" + externalPath;
            }
        } catch (Exception e) {
            VLog.w("ContentResolver", "Error building virtual storage path for: " + externalPath, e);
            return null;
        }
    }
}
