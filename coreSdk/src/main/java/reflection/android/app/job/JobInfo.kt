package reflection.android.app.job

import android.content.ComponentName
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 8:04 am
 * JobInfo
 */
class JobInfo {
    companion object {
        val REF: Reflector = Reflector.on("android.app.job.JobInfo")

        val service: Reflector.FieldWrapper<ComponentName> = REF.field("service")
    }
}
