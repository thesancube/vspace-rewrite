package reflection.android.media.session

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:02 am
 * ISessionManager
 */
class ISessionManager {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.media.session.ISessionManager\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> =
                REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}