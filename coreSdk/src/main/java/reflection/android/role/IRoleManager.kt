package reflection.android.role

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 3:51 am
 * IRoleManager
 */
class IRoleManager {
    companion object {
        val TYPE: Reflector = Reflector.on("android.app.role.IRoleManager")
    }

    class Stub {
        companion object {
            val TYPE: Reflector = Reflector.on("android.app.role.IRoleManager\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> =
                TYPE.staticMethod("asInterface", IBinder::class.java)
        }
    }
}
