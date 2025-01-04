package reflection.android.app

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.IBinder
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 7:23 am
 * Activity
 */
class Activity {
    companion object {
        val REF: Reflector = Reflector.on("android.app.Activity")

        val mActivityInfo: Reflector.FieldWrapper<ActivityInfo> = REF.field("mActivityInfo")
        val mFinished: Reflector.FieldWrapper<Boolean> = REF.field("mFinished")
        val mParent: Reflector.FieldWrapper<android.app.Activity> = REF.field("mParent")
        val mResultCode: Reflector.FieldWrapper<Int> = REF.field("mResultCode")
        val mResultData: Reflector.FieldWrapper<Intent> = REF.field("mResultData")
        val mToken: Reflector.FieldWrapper<IBinder> = REF.field("mToken")
    }
}
