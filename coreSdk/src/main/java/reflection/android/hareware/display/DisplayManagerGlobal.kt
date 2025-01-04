package reflection.android.hareware.display

import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:10 am
 * DisplayManagerGlobal
 */
class DisplayManagerGlobal {
    companion object {
        val REF: Reflector = Reflector.on("android.hardware.display.DisplayManagerGlobal")
        val mDm: Reflector.FieldWrapper<IInterface?> = REF.field("mDm")
        val getInstance: Reflector.StaticMethodWrapper<Any?> = REF.staticMethod("getInstance")
    }
}
