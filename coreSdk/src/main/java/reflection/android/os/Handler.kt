package reflection.android.os

import reflection.Reflector
import android.os.Handler.Callback

/**
 * @author alex
 * Created 04/01/25 at 4:14 am
 * Handler
 */
class Handler {
    companion object {
        val REF: Reflector = Reflector.on("android.os.Handler")
        val mCallback: Reflector.FieldWrapper<Callback?> = REF.field("mCallback")
    }
}