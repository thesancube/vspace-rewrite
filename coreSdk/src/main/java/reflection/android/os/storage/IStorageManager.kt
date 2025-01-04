package reflection.android.os.storage

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:23 am
 * IStorageManager
 */
class IStorageManager {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.os.storage.IStorageManager\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> =
                REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}
