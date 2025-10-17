package com.vcore.client.hook.proxies.connectivity;

import android.content.Context;

import com.vcore.client.hook.base.BinderInvocationProxy;
import com.vcore.client.hook.base.MethodProxy;
import com.vcore.client.hook.base.ReplaceLastPkgMethodProxy;
import com.vcore.client.hook.base.StaticMethodProxy;
import com.vcore.client.ipc.ServiceManagerNative;

import java.lang.reflect.Method;

import mirror.android.net.IConnectivityManager;

/**
 * @author legency
 */
public class ConnectivityStub extends BinderInvocationProxy {

    public ConnectivityStub() {
        super(IConnectivityManager.Stub.asInterface, Context.CONNECTIVITY_SERVICE);
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
    }
}
