package reflection.com.android.internal.telephony

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 6:50 am
 * ISub
 */
class ISub {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("com.android.internal.telephony.ISub\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface?> =
                REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}
