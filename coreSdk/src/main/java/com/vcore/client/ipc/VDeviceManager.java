package com.vcore.client.ipc;

import android.os.IBinder;
import android.os.RemoteException;

import com.vcore.client.core.VirtualCore;
import com.vcore.client.env.VirtualRuntime;
import com.vcore.remote.VDeviceInfo;
import com.vcore.server.IDeviceInfoManager;

/**
 * @author Lody
 */

public class VDeviceManager {

    private static final VDeviceManager sInstance = new VDeviceManager();
    private IDeviceInfoManager mRemote;


    public static VDeviceManager get() {
        return sInstance;
    }


    public IDeviceInfoManager getRemote() {
        if (mRemote == null ||
                (!mRemote.asBinder().pingBinder() && !VirtualCore.get().isVAppProcess())) {
            synchronized (this) {
                Object remote = getRemoteInterface();
                mRemote = LocalProxyUtils.genProxy(IDeviceInfoManager.class, remote);
            }
        }
        return mRemote;
    }

    private Object getRemoteInterface() {
        final IBinder binder = ServiceManagerNative.getService(ServiceManagerNative.DEVICE);
        return IDeviceInfoManager.Stub.asInterface(binder);
    }

    public VDeviceInfo getDeviceInfo(int userId) {
        try {
            return getRemote().getDeviceInfo(userId);
        } catch (RemoteException e) {
            return VirtualRuntime.crash(e);
        }
    }
}
