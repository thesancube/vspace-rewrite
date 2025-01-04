package reflection.com.android.internal.os

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 6:54 am
 * IVibratorService
 */
class IVibratorService {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.os.IVibratorService\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> = REF.staticMethod("asInterface",
                IBinder::class.java)
        }
    }
}
