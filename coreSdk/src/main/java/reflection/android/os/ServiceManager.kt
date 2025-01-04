package reflection.android.os

import android.os.IBinder
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:20 am
 * ServiceManager
 */
class ServiceManager {
    companion object {
        val REF: Reflector = Reflector.on("android.os.ServiceManager")

        val sCache: Reflector.FieldWrapper<Map<String, IBinder>?> = REF.field("sCache")
        val getService: Reflector.StaticMethodWrapper<IBinder?> =
            REF.staticMethod("getService", String::class.java)
    }
}