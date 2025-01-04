package reflection.android.content.pm

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 6:35 am
 * UserInfo
 */
class UserInfo {
    companion object {
        val REF: Reflector = Reflector.on("android.content.pm.UserInfo")

        val _new: Reflector.ConstructorWrapper<Any?> =
            REF.constructor(Int::class.java, String::class.java, Int::class.java)

        val FLAG_PRIMARY: Reflector.FieldWrapper<Int?> = REF.field("FLAG_PRIMARY")
    }
}

