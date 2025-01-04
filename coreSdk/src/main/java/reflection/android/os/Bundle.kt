package reflection.android.os

import android.os.IBinder
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:13 am
 * Bundle
 */
class Bundle {
    companion object {
        val REF: Reflector = Reflector.on("android.os.Bundle")

        val getIBinder: Reflector.MethodWrapper<IBinder?> =
            REF.method("getIBinder", String::class.java)
        val putIBinder: Reflector.MethodWrapper<Void?> =
            REF.method("putIBinder", String::class.java, IBinder::class.java)
    }
}