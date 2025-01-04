package reflection.android.app

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 7:27 am
 * ActivityTaskManager
 */
class ActivityTaskManager {
    companion object {
        val REF: Reflector = Reflector.on("android.app.ActivityTaskManager")

        val IActivityTaskManagerSingleton: Reflector.FieldWrapper<Any> = REF.field("IActivityTaskManagerSingleton")
    }
}
