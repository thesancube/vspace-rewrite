package reflection.com.android.internal.infra

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 6:55 am
 * AndroidFuture
 */
class AndroidFuture {
    companion object {
        val REF: Reflector = Reflector.on("com.android.internal.infra.AndroidFuture")

        val complete: Reflector.MethodWrapper<Boolean> = REF.method("complete", Any::class.java)

        val ctor: Reflector.ConstructorWrapper<Any> = REF.constructor()
    }
}
