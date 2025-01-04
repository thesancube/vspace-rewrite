package reflection.android.net.wifi

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 3:58 am
 * IWifiManager
 */
class IWifiManager {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.net.wifi.IWifiManager\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> =
                REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}
