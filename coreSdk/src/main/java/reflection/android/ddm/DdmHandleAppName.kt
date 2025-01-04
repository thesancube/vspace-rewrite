package reflection.android.ddm

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 3:54 am
 * DdmHandleAppName
 */
class DdmHandleAppName {
    companion object {
        val REF: Reflector = Reflector.on("android.ddm.DdmHandleAppName")
        val setAppName: Reflector.StaticMethodWrapper<Void?> =
            REF.staticMethod("setAppName", String::class.java, Int::class.javaPrimitiveType!!)
    }
}
