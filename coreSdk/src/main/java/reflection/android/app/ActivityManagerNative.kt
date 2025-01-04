package reflection.android.app

import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 7:25 am
 * ActivityManagerNative
 */
class ActivityManagerNative {
    companion object {
        val REF: Reflector = Reflector.on("android.app.ActivityManagerNative")

        val gDefault: Reflector.FieldWrapper<Any> = REF.field("gDefault")

        val getDefault: Reflector.StaticMethodWrapper<IInterface> = REF.staticMethod("getDefault")
    }
}
