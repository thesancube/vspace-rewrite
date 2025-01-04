package reflection.android.service.persistentdata

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 3:48 am
 * IPersistentDataBlockService
 */
class IPersistentDataBlockService {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.service.persistentdata.IPersistentDataBlockService\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> =
                REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}
