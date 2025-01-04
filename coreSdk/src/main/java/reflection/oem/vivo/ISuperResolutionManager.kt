package reflection.oem.vivo

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 3:41 am
 * ISuperResolutionManager
 */
class ISuperResolutionManager {
    companion object {
        val TYPE: Reflector = Reflector.on("vivo.app.superresolution.ISuperResolutionManager")
    }

    class Stub {
        companion object {
            val TYPE: Reflector = Reflector.on("vivo.app.superresolution.ISuperResolutionManager\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> =
                TYPE.staticMethod("asInterface", IBinder::class.java)
        }
    }
}
