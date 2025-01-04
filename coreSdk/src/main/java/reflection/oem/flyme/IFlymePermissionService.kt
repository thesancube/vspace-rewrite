package reflection.oem.flyme

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 2:40 am
 * IFlymePermissionService
 */


class IFlymePermissionService {
    companion object {
        val TYPE: Reflector = Reflector.on("meizu.security.IFlymePermissionService")
    }

    class Stub {
        companion object {
            val TYPE: Reflector = Reflector.on("meizu.security.IFlymePermissionService\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> =
                TYPE.staticMethod("asInterface", IBinder::class.java)
        }
    }
}