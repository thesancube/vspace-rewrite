package com.vcore.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.vcore.helper.utils.VLog;

/**
 * Helper class to request permissions for virtual storage access
 * This should be used in the main app activity to request permissions
 * 
 * @author Lody
 */
public class VirtualStoragePermissionHelper {
    
    private static final String TAG = VirtualStoragePermissionHelper.class.getSimpleName();
    
    /**
     * Request all necessary permissions for virtual storage access
     * Call this in your main activity's onCreate or onResume
     */
    public static void requestPermissionsIfNeeded(Activity activity) {
        try {
            PermissionHandler permissionHandler = new PermissionHandler(activity);
            
            VLog.d(TAG, "Checking permissions for virtual storage access");
            VLog.d(TAG, "Current permission status:\n" + permissionHandler.getPermissionStatus());
            
            if (!permissionHandler.hasAllPermissions()) {
                VLog.d(TAG, "Requesting permissions for virtual storage access");
                
                permissionHandler.setCallback(new PermissionHandler.PermissionCallback() {
                    @Override
                    public void onPermissionsGranted() {
                        VLog.d(TAG, "All permissions granted for virtual storage");
                        Toast.makeText(activity, "✅ Virtual storage permissions granted! You can now use sandboxed apps with full storage access.", Toast.LENGTH_LONG).show();
                    }
                    
                    @Override
                    public void onPermissionsDenied(String[] deniedPermissions) {
                        VLog.w(TAG, "Some permissions denied: " + java.util.Arrays.toString(deniedPermissions));
                        Toast.makeText(activity, "⚠️ Some permissions were denied. Virtual storage access may be limited. Please grant all permissions for full functionality.", Toast.LENGTH_LONG).show();
                        
                        // Check if we need to request manage storage permission
                        if (permissionHandler.needsManageStoragePermission()) {
                            requestManageStoragePermission(activity);
                        }
                    }
                });
                
                permissionHandler.requestAllPermissions();
            } else {
                VLog.d(TAG, "All permissions already granted");
            }
        } catch (Exception e) {
            VLog.e(TAG, "Error requesting permissions", e);
            Toast.makeText(activity, "Error requesting permissions: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Request manage storage permission (Android 11+)
     */
    public static void requestManageStoragePermission(Activity activity) {
        try {
            PermissionHandler permissionHandler = new PermissionHandler(activity);
            if (permissionHandler.needsManageStoragePermission()) {
                VLog.d(TAG, "Requesting manage storage permission");
                permissionHandler.requestManageStoragePermission();
            }
        } catch (Exception e) {
            VLog.e(TAG, "Error requesting manage storage permission", e);
        }
    }
    
    /**
     * Check if all required permissions are granted
     */
    public static boolean hasAllPermissions(Context context) {
        try {
            PermissionHandler permissionHandler = new PermissionHandler(context);
            return permissionHandler.hasAllPermissions();
        } catch (Exception e) {
            VLog.e(TAG, "Error checking permissions", e);
            return false;
        }
    }
    
    /**
     * Get permission status for debugging
     */
    public static String getPermissionStatus(Context context) {
        try {
            PermissionHandler permissionHandler = new PermissionHandler(context);
            return permissionHandler.getPermissionStatus();
        } catch (Exception e) {
            VLog.e(TAG, "Error getting permission status", e);
            return "Error getting permission status: " + e.getMessage();
        }
    }
    
    /**
     * Show permission request dialog with explanation
     */
    public static void showPermissionRequestDialog(Activity activity) {
        try {
            VLog.d(TAG, "Showing permission request dialog");
            
            // Create intent to start permission request activity
            Intent intent = new Intent(activity, PermissionRequestActivity.class);
            activity.startActivity(intent);
            
        } catch (Exception e) {
            VLog.e(TAG, "Error showing permission request dialog", e);
            Toast.makeText(activity, "Error showing permission dialog: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
