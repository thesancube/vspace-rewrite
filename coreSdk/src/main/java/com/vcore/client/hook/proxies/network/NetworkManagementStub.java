package com.vcore.client.hook.proxies.network;

import android.annotation.TargetApi;
import android.os.Build;

import com.vcore.client.hook.base.BinderInvocationProxy;
import com.vcore.client.hook.base.ReplaceUidMethodProxy;

import mirror.android.os.INetworkManagementService;

@TargetApi(Build.VERSION_CODES.M)
public class
NetworkManagementStub extends BinderInvocationProxy {

	public NetworkManagementStub() {
		super(INetworkManagementService.Stub.asInterface, "network_management");
	}

	@Override
	protected void onBindMethods() {
		super.onBindMethods();
		addMethodProxy(new ReplaceUidMethodProxy("setUidCleartextNetworkPolicy", 0));
	}
}
