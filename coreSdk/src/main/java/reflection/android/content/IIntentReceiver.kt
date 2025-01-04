package reflection.android.content

import android.content.Intent
import android.os.Bundle
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:32 am
 * IIntentReceiver
 */
class IIntentReceiver {
    companion object {
        val REF: Reflector = Reflector.on("android.content.IIntentReceiver")

        val performReceive: Reflector.MethodWrapper<Void> = REF.method(
            "performReceive",
            Intent::class.java,
            Int::class.javaPrimitiveType!!,
            String::class.java,
            Bundle::class.java,
            Boolean::class.javaPrimitiveType!!,
            Boolean::class.javaPrimitiveType!!,
            Int::class.javaPrimitiveType!!
        )
    }
}
