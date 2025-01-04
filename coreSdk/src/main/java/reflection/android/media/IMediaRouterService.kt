package reflection.android.media

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:01 am
 * IMediaRouterService
 */
class IMediaRouterService {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.media.IMediaRouterService\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> =
                REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}