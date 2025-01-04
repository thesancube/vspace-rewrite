package reflection.android.view.accessibility

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 6:44 am
 * IAccessibilityManager
 */
class IAccessibilityManager {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.view.accessibility.IAccessibilityManager\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface?> =
                REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}