package com.vcore.client;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.vcore.helper.utils.VLog;

/**
 * Helper class for managing notification permissions and channels
 * Handles notification setup for different Android versions
 * 
 * @author Lody
 */
public class NotificationPermissionHelper {
    
    private static final String TAG = NotificationPermissionHelper.class.getSimpleName();
    
    // Notification channel IDs
    public static final String CHANNEL_ID_DAEMON = "vcore_daemon_channel";
    public static final String CHANNEL_ID_APP_NOTIFICATIONS = "vcore_app_notifications";
    public static final String CHANNEL_ID_SYSTEM = "vcore_system";
    
    // Notification channel names
    public static final String CHANNEL_NAME_DAEMON = "VirtualCore Daemon";
    public static final String CHANNEL_NAME_APP_NOTIFICATIONS = "Virtual App Notifications";
    public static final String CHANNEL_NAME_SYSTEM = "VirtualCore System";
    
    /**
     * Create all necessary notification channels for the app
     */
    public static void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            VLog.d(TAG, "Creating notification channels for Android O+");
            
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager == null) {
                VLog.w(TAG, "NotificationManager is null, cannot create channels");
                return;
            }
            
            // Create daemon channel (low priority, no badge)
            createDaemonChannel(notificationManager);
            
            // Create app notifications channel (normal priority)
            createAppNotificationsChannel(notificationManager);
            
            // Create system channel (high priority for important system messages)
            createSystemChannel(notificationManager);
            
            VLog.d(TAG, "All notification channels created successfully");
        } else {
            VLog.d(TAG, "Android version < O, no notification channels needed");
        }
    }
    
    /**
     * Create daemon notification channel (for background services)
     */
    private static void createDaemonChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID_DAEMON,
                CHANNEL_NAME_DAEMON,
                NotificationManager.IMPORTANCE_MIN
            );
            channel.setDescription("Keeps virtual apps running in background");
            channel.setShowBadge(false);
            channel.setSound(null, null);
            channel.setVibrationPattern(null);
            channel.setLockscreenVisibility(android.app.Notification.VISIBILITY_SECRET);
            
            notificationManager.createNotificationChannel(channel);
            VLog.d(TAG, "Created daemon notification channel");
        }
    }
    
    /**
     * Create app notifications channel (for virtual app notifications)
     */
    private static void createAppNotificationsChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID_APP_NOTIFICATIONS,
                CHANNEL_NAME_APP_NOTIFICATIONS,
                NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notifications from virtual apps");
            channel.setShowBadge(true);
            channel.enableLights(true);
            channel.enableVibration(true);
            
            notificationManager.createNotificationChannel(channel);
            VLog.d(TAG, "Created app notifications channel");
        }
    }
    
    /**
     * Create system channel (for important system messages)
     */
    private static void createSystemChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID_SYSTEM,
                CHANNEL_NAME_SYSTEM,
                NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Important system messages from VirtualCore");
            channel.setShowBadge(true);
            channel.enableLights(true);
            channel.enableVibration(true);
            
            notificationManager.createNotificationChannel(channel);
            VLog.d(TAG, "Created system notification channel");
        }
    }
    
    /**
     * Check if notifications are enabled for the app
     */
    public static boolean areNotificationsEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                if (notificationManager != null) {
                    return notificationManager.areNotificationsEnabled();
                }
            } catch (SecurityException e) {
                VLog.w(TAG, "SecurityException when checking if notifications are enabled, assuming enabled", e);
                return true; // Assume enabled if we can't check due to permissions
            } catch (Exception e) {
                VLog.w(TAG, "Error checking if notifications are enabled, assuming enabled", e);
                return true; // Assume enabled if there's any error
            }
        }
        // For older versions, assume notifications are enabled
        return true;
    }
    
    /**
     * Check if a specific notification channel is enabled
     */
    public static boolean isChannelEnabled(Context context, String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                if (notificationManager != null) {
                    NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
                    return channel != null && channel.getImportance() != NotificationManager.IMPORTANCE_NONE;
                }
            } catch (SecurityException e) {
                VLog.w(TAG, "SecurityException when checking channel " + channelId + ", assuming enabled", e);
                return true; // Assume enabled if we can't check due to permissions
            } catch (Exception e) {
                VLog.w(TAG, "Error checking channel " + channelId + ", assuming enabled", e);
                return true; // Assume enabled if there's any error
            }
        }
        return true;
    }
    
    /**
     * Get notification channel importance
     */
    public static int getChannelImportance(Context context, String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                if (notificationManager != null) {
                    NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
                    if (channel != null) {
                        return channel.getImportance();
                    }
                }
            } catch (SecurityException e) {
                VLog.w(TAG, "SecurityException when getting channel importance for " + channelId + ", using default", e);
                return NotificationManager.IMPORTANCE_DEFAULT;
            } catch (Exception e) {
                VLog.w(TAG, "Error getting channel importance for " + channelId + ", using default", e);
                return NotificationManager.IMPORTANCE_DEFAULT;
            }
        }
        return NotificationManager.IMPORTANCE_DEFAULT;
    }
    
    /**
     * Request notification permission (Android 13+)
     */
    public static void requestNotificationPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            VLog.d(TAG, "Requesting notification permission for Android 13+");
            // This should be handled by the main app's permission request system
            // The actual permission request is done through PermissionHandler
        } else {
            VLog.d(TAG, "Android version < 13, notification permission not required");
        }
    }
    
    /**
     * Get notification status for debugging
     */
    public static String getNotificationStatus(Context context) {
        StringBuilder status = new StringBuilder();
        status.append("Notification Status:\n");
        
        // Check if notifications are enabled
        boolean notificationsEnabled = areNotificationsEnabled(context);
        status.append("  Notifications Enabled: ").append(notificationsEnabled ? "YES" : "NO").append("\n");
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Check individual channels
            status.append("  Channel Status:\n");
            status.append("    Daemon Channel: ").append(isChannelEnabled(context, CHANNEL_ID_DAEMON) ? "ENABLED" : "DISABLED").append("\n");
            status.append("    App Notifications: ").append(isChannelEnabled(context, CHANNEL_ID_APP_NOTIFICATIONS) ? "ENABLED" : "DISABLED").append("\n");
            status.append("    System Channel: ").append(isChannelEnabled(context, CHANNEL_ID_SYSTEM) ? "ENABLED" : "DISABLED").append("\n");
            
            // Check channel importance levels
            status.append("  Channel Importance:\n");
            status.append("    Daemon: ").append(getChannelImportance(context, CHANNEL_ID_DAEMON)).append("\n");
            status.append("    App Notifications: ").append(getChannelImportance(context, CHANNEL_ID_APP_NOTIFICATIONS)).append("\n");
            status.append("    System: ").append(getChannelImportance(context, CHANNEL_ID_SYSTEM)).append("\n");
        }
        
        return status.toString();
    }
    
    /**
     * Ensure all notification channels are properly set up
     */
    public static void ensureNotificationSetup(Context context) {
        try {
            VLog.d(TAG, "Ensuring notification setup is complete");
            
            // Create all channels
            createNotificationChannels(context);
            
            // Log current status
            VLog.d(TAG, "Notification setup status:\n" + getNotificationStatus(context));
            
            // Check if we need to request permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (!areNotificationsEnabled(context)) {
                    VLog.w(TAG, "Notifications are disabled, requesting permission");
                    requestNotificationPermission(context);
                }
            }
        } catch (SecurityException e) {
            VLog.w(TAG, "SecurityException during notification setup, continuing with basic setup", e);
            // Still try to create channels even if we can't check status
            try {
                createNotificationChannels(context);
                VLog.d(TAG, "Notification channels created despite security restrictions");
            } catch (Exception ex) {
                VLog.w(TAG, "Failed to create notification channels", ex);
            }
        } catch (Exception e) {
            VLog.w(TAG, "Error during notification setup, continuing with basic setup", e);
            // Still try to create channels even if there's an error
            try {
                createNotificationChannels(context);
                VLog.d(TAG, "Notification channels created despite error");
            } catch (Exception ex) {
                VLog.w(TAG, "Failed to create notification channels", ex);
            }
        }
    }
}
