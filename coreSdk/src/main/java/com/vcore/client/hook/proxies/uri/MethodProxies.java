package com.vcore.client.hook.proxies.uri;

import android.content.pm.PackageManager;
import android.os.RemoteException;

import com.vcore.client.core.VirtualCore;
import com.vcore.client.hook.base.MethodProxy;
import com.vcore.helper.utils.VLog;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Method proxies for IUriGrantsManager service
 * 
 * @author Lody
 */
public class MethodProxies {

    static class GetUriPermissions extends MethodProxy {

        @Override
        public String getMethodName() {
            return "getUriPermissions";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            VLog.d("IUriGrantsManager", "getUriPermissions called - isVAppProcess: " + VirtualCore.get().isVAppProcess());
            if (VirtualCore.get().isVAppProcess()) {
                try {
                    // For virtual apps, return empty list to avoid UID validation issues
                    // This prevents the SecurityException: Package does not belong to calling UID
                    VLog.d("IUriGrantsManager", "getUriPermissions called for virtual app, returning empty list to avoid UID validation");
                    return new ArrayList<>();
                } catch (Exception e) {
                    VLog.w("IUriGrantsManager", "Failed to get URI permissions, returning empty list", e);
                    return new ArrayList<>();
                }
            }
            VLog.d("IUriGrantsManager", "getUriPermissions called for non-virtual app, delegating to original method");
            return method.invoke(who, args);
        }
    }

    static class GrantUriPermission extends MethodProxy {

        @Override
        public String getMethodName() {
            return "grantUriPermission";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            if (VirtualCore.get().isVAppProcess()) {
                // For virtual apps, always grant URI permissions
                VLog.d("IUriGrantsManager", "grantUriPermission called for virtual app, granting permission");
                return null; // void method
            }
            return method.invoke(who, args);
        }
    }

    static class RevokeUriPermission extends MethodProxy {

        @Override
        public String getMethodName() {
            return "revokeUriPermission";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            if (VirtualCore.get().isVAppProcess()) {
                // For virtual apps, always allow revoking URI permissions
                VLog.d("IUriGrantsManager", "revokeUriPermission called for virtual app, revoking permission");
                return null; // void method
            }
            return method.invoke(who, args);
        }
    }

    static class RevokeUriPermissionFromOwner extends MethodProxy {

        @Override
        public String getMethodName() {
            return "revokeUriPermissionFromOwner";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            if (VirtualCore.get().isVAppProcess()) {
                // For virtual apps, always allow revoking URI permissions
                VLog.d("IUriGrantsManager", "revokeUriPermissionFromOwner called for virtual app, revoking permission");
                return null; // void method
            }
            return method.invoke(who, args);
        }
    }

    static class CheckUriPermission extends MethodProxy {

        @Override
        public String getMethodName() {
            return "checkUriPermission";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            if (VirtualCore.get().isVAppProcess()) {
                // For virtual apps, always grant URI permissions
                VLog.d("IUriGrantsManager", "checkUriPermission called for virtual app, granting permission");
                return PackageManager.PERMISSION_GRANTED;
            }
            return method.invoke(who, args);
        }
    }

    static class CheckGrantUriPermission extends MethodProxy {

        @Override
        public String getMethodName() {
            return "checkGrantUriPermission";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            if (VirtualCore.get().isVAppProcess()) {
                // For virtual apps, always grant URI permissions
                VLog.d("IUriGrantsManager", "checkGrantUriPermission called for virtual app, granting permission");
                return PackageManager.PERMISSION_GRANTED;
            }
            return method.invoke(who, args);
        }
    }

    static class GetUriPermissionOwner extends MethodProxy {

        @Override
        public String getMethodName() {
            return "getUriPermissionOwner";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            if (VirtualCore.get().isVAppProcess()) {
                // For virtual apps, return null (no owner)
                VLog.d("IUriGrantsManager", "getUriPermissionOwner called for virtual app, returning null");
                return null;
            }
            return method.invoke(who, args);
        }
    }

    static class TakePersistableUriPermission extends MethodProxy {

        @Override
        public String getMethodName() {
            return "takePersistableUriPermission";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            if (VirtualCore.get().isVAppProcess()) {
                // For virtual apps, always allow taking persistable URI permissions
                VLog.d("IUriGrantsManager", "takePersistableUriPermission called for virtual app, allowing");
                return null; // void method
            }
            return method.invoke(who, args);
        }
    }

    static class ReleasePersistableUriPermission extends MethodProxy {

        @Override
        public String getMethodName() {
            return "releasePersistableUriPermission";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            if (VirtualCore.get().isVAppProcess()) {
                // For virtual apps, always allow releasing persistable URI permissions
                VLog.d("IUriGrantsManager", "releasePersistableUriPermission called for virtual app, allowing");
                return null; // void method
            }
            return method.invoke(who, args);
        }
    }

    static class TakePersistableUriPermissionFromOwner extends MethodProxy {

        @Override
        public String getMethodName() {
            return "takePersistableUriPermissionFromOwner";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            if (VirtualCore.get().isVAppProcess()) {
                // For virtual apps, always allow taking persistable URI permissions
                VLog.d("IUriGrantsManager", "takePersistableUriPermissionFromOwner called for virtual app, allowing");
                return null; // void method
            }
            return method.invoke(who, args);
        }
    }

    static class ReleasePersistableUriPermissionFromOwner extends MethodProxy {

        @Override
        public String getMethodName() {
            return "releasePersistableUriPermissionFromOwner";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            if (VirtualCore.get().isVAppProcess()) {
                // For virtual apps, always allow releasing persistable URI permissions
                VLog.d("IUriGrantsManager", "releasePersistableUriPermissionFromOwner called for virtual app, allowing");
                return null; // void method
            }
            return method.invoke(who, args);
        }
    }

    static class GetPersistedUriPermissions extends MethodProxy {

        @Override
        public String getMethodName() {
            return "getPersistedUriPermissions";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            if (VirtualCore.get().isVAppProcess()) {
                try {
                    // For virtual apps, return empty list to avoid UID validation issues
                    VLog.d("IUriGrantsManager", "getPersistedUriPermissions called for virtual app, returning empty list to avoid UID validation");
                    return new ArrayList<>();
                } catch (Exception e) {
                    VLog.w("IUriGrantsManager", "Failed to get persisted URI permissions, returning empty list", e);
                    return new ArrayList<>();
                }
            }
            return method.invoke(who, args);
        }
    }

    static class ClearGrantedUriPermissions extends MethodProxy {

        @Override
        public String getMethodName() {
            return "clearGrantedUriPermissions";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            if (VirtualCore.get().isVAppProcess()) {
                // For virtual apps, always allow clearing URI permissions
                VLog.d("IUriGrantsManager", "clearGrantedUriPermissions called for virtual app, allowing");
                return null; // void method
            }
            return method.invoke(who, args);
        }
    }
}
