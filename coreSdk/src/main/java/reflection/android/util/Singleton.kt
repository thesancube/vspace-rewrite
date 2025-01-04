package reflection.android.util

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 3:46 am
 * Singleton
 */
class Singleton {
    companion object {
        val REF: Reflector = Reflector.on("android.util.Singleton")
        val mInstance: Reflector.FieldWrapper<Any?> = REF.field("mInstance")
        val get: Reflector.MethodWrapper<Any?> = REF.method("get")
    }
}
