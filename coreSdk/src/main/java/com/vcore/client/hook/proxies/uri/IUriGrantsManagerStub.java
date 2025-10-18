package com.vcore.client.hook.proxies.uri;

import com.vcore.client.core.VirtualCore;
import com.vcore.client.hook.base.BinderInvocationProxy;
import com.vcore.client.hook.base.Inject;
import com.vcore.helper.utils.VLog;

import mirror.android.app.IUriGrantsManager;

/**
 * Hook for IUriGrantsManager service to handle virtual app UID validation
 * 
 * @author Lody
 */
@Inject(MethodProxies.class)
public class IUriGrantsManagerStub extends BinderInvocationProxy {

    public IUriGrantsManagerStub() {
        super(IUriGrantsManager.Stub.asInterface, "urigrants");
        VLog.d("IUriGrantsManagerStub", "IUriGrantsManagerStub created with service name: urigrants");
    }

    @Override
    public void inject() throws Throwable {
        VLog.d("IUriGrantsManagerStub", "IUriGrantsManagerStub inject() called");
        super.inject();
    }

    @Override
    public boolean isEnvBad() {
        boolean bad = super.isEnvBad();
        VLog.d("IUriGrantsManagerStub", "IUriGrantsManagerStub isEnvBad() called, result: " + bad);
        return bad;
    }
}
