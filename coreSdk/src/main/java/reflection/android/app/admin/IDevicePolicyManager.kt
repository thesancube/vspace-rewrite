package reflection.android.app.admin

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 8:03 am
 * IDevicePolicyManager
 */
class IDevicePolicyManager {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.app.admin.IDevicePolicyManager\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> = REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}
