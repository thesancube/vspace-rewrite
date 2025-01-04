package reflection.android.app

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 7:48 am
 * ContextImplKitkat
 */
class ContextImplKitkat {
    companion object {
        val REF: Reflector = Reflector.on("android.app.ContextImpl")

        val mOpPackageName: Reflector.FieldWrapper<String> = REF.field("mOpPackageName")
    }
}
