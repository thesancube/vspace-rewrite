package reflection.android.app

import android.content.Intent
import android.os.IBinder
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 7:52 am
 * IActivityManagerL
 */
class IActivityManagerL {
    companion object {
        val REF: Reflector = Reflector.on("android.app.IActivityManager")

        val finishActivity: Reflector.MethodWrapper<Boolean> =
            REF.method("finishActivity", IBinder::class.java, Int::class.javaPrimitiveType!!,
                Intent::class.java,
                Boolean::class.javaPrimitiveType!!)
    }
}
