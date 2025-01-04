package reflection.android.os

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:23 am
 * UserHandle
 */
class UserHandle {
    companion object {
        val REF: Reflector = Reflector.on("android.os.UserHandle")

        val myUserId: Reflector.StaticMethodWrapper<Int?> =
            REF.staticMethod("myUserId")
    }
}