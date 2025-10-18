package com.vcore.server;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.vcore.client.core.VirtualCore;
import com.vcore.client.ipc.ServiceManagerNative;
import com.vcore.client.stub.DaemonService;
import com.vcore.helper.compat.BundleCompat;
import com.vcore.server.accounts.VAccountManagerService;
import com.vcore.server.am.BroadcastSystem;
import com.vcore.server.am.VActivityManagerService;
import com.vcore.server.device.VDeviceManagerService;
import com.vcore.server.interfaces.IServiceFetcher;
import com.vcore.server.job.VJobSchedulerService;
import com.vcore.server.location.VirtualLocationService;
import com.vcore.server.notification.VNotificationManagerService;
import com.vcore.server.pm.VAppManagerService;
import com.vcore.server.pm.VPackageManagerService;
import com.vcore.server.pm.VUserManagerService;
import com.vcore.server.vs.VirtualStorageService;

/**
 * @author Lody
 */
public final class BinderProvider extends ContentProvider {

    private final ServiceFetcher mServiceFetcher = new ServiceFetcher();

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DaemonService.startup(context);
        
        // Only initialize services if VirtualCore is ready
        // This prevents null pointer exceptions during early initialization
        if (VirtualCore.get().isStartup()) {
            try {
                VPackageManagerService.systemReady();
                addService(ServiceManagerNative.PACKAGE, VPackageManagerService.get());
                VActivityManagerService.systemReady(context);
                addService(ServiceManagerNative.ACTIVITY, VActivityManagerService.get());
                addService(ServiceManagerNative.USER, VUserManagerService.get());
                VAppManagerService.systemReady();
                addService(ServiceManagerNative.APP, VAppManagerService.get());
                BroadcastSystem.attach(VActivityManagerService.get(), VAppManagerService.get());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    addService(ServiceManagerNative.JOB, VJobSchedulerService.get());
                }
                VNotificationManagerService.systemReady(context);
                addService(ServiceManagerNative.NOTIFICATION, VNotificationManagerService.get());
                VAppManagerService.get().scanApps();
                VAccountManagerService.systemReady();
                addService(ServiceManagerNative.ACCOUNT, VAccountManagerService.get());
                addService(ServiceManagerNative.VS, VirtualStorageService.get());
                addService(ServiceManagerNative.DEVICE, VDeviceManagerService.get());
                addService(ServiceManagerNative.VIRTUAL_LOC, VirtualLocationService.get());
            } catch (Exception e) {
                // Log error but don't fail completely
                android.util.Log.e("BinderProvider", "Error initializing services", e);
            }
        } else {
            android.util.Log.d("BinderProvider", "VirtualCore not ready yet, skipping service initialization");
        }
        return true;
    }


    private void addService(String name, IBinder service) {
        ServiceCache.addService(name, service);
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        if ("@".equals(method)) {
            Bundle bundle = new Bundle();
            BundleCompat.putBinder(bundle, "_VA_|_binder_", mServiceFetcher);
            return bundle;
        } else if ("ensure_created".equals(method)) {
            // Ensure all services are created and ready
            try {
                // Check if services are already initialized
                if (ServiceCache.getService(ServiceManagerNative.APP) == null) {
                    // Only initialize if VirtualCore is ready
                    if (VirtualCore.get().isStartup()) {
                        onCreate();
                    } else {
                        android.util.Log.d("BinderProvider", "VirtualCore not ready in ensure_created, waiting...");
                        // Wait a bit for VirtualCore to be ready
                        long startTime = System.currentTimeMillis();
                        while (!VirtualCore.get().isStartup() && (System.currentTimeMillis() - startTime) < 3000) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                        if (VirtualCore.get().isStartup()) {
                            onCreate();
                        } else {
                            android.util.Log.w("BinderProvider", "VirtualCore still not ready after timeout");
                        }
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putBoolean("success", true);
                return bundle;
            } catch (Exception e) {
                android.util.Log.e("BinderProvider", "Error in ensure_created", e);
                Bundle bundle = new Bundle();
                bundle.putBoolean("success", false);
                bundle.putString("error", e.getMessage());
                return bundle;
            }
        }
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private class ServiceFetcher extends IServiceFetcher.Stub {
        @Override
        public IBinder getService(String name) throws RemoteException {
            if (name != null) {
                return ServiceCache.getService(name);
            }
            return null;
        }

        @Override
        public void addService(String name, IBinder service) throws RemoteException {
            if (name != null && service != null) {
                ServiceCache.addService(name, service);
            }
        }

        @Override
        public void removeService(String name) throws RemoteException {
            if (name != null) {
                ServiceCache.removeService(name);
            }
        }
    }
}
