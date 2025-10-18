package com.vcore.client;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.vcore.helper.utils.VLog;

/**
 * Activity to request permissions for virtual storage access
 * 
 * @author Lody
 */
public class PermissionRequestActivity extends Activity {
    
    private static final String TAG = PermissionRequestActivity.class.getSimpleName();
    
    private PermissionHandler mPermissionHandler;
    private boolean mPermissionsRequested = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        VLog.d(TAG, "PermissionRequestActivity created");
        
        mPermissionHandler = new PermissionHandler(this);
        mPermissionHandler.setCallback(new PermissionHandler.PermissionCallback() {
            @Override
            public void onPermissionsGranted() {
                VLog.d(TAG, "All permissions granted");
                Toast.makeText(PermissionRequestActivity.this, "Permissions granted! Virtual storage is now accessible.", Toast.LENGTH_LONG).show();
                finish();
            }
            
            @Override
            public void onPermissionsDenied(String[] deniedPermissions) {
                VLog.w(TAG, "Some permissions denied: " + java.util.Arrays.toString(deniedPermissions));
                Toast.makeText(PermissionRequestActivity.this, "Some permissions were denied. Virtual storage access may be limited.", Toast.LENGTH_LONG).show();
                
                // Check if we need to request manage storage permission
                if (mPermissionHandler.needsManageStoragePermission()) {
                    requestManageStoragePermission();
                } else {
                    finish();
                }
            }
        });
        
        // Request permissions immediately
        requestPermissions();
    }
    
    private void requestPermissions() {
        if (mPermissionsRequested) {
            return;
        }
        
        mPermissionsRequested = true;
        
        VLog.d(TAG, "Requesting permissions for virtual storage access");
        VLog.d(TAG, "Current permission status:\n" + mPermissionHandler.getPermissionStatus());
        
        // Check if we need manage storage permission first (Android 11+)
        if (mPermissionHandler.needsManageStoragePermission()) {
            VLog.d(TAG, "Need to request manage storage permission first");
            requestManageStoragePermission();
        } else {
            // Request regular permissions
            mPermissionHandler.requestAllPermissions();
        }
    }
    
    private void requestManageStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            VLog.d(TAG, "Requesting manage storage permission");
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, PermissionHandler.REQUEST_MANAGE_STORAGE);
            } catch (Exception e) {
                VLog.w(TAG, "Failed to request manage storage permission", e);
                // Fall back to regular permissions
                mPermissionHandler.requestAllPermissions();
            }
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == PermissionHandler.REQUEST_MANAGE_STORAGE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    VLog.d(TAG, "Manage storage permission granted");
                    Toast.makeText(this, "Manage storage permission granted!", Toast.LENGTH_SHORT).show();
                } else {
                    VLog.w(TAG, "Manage storage permission denied");
                    Toast.makeText(this, "Manage storage permission denied. Some features may not work.", Toast.LENGTH_LONG).show();
                }
            }
            
            // Now request regular permissions
            mPermissionHandler.requestAllPermissions();
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        // Check if permissions were granted while the activity was paused
        if (mPermissionHandler.hasAllPermissions()) {
            VLog.d(TAG, "All permissions granted on resume");
            Toast.makeText(this, "All permissions granted! Virtual storage is now accessible.", Toast.LENGTH_LONG).show();
            finish();
        }
    }
    
    @Override
    public void onBackPressed() {
        // Don't allow back press to skip permission request
        Toast.makeText(this, "Please grant permissions to use virtual storage features.", Toast.LENGTH_SHORT).show();
    }
}
