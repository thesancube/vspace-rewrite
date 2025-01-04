package reflection.android.os

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:22 am
 * SystemProperties
 */
class SystemProperties {
    companion object {
        val REF: Reflector = Reflector.on("android.os.SystemProperties")

        val get0: Reflector.StaticMethodWrapper<String?> =
            REF.staticMethod("get", String::class.java, String::class.java)
        val get1: Reflector.StaticMethodWrapper<String?> =
            REF.staticMethod("get", String::class.java)
        val getInt: Reflector.StaticMethodWrapper<Int?> =
            REF.staticMethod("getInt", String::class.java, Int::class.java)
    }
}