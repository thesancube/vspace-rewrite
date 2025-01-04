package reflection.android.app

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 7:54 am
 * IApplicationThread
 */
class IApplicationThread {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.app.IApplicationThread\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> = REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}
