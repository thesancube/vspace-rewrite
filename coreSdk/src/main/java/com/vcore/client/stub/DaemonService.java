package com.vcore.client.stub;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;

import com.vcore.client.core.VirtualCore;
import com.vcore.client.env.Constants;
import com.vcore.client.NotificationPermissionHelper;

import java.io.File;


/**
 * @author Lody
 *
 */
public class DaemonService extends Service {

    private static final int NOTIFY_ID = 1001;
    private static final String CHANNEL_ID = "vcore_daemon_channel";

	static boolean showNotification = true;

	public static void startup(Context context) {
		File flagFile = context.getFileStreamPath(Constants.NO_NOTIFICATION_FLAG);
		if (Build.VERSION.SDK_INT >= 25 && flagFile.exists()) {
			showNotification = false;
		}

		context.startService(new Intent(context, DaemonService.class));
		if (VirtualCore.get().isServerProcess()) {
			// PrivilegeAppOptimizer.notifyBootFinish();
			DaemonJobService.scheduleJob(context);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		startup(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void createNotificationChannel() {
		// Use the enhanced notification helper
		NotificationPermissionHelper.createNotificationChannels(this);
	}

	private Notification createNotification() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			return new Notification.Builder(this, CHANNEL_ID)
				.setContentTitle("VirtualCore")
				.setContentText("Running")
				.setSmallIcon(android.R.drawable.ic_dialog_info)
				.build();
		} else {
			return new Notification();
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (!showNotification) {
			return;
		}
		createNotificationChannel();
        startService(new Intent(this, InnerService.class));
        
        // Start foreground service with proper type for Android 15+
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                // Android 15+ requires foreground service type
                startForeground(NOTIFY_ID, createNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);
            } else {
                startForeground(NOTIFY_ID, createNotification());
            }
        } catch (Exception e) {
            // Fallback for older versions or if service type is not supported
            try {
                startForeground(NOTIFY_ID, createNotification());
            } catch (Exception fallbackException) {
                // Ignore errors during early initialization
                e.printStackTrace();
            }
        }
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	public static final class InnerService extends Service {

		private Notification createNotification() {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				return new Notification.Builder(this, CHANNEL_ID)
					.setContentTitle("VirtualCore")
					.setContentText("Running")
					.setSmallIcon(android.R.drawable.ic_dialog_info)
					.build();
			} else {
				return new Notification();
			}
		}

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            try {
                // Start foreground service with proper type for Android 15+
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    // Android 15+ requires foreground service type
                    startForeground(NOTIFY_ID, createNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);
                } else {
                    startForeground(NOTIFY_ID, createNotification());
                }
                stopForeground(true);
                stopSelf();
            } catch (Exception e) {
                // Ignore errors during early initialization
                e.printStackTrace();
            }
            return super.onStartCommand(intent, flags, startId);
        }

		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}
	}


}
