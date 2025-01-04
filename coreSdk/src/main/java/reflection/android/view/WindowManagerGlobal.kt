package reflection.android.view

import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 6:43 am
 * WindowManagerGlobal
 */
class WindowManagerGlobal {
    companion object {
        val REF: Reflector = Reflector.on("android.view.WindowManagerGlobal")

        val sWindowManagerService: Reflector.FieldWrapper<IInterface?> =
            REF.field("sWindowManagerService")
    }
}
