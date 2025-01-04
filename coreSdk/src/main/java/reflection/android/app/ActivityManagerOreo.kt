package reflection.android.app

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 7:26 am
 * ActivityManagerOreo
 */
class ActivityManagerOreo {
    companion object {
        val REF: Reflector = Reflector.on("android.app.ActivityManager")

        val IActivityManagerSingleton: Reflector.FieldWrapper<Any> = REF.field("IActivityManagerSingleton")
    }
}
