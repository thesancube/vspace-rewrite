package reflection.android.app.usage

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector
import reflection.Reflector.StaticMethodWrapper


/**
 * @author alex
 * Created 04/01/25 at 8:07 am
 * IStorageStatsManager
 */
class IStorageStatsManager {
    class Stub {
        companion object {
            val REF : Reflector = Reflector.on("android.app.usage.IStorageStatsManager$Stub")
            var asInterface: StaticMethodWrapper<IInterface> = REF.staticMethod(
                "asInterface",
                IBinder::class.java
            )
        }
    }
}