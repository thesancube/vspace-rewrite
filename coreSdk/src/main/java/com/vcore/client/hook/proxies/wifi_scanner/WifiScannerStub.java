package com.vcore.client.hook.proxies.wifi_scanner;

import com.vcore.client.hook.base.BinderInvocationProxy;

/**
 * @author Lody
 */

public class WifiScannerStub extends BinderInvocationProxy {

    public WifiScannerStub() {
        super(new GhostWifiScannerImpl(), "wifiscanner");
    }

}
