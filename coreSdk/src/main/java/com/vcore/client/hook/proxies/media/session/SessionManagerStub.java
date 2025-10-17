package com.vcore.client.hook.proxies.media.session;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import com.vcore.client.hook.base.BinderInvocationProxy;
import com.vcore.client.hook.base.ReplaceCallingPkgMethodProxy;

import mirror.android.media.session.ISessionManager;

/**
 * @author Lody
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class SessionManagerStub extends BinderInvocationProxy {

	public SessionManagerStub() {
		super(ISessionManager.Stub.asInterface, Context.MEDIA_SESSION_SERVICE);
	}

	@Override
	protected void onBindMethods() {
		super.onBindMethods();
		addMethodProxy(new ReplaceCallingPkgMethodProxy("createSession"));
	}
}
