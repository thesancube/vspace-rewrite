// IDeviceInfoManager.aidl
package com.vcore.server;

import com.vcore.remote.VDeviceInfo;

interface IDeviceInfoManager {

    VDeviceInfo getDeviceInfo(int userId);

    void updateDeviceInfo(int userId, in VDeviceInfo info);

}
