package reflection.android.os

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:16 am
 * IPowerManager
 */
class IPowerManager {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.os.IPowerManager\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> =
                REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}