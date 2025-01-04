package reflection.android.net

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 3:56 am
 * IConnectivityManager
 */
class IConnectivityManager {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.net.IConnectivityManager\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> =
                REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}
