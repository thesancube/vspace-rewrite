package reflection.android.location

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:07 am
 * ILocationManager
 */
class ILocationManager {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.location.ILocationManager\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> =
                REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}