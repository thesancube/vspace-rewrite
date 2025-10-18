package com.vcore.client.hook.proxies.content;

import android.content.pm.ApplicationInfo;
import android.os.Build;

import com.vcore.client.VClientImpl;
import com.vcore.client.core.VirtualCore;
import com.vcore.client.hook.base.MethodProxy;
import com.vcore.helper.utils.VLog;

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
}
