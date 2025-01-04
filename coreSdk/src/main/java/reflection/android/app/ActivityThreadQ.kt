package reflection.android.app

import android.os.IBinder
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 7:45 am
 * ActivityThreadQ
 */
class ActivityThreadQ {
    companion object {
        val REF: Reflector = Reflector.on("android.app.ActivityThread")

        val handleNewIntent: Reflector.MethodWrapper<Void> =
            REF.method("handleNewIntent", IBinder::class.java, List::class.java)
    }
}
