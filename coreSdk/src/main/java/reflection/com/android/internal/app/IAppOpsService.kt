package reflection.com.android.internal.app

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 7:00 am
 * IAppOpsService
 */
class IAppOpsService {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("com.android.internal.app.IAppOpsService\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> = REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}
