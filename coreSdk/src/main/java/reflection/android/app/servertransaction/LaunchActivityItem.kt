package reflection.android.app.servertransaction

import android.content.Intent
import android.content.pm.ActivityInfo
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 8:05 am
 * LaunchActivityItem
 */
class LaunchActivityItem {
    companion object {
        val REF: Reflector = Reflector.on("android.app.servertransaction.LaunchActivityItem")

        val mInfo: Reflector.FieldWrapper<ActivityInfo> = REF.field("mInfo")
        val mIntent: Reflector.FieldWrapper<Intent> = REF.field("mIntent")
    }
}
