// IAppRequestListener.aidl
package com.vcore.server.interfaces;

interface IAppRequestListener {
    void onRequestInstall(in String path);
    void onRequestUninstall(in String pkg);
}
