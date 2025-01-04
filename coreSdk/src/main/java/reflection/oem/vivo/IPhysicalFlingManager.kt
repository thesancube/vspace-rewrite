package reflection.oem.vivo

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 3:40 am
 * IPhysicalFlingManager
 */
class IPhysicalFlingManager {
    companion object {
        val TYPE: Reflector = Reflector.on("vivo.app.physicalfling.IPhysicalFlingManager")
    }

    class Stub {
        companion object {
            val TYPE: Reflector = Reflector.on("vivo.app.physicalfling.IPhysicalFlingManager\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> =
                TYPE.staticMethod("asInterface", IBinder::class.java)
        }
    }
}
