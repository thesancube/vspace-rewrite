package reflection.oem.vivo

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 3:42 am
 * ISystemDefenceManager
 */
class ISystemDefenceManager {
    companion object {
        val TYPE: Reflector = Reflector.on("vivo.app.systemdefence.ISystemDefenceManager")
    }

    class Stub {
        companion object {
            val TYPE: Reflector = Reflector.on("vivo.app.systemdefence.ISystemDefenceManager\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> =
                TYPE.staticMethod("asInterface", IBinder::class.java)
        }
    }
}
