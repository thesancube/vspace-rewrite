package com.vcore.server.device;

import android.os.Parcel;

import com.vcore.helper.PersistenceLayer;
import com.vcore.helper.collection.SparseArray;
import com.vcore.os.VEnvironment;
import com.vcore.remote.VDeviceInfo;

/**
 * @author Lody
 */

public class DeviceInfoPersistenceLayer extends PersistenceLayer {

    private VDeviceManagerService mService;

    public DeviceInfoPersistenceLayer(VDeviceManagerService service) {
        super(VEnvironment.getDeviceInfoFile());
        this.mService = service;
    }

    @Override
    public int getCurrentVersion() {
        return 1;
    }

    @Override
    public void writeMagic(Parcel p) {

    }

    @Override
    public boolean verifyMagic(Parcel p) {
        return true;
    }

    @Override
    public void writePersistenceData(Parcel p) {
        SparseArray<VDeviceInfo> infos = mService.getDeviceInfos();
        int size = infos.size();
        p.writeInt(size);
        for (int i = 0; i < size; i++) {
            int userId = infos.keyAt(i);
            VDeviceInfo info = infos.valueAt(i);
            p.writeInt(userId);
            info.writeToParcel(p, 0);
        }
    }

    @Override
    public void readPersistenceData(Parcel p) {
        SparseArray<VDeviceInfo> infos = mService.getDeviceInfos();
        infos.clear();
        int size = p.readInt();
        while (size-- > 0) {
            int userId = p.readInt();
            VDeviceInfo info = new VDeviceInfo(p);
            infos.put(userId, info);
        }
    }

    @Override
    public boolean onVersionConflict(int fileVersion, int currentVersion) {
        return false;
    }

    @Override
    public void onPersistenceFileDamage() {
        getPersistenceFile().delete();
    }
}
