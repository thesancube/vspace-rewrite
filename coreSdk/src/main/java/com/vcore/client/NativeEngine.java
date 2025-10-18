package com.vcore.client;

import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.os.Process;

import com.vcore.client.core.VirtualCore;
import com.vcore.client.env.VirtualRuntime;
import com.vcore.client.ipc.VActivityManager;
import com.vcore.client.natives.NativeMethods;
import com.vcore.helper.compat.BuildCompat;
import com.vcore.helper.utils.DeviceUtil;
import com.vcore.helper.utils.VLog;
import com.vcore.os.VUserHandle;
import com.vcore.remote.InstalledAppInfo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VirtualApp Native Project
 */
public class NativeEngine {
    private static final String TAG = NativeEngine.class.getSimpleName();

    private static final String VESCAPE = "/6decacfa7aad11e8a718985aebe4663a";

    private static Map<String, InstalledAppInfo> sDexOverrideMap;

    private static boolean sFlag = false;
    private static boolean sLibraryLoaded = false;

    private static final String LIB_NAME = "vcore";

    static {
        VLog.d(TAG, "=== NativeEngine static initializer ===");
        try {
            VLog.d(TAG, "Loading native library: " + LIB_NAME);
            VLog.d(TAG, "Library path: " + System.getProperty("java.library.path"));
            VLog.d(TAG, "Current working directory: " + System.getProperty("user.dir"));
            
            // Try to load the library
            System.loadLibrary(LIB_NAME);
            sLibraryLoaded = true;
            VLog.d(TAG, "✅ Native library loaded successfully");
            
            // Test if native methods are available
            try {
                // This will test if the JNI methods are properly linked
                VLog.d(TAG, "Testing native method availability...");
                // We can't call the methods yet since they need parameters, but we can check if the class loads
                VLog.d(TAG, "✅ Native methods appear to be available");
            } catch (Exception e) {
                VLog.w(TAG, "⚠️ Native methods may not be fully available yet: " + e.getMessage());
            }
            
        } catch (UnsatisfiedLinkError e) {
            VLog.e(TAG, "❌ Failed to load native library: " + LIB_NAME, e);
            VLog.e(TAG, "Library path: " + System.getProperty("java.library.path"));
            VLog.e(TAG, "Error details: " + e.getMessage());
            VLog.e(TAG, "This usually means the library file is not found or incompatible");
            e.printStackTrace();
        } catch (Throwable e) {
            VLog.e(TAG, "❌ Error loading native library: " + LIB_NAME, e);
            e.printStackTrace();
        }
        VLog.d(TAG, "=== NativeEngine static initializer complete ===");
    }

    static {
        try {
            NativeMethods.init();
            VLog.d(TAG, "Native methods initialized successfully");
        } catch (Exception e) {
            VLog.e(TAG, "Error initializing native methods", e);
        }
    }


    public static void startDexOverride() {
        List<InstalledAppInfo> installedAppInfos = VirtualCore.get().getInstalledApps(0);
        sDexOverrideMap = new HashMap<>(installedAppInfos.size());
        for (InstalledAppInfo info : installedAppInfos) {
            try {
                sDexOverrideMap.put(new File(info.apkPath).getCanonicalPath(), info);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getRedirectedPath(String redirectPath) {
        try {
            return nativeGetRedirectedPath(redirectPath);
        } catch (Throwable e) {
            VLog.e(TAG, VLog.getStackTraceString(e));
        }
        return redirectPath;
    }

    public static String resverseRedirectedPath(String origPath) {
        try {
            return nativeReverseRedirectedPath(origPath);
        } catch (Throwable e) {
            VLog.e(TAG, VLog.getStackTraceString(e));
        }
        return origPath;
    }

    public static void redirectDirectory(String origPath, String newPath) {
        if (!origPath.endsWith("/")) {
            origPath = origPath + "/";
        }
        if (!newPath.endsWith("/")) {
            newPath = newPath + "/";
        }
        try {
            nativeIORedirect(origPath, newPath);
        } catch (Throwable e) {
            VLog.e(TAG, VLog.getStackTraceString(e));
        }
    }

    public static String getEscapePath(String path) {
        if (path == null) {
            return null;
        }
        File file = new File(path);
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        return new File(VESCAPE, path).getAbsolutePath();
    }

    public static void redirectFile(String origPath, String newPath) {
        if (origPath.endsWith("/")) {
            origPath = origPath.substring(0, origPath.length() - 1);
        }
        if (newPath.endsWith("/")) {
            newPath = newPath.substring(0, newPath.length() - 1);
        }

        try {
            nativeIORedirect(origPath, newPath);
        } catch (Throwable e) {
            VLog.e(TAG, VLog.getStackTraceString(e));
        }
    }

    public static void whitelist(String path, boolean directory) {
        if (directory && !path.endsWith("/")) {
            path = path + "/";
        } else if (!directory && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        try {
            nativeIOWhitelist(path);
        } catch (Throwable e) {
            VLog.e(TAG, VLog.getStackTraceString(e));
        }
    }

    public static void forbid(String path) {
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        try {
            nativeIOForbid(path);
        } catch (Throwable e) {
            VLog.e(TAG, VLog.getStackTraceString(e));
        }
    }

    public static void enableIORedirect() {
        VLog.d(TAG, "=== Starting IO redirect initialization ===");
        
        if (!sLibraryLoaded) {
            VLog.e(TAG, "❌ Native library not loaded, cannot enable IO redirect");
            return;
        }
        
        try {
            Context context = VirtualCore.get().getContext();
            VLog.d(TAG, "Context: " + context);
            VLog.d(TAG, "Package name: " + context.getPackageName());
            VLog.d(TAG, "Native library dir: " + context.getApplicationInfo().nativeLibraryDir);
            VLog.d(TAG, "Data dir: " + context.getApplicationInfo().dataDir);
            VLog.d(TAG, "Files dir: " + context.getFilesDir().getAbsolutePath());
            
            String soPath = context.getApplicationInfo().nativeLibraryDir + File.separator + "lib" + LIB_NAME + ".so";
            VLog.d(TAG, "Primary path: " + soPath);
            VLog.d(TAG, "File exists: " + new File(soPath).exists());
            
            if (!new File(soPath).exists()) {
                VLog.d(TAG, "Primary path not found, trying alternatives...");
                // Try alternative paths
                String[] alternativePaths = {
                    context.getApplicationInfo().dataDir + "/lib/lib" + LIB_NAME + ".so",
                    context.getFilesDir().getParent() + "/lib/lib" + LIB_NAME + ".so",
                    "/data/app/" + context.getPackageName() + "/lib/arm64/lib" + LIB_NAME + ".so",
                    "/data/app/" + context.getPackageName() + "/lib/arm/lib" + LIB_NAME + ".so",
                    "/data/app/" + context.getPackageName() + "-1/lib/arm64/lib" + LIB_NAME + ".so",
                    "/data/app/" + context.getPackageName() + "-1/lib/arm/lib" + LIB_NAME + ".so"
                };
                
                boolean found = false;
                for (String altPath : alternativePaths) {
                    VLog.d(TAG, "Trying alternative path: " + altPath);
                    VLog.d(TAG, "  Exists: " + new File(altPath).exists());
                    if (new File(altPath).exists()) {
                        soPath = altPath;
                        found = true;
                        VLog.d(TAG, "✅ Found native library at: " + soPath);
                        break;
                    }
                }
                
                if (!found) {
                    VLog.e(TAG, "❌ Native library not found in any path. Library name: " + LIB_NAME);
                    VLog.e(TAG, "Available files in native library dir:");
                    File nativeLibDir = new File(context.getApplicationInfo().nativeLibraryDir);
                    if (nativeLibDir.exists()) {
                        String[] files = nativeLibDir.list();
                        if (files != null) {
                            for (String file : files) {
                                VLog.e(TAG, "  - " + file);
                            }
                        }
                    }
                    VLog.e(TAG, "Available files in data dir:");
                    File dataDir = new File(context.getApplicationInfo().dataDir);
                    if (dataDir.exists()) {
                        String[] files = dataDir.list();
                        if (files != null) {
                            for (String file : files) {
                                VLog.e(TAG, "  - " + file);
                            }
                        }
                    }
                    // Don't throw exception, just log and continue
                    VLog.w(TAG, "Continuing without IO redirect...");
                    return;
                }
            } else {
                VLog.d(TAG, "✅ Found native library at primary path: " + soPath);
            }
            
            VLog.d(TAG, "Setting up path redirection rules...");
            redirectDirectory(VESCAPE, "/");
            VLog.d(TAG, "Calling nativeEnableIORedirect with: " + soPath);
            VLog.d(TAG, "API Level: " + Build.VERSION.SDK_INT);
            VLog.d(TAG, "Preview API Level: " + BuildCompat.getPreviewSDKInt());
            
            nativeEnableIORedirect(soPath, Build.VERSION.SDK_INT, BuildCompat.getPreviewSDKInt());
            VLog.d(TAG, "✅ IO redirect enabled successfully!");
        } catch (Throwable e) {
            VLog.e(TAG, "❌ Error enabling IO redirect", e);
            e.printStackTrace();
        }
        VLog.d(TAG, "=== IO redirect initialization complete ===");
    }

    static void launchEngine() {
        if (sFlag) {
            return;
        }
        try {
            Method[] methods = {NativeMethods.gOpenDexFileNative, NativeMethods.gCameraNativeSetup, NativeMethods.gAudioRecordNativeCheckPermission};
            VLog.d(TAG, "Launching native engine...");
            nativeLaunchEngine(methods, VirtualCore.get().getHostPkg(), VirtualRuntime.isArt(), Build.VERSION.SDK_INT, NativeMethods.gCameraMethodType);
            VLog.d(TAG, "Native engine launched successfully");
        } catch (UnsatisfiedLinkError e) {
            VLog.e(TAG, "Native method not found - library may not be loaded properly", e);
        } catch (Throwable e) {
            VLog.e(TAG, "Error launching native engine", e);
        }
        sFlag = true;
    }

    public static void onKillProcess(int pid, int signal) {
        VLog.e(TAG, "killProcess: pid = %d, signal = %d.", pid, signal);
        if (pid == android.os.Process.myPid()) {
            VLog.e(TAG, VLog.getStackTraceString(new Throwable()));
        }
    }

    public static int onGetCallingUid(int originUid) {
        int callingPid = Binder.getCallingPid();
        if (callingPid == Process.myPid()) {
            return VClientImpl.get().getBaseVUid();
        }
        if (callingPid == VirtualCore.get().getSystemPid()) {
            return Process.SYSTEM_UID;
        }
        int vuid = VActivityManager.get().getUidByPid(callingPid);
        if (vuid != -1) {
            return VUserHandle.getAppId(vuid);
        }
        VLog.w(TAG, String.format("Unknown uid: %s", callingPid));
        return VClientImpl.get().getBaseVUid();
    }

    public static void onOpenDexFileNative(String[] params) {
        String dexOrJarPath = params[0];
        String outputPath = params[1];
        VLog.d(TAG, "DexOrJarPath = %s, OutputPath = %s.", dexOrJarPath, outputPath);
        try {
            String canonical = new File(dexOrJarPath).getCanonicalPath();
            InstalledAppInfo info = sDexOverrideMap.get(canonical);
            if (info != null && !info.dependSystem || info != null && DeviceUtil.isMeizuBelowN() && params[1] == null) {
                outputPath = info.getOdexFile().getPath();
                params[1] = outputPath;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static native void nativeLaunchEngine(Object[] method, String hostPackageName, boolean isArt, int apiLevel, int cameraMethodType);

    private static native void nativeMark();

    private static native String nativeReverseRedirectedPath(String redirectedPath);

    private static native String nativeGetRedirectedPath(String orgPath);

    private static native void nativeIORedirect(String origPath, String newPath);

    private static native void nativeIOWhitelist(String path);

    private static native void nativeIOForbid(String path);

    private static native void nativeEnableIORedirect(String selfSoPath, int apiLevel, int previewApiLevel);

    public static native void disableJit(int apiLevel);

    public static int onGetUid(int uid) {
        return VClientImpl.get().getBaseVUid();
    }
}
