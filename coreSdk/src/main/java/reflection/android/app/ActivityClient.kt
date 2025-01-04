package reflection.android.app

import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 7:24 am
 * ActivityClient
 */
class ActivityClient {
    companion object {
        val REF: Reflector = Reflector.on("android.app.ActivityClient")

        val INTERFACE_SINGLETON: Reflector.FieldWrapper<Any> = REF.field("INTERFACE_SINGLETON")

        val getInstance: Reflector.StaticMethodWrapper<Any> = REF.staticMethod("getInstance")
        val getActivityClientController: Reflector.StaticMethodWrapper<Any> = REF.staticMethod("getActivityClientController")
    }

    class ActivityClientControllerSingleton {
        companion object {
            val REF: Reflector = Reflector.on("android.app.ActivityClient\$ActivityClientControllerSingleton")

            val mKnownInstance: Reflector.FieldWrapper<IInterface> = REF.field("mKnownInstance")
        }
    }
}
