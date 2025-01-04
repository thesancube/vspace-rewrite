package reflection.android.os

import reflection.Reflector
import reflection.Reflector.StaticMethodWrapper


/**
 * @author alex
 * Created 04/01/25 at 4:18 am
 * Process
 */
class Process {
    companion object {
        val REF: Reflector = Reflector.on("android.os.Process")
        val setArgV0: Reflector.StaticMethodWrapper<Void?> =
            REF.staticMethod("setArgV0", String::class.java)
    }
}
