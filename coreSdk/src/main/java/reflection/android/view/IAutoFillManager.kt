package reflection.android.view

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 6:41 am
 * IAutoFillManager
 */
class IAutoFillManager {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.view.autofill.IAutoFillManager\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface?> =
                REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}
