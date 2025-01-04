package reflection.com.android.internal.appwidget

import android.os.IBinder
import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 6:59 am
 * IAppWidgetService
 */
class IAppWidgetService {
    class Stub {
        companion object {
            val REF: Reflector = Reflector.on("com.android.internal.appwidget.IAppWidgetService\$Stub")
            val asInterface: Reflector.StaticMethodWrapper<IInterface> = REF.staticMethod("asInterface", IBinder::class.java)
        }
    }
}
