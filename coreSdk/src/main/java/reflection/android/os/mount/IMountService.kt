package reflection.android.os.mount

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:23 am
 * IMountService
 */
class IMountService {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.os.storage.IMountService\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> =
                REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}
