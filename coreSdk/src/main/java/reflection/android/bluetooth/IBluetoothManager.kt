package reflection.android.bluetooth

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 3:45 am
 * IBluetoothManager
 */
class IBluetoothManager {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.bluetooth.IBluetoothManager\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> =
                REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}
