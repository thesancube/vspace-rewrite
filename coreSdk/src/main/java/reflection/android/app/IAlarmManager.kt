package reflection.android.app

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 7:53 am
 * IAlarmManager
 */
class IAlarmManager {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.app.IAlarmManager\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> = REF.staticMethod("asInterface",
                IBinder::class.java)
        }
    }
}
