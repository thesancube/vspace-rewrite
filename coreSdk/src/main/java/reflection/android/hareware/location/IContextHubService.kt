package reflection.android.hareware.location

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:11 am
 * IContextHubService
 */
class IContextHubService {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.hardware.location.IContextHubService\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> =
                REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}