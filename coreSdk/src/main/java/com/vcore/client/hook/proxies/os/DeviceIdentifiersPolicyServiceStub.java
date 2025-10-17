package com.vcore.client.hook.proxies.os;

import android.content.pm.ApplicationInfo;

import com.vcore.client.VClientImpl;
import com.vcore.client.hook.base.BinderInvocationProxy;
import com.vcore.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.vcore.helper.compat.BuildCompat;

import java.lang.reflect.Method;

import mirror.android.os.IDeviceIdentifiersPolicyService;

/**
 * @author weishu
 * @date 2021/8/19.
 */
public class DeviceIdentifiersPolicyServiceStub extends BinderInvocationProxy {
    public DeviceIdentifiersPolicyServiceStub() {
        super(IDeviceIdentifiersPolicyService.Stub.TYPE, "device_identifiers");
    }

    @Override
    protected void onBindMethods() {

        addMethodProxy(new ReplaceCallingPkgMethodProxy("getSerialForPackage") {
            @Override
            public Object call(Object who, Method method, Object... args) throws Throwable {
                ApplicationInfo info = VClientImpl.get().getCurrentApplicationInfo();
                if (info != null && info.targetSdkVersion >= 29 && BuildCompat.isQ()) {
                    return "unknown";
                }
                return super.call(who, method, args);
            }
        });
    }
}
