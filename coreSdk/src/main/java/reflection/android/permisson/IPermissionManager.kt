package reflection.android.permisson

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 3:53 am
 * IPermissionManager
 */
class IPermissionManager {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.permission.IPermissionManager\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> =
                REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}