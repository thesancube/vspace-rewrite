package com.vcore.client;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.vcore.helper.utils.VLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Permission handler for virtual apps to request storage and file permissions
 * 
 * @author Lody
 */
public class PermissionHandler {
    
    private static final String TAG = PermissionHandler.class.getSimpleName();
    
    // Storage permissions for different Android versions
    private static final String[] STORAGE_PERMISSIONS = {
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    
    // Android 13+ storage permissions
    private static final String[] STORAGE_PERMISSIONS_13 = {
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.READ_MEDIA_AUDIO
    };
    
    // Notification permission for Android 13+
    private static final String NOTIFICATION_PERMISSION = Manifest.permission.POST_NOTIFICATIONS;
    
    // Request codes
    public static final int REQUEST_STORAGE_PERMISSIONS = 1001;
    public static final int REQUEST_NOTIFICATION_PERMISSION = 1002;
    public static final int REQUEST_MANAGE_STORAGE = 1003;
    
    private Context mContext;
    private PermissionCallback mCallback;
    
    public interface PermissionCallback {
        void onPermissionsGranted();
        void onPermissionsDenied(String[] deniedPermissions);
    }
    
    public PermissionHandler(Context context) {
        this.mContext = context;
    }
    
    public void setCallback(PermissionCallback callback) {
        this.mCallback = callback;
    }
    
    /**
     * Check and request all necessary permissions for virtual storage access
     */
    public void requestAllPermissions() {
        VLog.d(TAG, "Requesting all permissions for virtual storage access");
        
        List<String> permissionsToRequest = new ArrayList<>();
        
        // Check storage permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ - use new media permissions
            for (String permission : STORAGE_PERMISSIONS_13) {
                if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }
        } else {
            // Android 12 and below - use traditional storage permissions
            for (String permission : STORAGE_PERMISSIONS) {
                if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }
        }
        
        // Check notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(mContext, NOTIFICATION_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(NOTIFICATION_PERMISSION);
            }
        }
        
        if (permissionsToRequest.isEmpty()) {
            VLog.d(TAG, "All permissions already granted");
            if (mCallback != null) {
                mCallback.onPermissionsGranted();
            }
            return;
        }
        
        VLog.d(TAG, "Requesting permissions: " + permissionsToRequest);
        
        if (mContext instanceof Activity) {
            ActivityCompat.requestPermissions(
                (Activity) mContext,
                permissionsToRequest.toArray(new String[0]),
                REQUEST_STORAGE_PERMISSIONS
            );
        } else {
            VLog.w(TAG, "Context is not an Activity, cannot request permissions");
            if (mCallback != null) {
                mCallback.onPermissionsDenied(permissionsToRequest.toArray(new String[0]));
            }
        }
    }
    
    /**
     * Handle permission request results
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        VLog.d(TAG, "Permission request result - Code: " + requestCode + ", Permissions: " + java.util.Arrays.toString(permissions));
        
        if (requestCode == REQUEST_STORAGE_PERMISSIONS) {
            List<String> deniedPermissions = new ArrayList<>();
            
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permissions[i]);
                }
            }
            
            if (deniedPermissions.isEmpty()) {
                VLog.d(TAG, "All storage permissions granted");
                if (mCallback != null) {
                    mCallback.onPermissionsGranted();
                }
            } else {
                VLog.w(TAG, "Some permissions denied: " + deniedPermissions);
                if (mCallback != null) {
                    mCallback.onPermissionsDenied(deniedPermissions.toArray(new String[0]));
                }
            }
        }
    }
    
    /**
     * Check if all required permissions are granted
     */
    public boolean hasAllPermissions() {
        // Check storage permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            for (String permission : STORAGE_PERMISSIONS_13) {
                if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        } else {
            for (String permission : STORAGE_PERMISSIONS) {
                if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        
        // Check notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(mContext, NOTIFICATION_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Check if we need to request manage storage permission (Android 11+)
     */
    public boolean needsManageStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return !Environment.isExternalStorageManager();
        }
        return false;
    }
    
    /**
     * Request manage storage permission (Android 11+)
     */
    public void requestManageStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (mContext instanceof Activity) {
                VLog.d(TAG, "Requesting manage storage permission");
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(android.net.Uri.parse("package:" + mContext.getPackageName()));
                    ((Activity) mContext).startActivityForResult(intent, REQUEST_MANAGE_STORAGE);
                } catch (Exception e) {
                    VLog.w(TAG, "Failed to request manage storage permission", e);
                }
            }
        }
    }
    
    /**
     * Get permission status for debugging
     */
    public String getPermissionStatus() {
        StringBuilder status = new StringBuilder();
        status.append("Permission Status:\n");
        
        // Storage permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            for (String permission : STORAGE_PERMISSIONS_13) {
                boolean granted = ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED;
                status.append("  ").append(permission).append(": ").append(granted ? "GRANTED" : "DENIED").append("\n");
            }
        } else {
            for (String permission : STORAGE_PERMISSIONS) {
                boolean granted = ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED;
                status.append("  ").append(permission).append(": ").append(granted ? "GRANTED" : "DENIED").append("\n");
            }
        }
        
        // Notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            boolean granted = ContextCompat.checkSelfPermission(mContext, NOTIFICATION_PERMISSION) == PackageManager.PERMISSION_GRANTED;
            status.append("  ").append(NOTIFICATION_PERMISSION).append(": ").append(granted ? "GRANTED" : "DENIED").append("\n");
        }
        
        // Manage storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            boolean granted = Environment.isExternalStorageManager();
            status.append("  MANAGE_EXTERNAL_STORAGE: ").append(granted ? "GRANTED" : "DENIED").append("\n");
        }
        
        return status.toString();
    }
}
