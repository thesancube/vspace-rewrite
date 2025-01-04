package reflection.android.accounts

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 3:44 am
 * IAccountManager
 */
class IAccountManager {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.accounts.IAccountManager\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> =
                REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}