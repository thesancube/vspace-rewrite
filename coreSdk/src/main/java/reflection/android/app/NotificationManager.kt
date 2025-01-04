package reflection.android.app

import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 7:59 am
 * NotificationManager
 */
class NotificationManager {
    companion object {
        val REF: Reflector = Reflector.on("android.app.NotificationManager")

        val sService: Reflector.FieldWrapper<IInterface> = REF.field("sService")
        val getService: Reflector.StaticMethodWrapper<IInterface> = REF.staticMethod("getService")
    }
}
