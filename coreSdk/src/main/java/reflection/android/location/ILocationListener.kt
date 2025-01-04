package reflection.android.location

import android.location.Location
import reflection.Reflector
import android.os.IBinder
import android.os.IInterface


/**
 * @author alex
 * Created 04/01/25 at 4:04 am
 * ILocationListener
 */
class ILocationListener {
    companion object {
        val REF: Reflector = Reflector.on("android.location.ILocationListener")
        val onLocationChanged: Reflector.MethodWrapper<Void?> =
            REF.method("onLocationChanged", Location::class.java)
    }

    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.location.ILocationListener\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> =
                REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}