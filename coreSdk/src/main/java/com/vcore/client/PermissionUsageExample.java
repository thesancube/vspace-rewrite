package com.vcore.client;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.vcore.helper.utils.VLog;

/**
 * Example usage of VirtualStoragePermissionHelper
 * This shows how to integrate permission requests in your main app
 * 
 * @author Lody
 */
public class PermissionUsageExample extends Activity {
    
    private static final String TAG = PermissionUsageExample.class.getSimpleName();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        VLog.d(TAG, "Main activity created, checking permissions");
        
        // Request permissions for virtual storage access
        VirtualStoragePermissionHelper.requestPermissionsIfNeeded(this);
        
        // You can also check permission status
        if (VirtualStoragePermissionHelper.hasAllPermissions(this)) {
            VLog.d(TAG, "All permissions granted");
            Toast.makeText(this, "All permissions granted! Virtual storage is ready.", Toast.LENGTH_SHORT).show();
        } else {
            VLog.w(TAG, "Some permissions missing");
            // Show permission request dialog
            VirtualStoragePermissionHelper.showPermissionRequestDialog(this);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        // Check permissions again when activity resumes
        // This handles cases where user granted permissions while app was in background
        if (VirtualStoragePermissionHelper.hasAllPermissions(this)) {
            VLog.d(TAG, "All permissions granted on resume");
        } else {
            VLog.w(TAG, "Still missing some permissions");
        }
    }
    
    /**
     * Example method to check permission status for debugging
     */
    private void logPermissionStatus() {
        String status = VirtualStoragePermissionHelper.getPermissionStatus(this);
        VLog.d(TAG, "Permission status:\n" + status);
    }
    
    /**
     * Example method to request manage storage permission (Android 11+)
     */
    private void requestManageStorageIfNeeded() {
        VirtualStoragePermissionHelper.requestManageStoragePermission(this);
    }
}
