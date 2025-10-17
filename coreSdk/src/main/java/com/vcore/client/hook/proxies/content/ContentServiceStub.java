package com.vcore.client.hook.proxies.content;

import com.vcore.client.hook.base.BinderInvocationProxy;
import com.vcore.client.hook.base.Inject;

import mirror.android.content.IContentService;

/**
 * @author Lody
 * @see IContentService
 */
@Inject(MethodProxies.class)
public class ContentServiceStub extends BinderInvocationProxy {

    public ContentServiceStub() {
        super(IContentService.Stub.asInterface, "content");
    }
}
