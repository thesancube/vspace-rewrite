package reflection.android.app.servertransaction

import android.os.IBinder
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 8:04 am
 * ClientTransaction
 */
class ClientTransaction {
    companion object {
        val REF: Reflector = Reflector.on("android.app.servertransaction.ClientTransaction")

        val mActivityCallbacks: Reflector.FieldWrapper<List<Any>> = REF.field("mActivityCallbacks")
        val mActivityToken: Reflector.FieldWrapper<IBinder> = REF.field("mActivityToken")
    }
}
