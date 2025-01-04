package reflection.android.content.pm

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:36 am
 * ILauncherApps
 */
class ILauncherApps {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("android.content.pm.ILauncherApps\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface?> = REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}