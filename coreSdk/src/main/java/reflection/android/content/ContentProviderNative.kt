package reflection.android.content

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:30 am
 * ContentProviderNative
 */
class ContentProviderNative {
    companion object {
        val REF: Reflector = Reflector.on("android.content.ContentProviderNative")

        val asInterface: Reflector.StaticMethodWrapper<IInterface?> =
            REF.staticMethod("asInterface", IBinder::class.java)
    }
}