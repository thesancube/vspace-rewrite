package reflection.android.app

import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 7:47 am
 * AppOpsManager
 */
class AppOpsManager {
    companion object {
        val REF: Reflector = Reflector.on("android.app.AppOpsManager")

        val mService: Reflector.FieldWrapper<IInterface> = REF.field("mService")
    }
}
