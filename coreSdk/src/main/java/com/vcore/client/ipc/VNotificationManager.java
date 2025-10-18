package com.vcore.client.ipc;

import android.app.Notification;
import android.os.IBinder;
import android.os.RemoteException;

import com.vcore.client.core.VirtualCore;
import com.vcore.helper.utils.VLog;
import com.vcore.server.INotificationManager;
import com.vcore.server.notification.NotificationCompat;

/**
 * Fake notification manager
 */
public class VNotificationManager {
    private static final VNotificationManager sInstance = new VNotificationManager();
    private final NotificationCompat mNotificationCompat;
    private INotificationManager mRemote;

    private VNotificationManager() {
        mNotificationCompat = NotificationCompat.create();
    }

    public static VNotificationManager get() {
        return sInstance;
    }

    public INotificationManager getService() {
        if (mRemote == null ||
                (!mRemote.asBinder().pingBinder() && !VirtualCore.get().isVAppProcess())) {
            synchronized (VNotificationManager.class) {
                final IBinder pmBinder = ServiceManagerNative.getService(ServiceManagerNative.NOTIFICATION);
                mRemote = INotificationManager.Stub.asInterface(pmBinder);
            }
        }
        return mRemote;
    }

    public boolean dealNotification(int id, Notification notification, String packageName) {
        if(notification == null) return false;
        
        // Always allow notifications from host app
        if (VirtualCore.get().getHostPkg().equals(packageName)) {
            return true;
        }
        
        // For virtual apps, process the notification through the compatibility layer
        try {
            return mNotificationCompat.dealNotification(id, notification, packageName);
        } catch (Exception e) {
            VLog.w("VNotificationManager", "Failed to process notification for package: " + packageName, e);
            return false;
        }
    }

    public int dealNotificationId(int id, String packageName, String tag, int userId) {
        try {
            return getService().dealNotificationId(id, packageName, tag, userId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return id;
    }

    public String dealNotificationTag(int id, String packageName, String tag, int userId) {
        try {
            return getService().dealNotificationTag(id, packageName, tag, userId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return tag;
    }

    public void addNotification(int id, String tag, String packageName, int userId) {
        try {
            getService().addNotification(id, tag, packageName, userId);
        } catch (RemoteException e) {
            VLog.w("VNotificationManager", "Failed to add notification for package: " + packageName, e);
        }
    }

    public boolean areNotificationsEnabledForPackage(String packageName, int userId) {
        try {
            return getService().areNotificationsEnabledForPackage(packageName, userId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return true;
        }
    }

    public void setNotificationsEnabledForPackage(String packageName, boolean enable, int userId) {
        try {
            getService().setNotificationsEnabledForPackage(packageName, enable, userId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void cancelAllNotification(String packageName, int userId) {
        try {
            getService().cancelAllNotification(packageName, userId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
