package reflection.android.content

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:30 am
 * ContentResolver
 */
class ContentResolver {
    companion object {
        val REF: Reflector = Reflector.on("android.content.ContentResolver")

        val mPackageName: Reflector.FieldWrapper<String?> = REF.field("mPackageName")
    }
}
