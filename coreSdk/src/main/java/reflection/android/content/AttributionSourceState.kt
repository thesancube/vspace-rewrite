package reflection.android.content

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:27 am
 * AttributionSourceState
 */
class AttributionSourceState {
    companion object {
        val REF: Reflector = Reflector.on("android.content.AttributionSourceState")

        val packageName: Reflector.FieldWrapper<String?> = REF.field("packageName")
        val uid: Reflector.FieldWrapper<Int?> = REF.field("uid")
    }
}
