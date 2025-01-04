package reflection.android.app.job

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 8:03 am
 * IJobScheduler
 */
class IJobScheduler {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.app.job.IJobScheduler\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> = REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}
