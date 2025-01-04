package reflection.android.app

import android.content.Intent
import android.content.pm.ProviderInfo
import android.os.IBinder
import android.os.IInterface
import reflection.Reflector
import reflection.android.os.Bundle

/**
 * @author alex
 * Created 04/01/25 at 7:49 am
 * IActivityManager
 */
class IActivityManager {
    companion object {
        val REF: Reflector = Reflector.on("android.app.IActivityManager")

        val getTaskForActivity: Reflector.MethodWrapper<Int> =
            REF.method("getTaskForActivity", IBinder::class.java, Boolean::class.javaPrimitiveType!!)
        val setRequestedOrientation: Reflector.MethodWrapper<Void> =
            REF.method("setRequestedOrientation", IBinder::class.java, Int::class.javaPrimitiveType!!)
        val startActivity: Reflector.MethodWrapper<Int> =
            REF.method(
                "startActivity",
                Reflector.findClass("android.app.IApplicationThread"),
                String::class.java,
                Intent::class.java,
                String::class.java,
                IBinder::class.java,
                String::class.java,
                Int::class.javaPrimitiveType!!,
                Int::class.javaPrimitiveType!!,
                Reflector.findClass("android.app.ProfilerInfo"),
                Bundle::class.java
            )
    }

    class ContentProviderHolder {
        companion object {
            val REF: Reflector = Reflector.on("android.app.IActivityManager\$ContentProviderHolder")

            val info: Reflector.FieldWrapper<ProviderInfo> = REF.field("info")
            val provider: Reflector.FieldWrapper<IInterface> = REF.field("provider")
        }
    }
}
