package reflection.android.app

import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 7:46 am
 * ApplicationPackageManager
 */
class ApplicationPackageManager {
    companion object {
        val REF: Reflector = Reflector.on("android.app.ApplicationPackageManager")

        val mPM: Reflector.FieldWrapper<IInterface> = REF.field("mPM")
        val mPermissionManager: Reflector.FieldWrapper<Any> = REF.field("mPermissionManager")
    }
}
