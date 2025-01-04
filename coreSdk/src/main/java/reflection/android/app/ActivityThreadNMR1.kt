package reflection.android.app

import android.os.IBinder
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 7:33 am
 * ActivityThreadNMR1
 */
class ActivityThreadNMR1 {
    companion object {
        val REF: Reflector = Reflector.on("android.app.ActivityThread")

        val performNewIntents: Reflector.MethodWrapper<Void> =
            REF.method("performNewIntents", IBinder::class.java, List::class.java,
                Boolean::class.javaPrimitiveType!!)
    }
}
