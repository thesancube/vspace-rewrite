package reflection.oem.vivo

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 3:43 am
 * IVivoPermissonService
 */
class IVivoPermissionService {
    companion object {
        val TYPE: Reflector = Reflector.on("vivo.app.security.IVivoPermissionService")
    }

    class Stub {
        companion object {
            val TYPE: Reflector = Reflector.on("vivo.app.security.IVivoPermissionService\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> =
                TYPE.staticMethod("asInterface", IBinder::class.java)
        }
    }
}