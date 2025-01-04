package reflection.android.graphics

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 3:55 am
 * Compatibility
 */
class Compatibility {
    companion object {
        val REF: Reflector = Reflector.on("android.graphics.Compatibility")
        val setTargetSdkVersion: Reflector.StaticMethodWrapper<Void?> =
            REF.staticMethod("setTargetSdkVersion", Int::class.javaPrimitiveType!!)
    }
}
