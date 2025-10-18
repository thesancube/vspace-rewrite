package com.vcore.client.hook.proxies.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.vcore.client.core.VirtualCore;
import com.vcore.client.hook.base.MethodProxy;
import com.vcore.client.hook.utils.MethodParameterUtils;
import com.vcore.client.ipc.VNotificationManager;
import com.vcore.client.NotificationPermissionHelper;
import com.vcore.helper.utils.ArrayUtils;
import com.vcore.helper.utils.VLog;

import java.lang.reflect.Method;

/**
 * @author Lody
 */

@SuppressWarnings("unused")
class MethodProxies {

    static class EnqueueNotification extends MethodProxy {

        @Override
        public String getMethodName() {
            return "enqueueNotification";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            String pkg = (String) args[0];
            if (getHostPkg().equals(pkg)) {
                return method.invoke(who, args);
            }
            
            try {
                int notificationIndex = ArrayUtils.indexOfFirst(args, Notification.class);
                int idIndex = ArrayUtils.indexOfFirst(args, Integer.class);
                int id = (int) args[idIndex];
                
                // Process notification ID and tag
                id = VNotificationManager.get().dealNotificationId(id, pkg, null, getAppUserId());
                args[idIndex] = id;
                
                Notification notification = (Notification) args[notificationIndex];
                if (notification == null) {
                    VLog.w("EnqueueNotification", "Notification is null for package: " + pkg);
                    return 0;
                }
                
                // Check if notifications are enabled for this package
                if (!VNotificationManager.get().dealNotification(id, notification, pkg)) {
                    VLog.d("EnqueueNotification", "Notification blocked for package: " + pkg);
                    return 0;
                }
                
                // Ensure notification has proper channel for Android O+
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ensureNotificationChannel(notification, pkg);
                }
                
                // Add notification to our tracking
                VNotificationManager.get().addNotification(id, null, pkg, getAppUserId());
                
                // Replace package name with host package for system call
                args[0] = getHostPkg();
                
                VLog.d("EnqueueNotification", "Sending notification for virtual app: " + pkg + " as host: " + getHostPkg());
                return method.invoke(who, args);
            } catch (Exception e) {
                VLog.e("EnqueueNotification", "Error processing notification for package: " + pkg, e);
                return 0;
            }
        }
    }

    /* package */ static class EnqueueNotificationWithTag extends MethodProxy {

        @Override
        public String getMethodName() {
            return "enqueueNotificationWithTag";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            String pkg = (String) args[0];
            if (getHostPkg().equals(pkg)) {
                return method.invoke(who, args);
            }
            int notificationIndex = ArrayUtils.indexOfFirst(args, Notification.class);
            int idIndex = ArrayUtils.indexOfFirst(args, Integer.class);
            int tagIndex = (Build.VERSION.SDK_INT >= 18 ? 2 : 1);
            int id = (int) args[idIndex];
            String tag = (String) args[tagIndex];

            id = VNotificationManager.get().dealNotificationId(id, pkg, tag, getAppUserId());
            tag = VNotificationManager.get().dealNotificationTag(id, pkg, tag, getAppUserId());
            args[idIndex] = id;
            args[tagIndex] = tag;
            //key(tag,id)
            Notification notification = (Notification) args[notificationIndex];
            if (!VNotificationManager.get().dealNotification(id, notification, pkg)) {
                return 0;
            }
            VNotificationManager.get().addNotification(id, tag, pkg, getAppUserId());
            args[0] = getHostPkg();
            if (Build.VERSION.SDK_INT >= 18 && args[1] instanceof String) {
                args[1] = getHostPkg();
            }
            return method.invoke(who, args);
        }
    }

    /* package */ static class EnqueueNotificationWithTagPriority extends EnqueueNotificationWithTag {

        @Override
        public String getMethodName() {
            return "enqueueNotificationWithTagPriority";
        }
    }

    /* package */ static class CancelNotificationWithTag extends MethodProxy {

        @Override
        public String getMethodName() {
            return "cancelNotificationWithTag";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            String pkg = MethodParameterUtils.replaceFirstAppPkg(args);
            if (getHostPkg().equals(pkg)) {
                return method.invoke(who, args);
            }
            
            int index_tag = 1;
            int index_id = 2;

            if (Build.VERSION.SDK_INT >= 30) {
                index_tag = 2;
                index_id = 3;
            }
            
            String tag = (String) args[index_tag];
            int id = (int) args[index_id];

            id = VNotificationManager.get().dealNotificationId(id, pkg, tag, getAppUserId());
            tag = VNotificationManager.get().dealNotificationTag(id, pkg, tag, getAppUserId());

            args[index_tag] = tag;
            args[index_id] = id;
            return method.invoke(who, args);
        }
    }

    /**
     * @author Lody
     */
    /* package */ static class CancelAllNotifications extends MethodProxy {

        @Override
        public String getMethodName() {
            return "cancelAllNotifications";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            String pkg = MethodParameterUtils.replaceFirstAppPkg(args);
            if (VirtualCore.get().isAppInstalled(pkg)) {
                VNotificationManager.get().cancelAllNotification(pkg, getAppUserId());
                return 0;
            }
            return method.invoke(who, args);
        }
    }

    static class AreNotificationsEnabledForPackage extends MethodProxy {
        @Override
        public String getMethodName() {
            return "areNotificationsEnabledForPackage";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            String pkg = (String) args[0];
            if (getHostPkg().equals(pkg)) {
                return method.invoke(who, args);
            }
            return VNotificationManager.get().areNotificationsEnabledForPackage(pkg, getAppUserId());
        }
    }

    static class SetNotificationsEnabledForPackage extends MethodProxy {
        @Override
        public String getMethodName() {
            return "setNotificationsEnabledForPackage";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            String pkg = (String) args[0];
            if (getHostPkg().equals(pkg)) {
                return method.invoke(who, args);
            }
            int enableIndex = ArrayUtils.indexOfFirst(args, Boolean.class);
            boolean enable = (boolean) args[enableIndex];
            VNotificationManager.get().setNotificationsEnabledForPackage(pkg, enable, getAppUserId());
            return 0;
        }
    }
    
    /**
     * Ensure notification has proper channel for Android O+
     */
    private static void ensureNotificationChannel(Notification notification, String packageName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                Context context = VirtualCore.get().getContext();
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                
                if (notificationManager != null) {
                    // Check if notification already has a channel
                    if (notification.getChannelId() == null || notification.getChannelId().isEmpty()) {
                        // Use the app notifications channel for virtual apps
                        String channelId = NotificationPermissionHelper.CHANNEL_ID_APP_NOTIFICATIONS;
                        
                        // Check if channel exists
                        NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
                        if (channel == null) {
                            // Create the channel if it doesn't exist
                            NotificationPermissionHelper.createNotificationChannels(context);
                        }
                        
                        // Set the channel ID using reflection since it's not directly accessible
                        try {
                            java.lang.reflect.Field channelIdField = Notification.class.getDeclaredField("mChannelId");
                            channelIdField.setAccessible(true);
                            channelIdField.set(notification, channelId);
                            VLog.d("EnqueueNotification", "Set channel ID for virtual app notification: " + channelId);
                        } catch (Exception e) {
                            VLog.w("EnqueueNotification", "Failed to set channel ID for notification", e);
                        }
                    }
                }
            } catch (Exception e) {
                VLog.w("EnqueueNotification", "Failed to ensure notification channel", e);
            }
        }
    }
}
