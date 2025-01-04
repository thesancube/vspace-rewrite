package reflection.android.content

import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:28 am
 * ContentProviderClient
 */
class ContentProviderClient {
    companion object {
        val REF: Reflector = Reflector.on("android.content.ContentProviderClient")

        val mContentProvider: Reflector.FieldWrapper<IInterface?> = REF.field("mContentProvider")
    }
}
