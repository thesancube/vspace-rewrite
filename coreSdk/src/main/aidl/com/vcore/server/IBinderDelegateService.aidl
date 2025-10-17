// IBinderDelegateService.aidl
package com.vcore.server;

import android.content.ComponentName;

interface IBinderDelegateService {

   ComponentName getComponent();

   IBinder getService();

}
