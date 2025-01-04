package reflection.android.app

import android.content.ComponentName
import android.os.IBinder
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 7:55 am
 * IServiceConnectionO
 */
class IServiceConnectionO {
    companion object {
        val REF: Reflector = Reflector.on("android.app.IServiceConnection")

        val connected: Reflector.MethodWrapper<Void> =
            REF.method("connected", ComponentName::class.java,
                IBinder::class.java,
                Boolean::class.javaPrimitiveType!!)
    }
}
