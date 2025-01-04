package reflection.dalvik

import reflection.Reflector
import reflection.Reflector.StaticMethodWrapper


/**
 * @author alex
 * Created 04/01/25 at 7:01 am
 * VMRuntime
 */
class VMRuntime {
    companion object {
        val REF: Reflector = Reflector.on("dalvik.system.VMRuntime")
        var getRuntime: StaticMethodWrapper<Any> = REF.staticMethod("getRuntime")
        var setTargetSdkVersion: Reflector.MethodWrapper<Void> = REF.method(
            "setTargetSdkVersion",
            Int::class.javaPrimitiveType!!  //  because we need raw Int not class
        )
    }
}