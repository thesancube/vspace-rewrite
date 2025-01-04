package reflection.android.content.pm

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 5:15 am
 * ParceledListSlice
 */
class ParceledListSlice {
    companion object {
        val REF: Reflector = Reflector.on("android.content.pm.ParceledListSlice")

        val _new0: Reflector.ConstructorWrapper<Any> = REF.constructor()

        val _new1: Reflector.ConstructorWrapper<Any> = REF.constructor(List::class.java)

        val append: Reflector.MethodWrapper<Boolean> = REF.method("append", Any::class.java)

        val getList: Reflector.MethodWrapper<List<*>> = REF.method("getList")

        val setLastSlice: Reflector.MethodWrapper<Void> = REF.method("setLastSlice", Boolean::class.java)
    }
}
