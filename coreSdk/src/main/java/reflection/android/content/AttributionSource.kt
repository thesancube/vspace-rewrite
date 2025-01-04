package reflection.android.content

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:27 am
 * AttributionSource
 */
class AttributionSource {
    companion object {
        val REF: Reflector = Reflector.on("android.content.AttributionSource")

        val mAttributionSourceState: Reflector.FieldWrapper<Any?> = REF.field("mAttributionSourceState")
        val getNext: Reflector.MethodWrapper<Any?> = REF.method("getNext")
    }
}