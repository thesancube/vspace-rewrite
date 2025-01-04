package reflection.android.content.pm

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:38 am
 * IShortcutService
 */
class IShortcutService {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.content.pm.IShortcutService\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface?> =
                REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}
