package reflection.android.content

import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:29 am
 * ContentProviderHolderOreo
 */
class ContentProviderHolderOreo {
    companion object {
        val REF: Reflector = Reflector.on("android.app.ContentProviderHolder")

        val provider: Reflector.FieldWrapper<IInterface?> = REF.field("provider")
    }
}
