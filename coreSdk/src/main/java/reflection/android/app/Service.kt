package reflection.android.app

import android.app.Application
import android.content.Context
import android.os.IBinder
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 8:00 am
 * Service
 */
// activity thread attach to service for virtual machine
class Service {
    companion object {
        val REF: Reflector = Reflector.on("android.app.Service")

        val attach: Reflector.MethodWrapper<Void> =
            REF.method("attach",
                Context::class.java,
                sandbox.android.app.ActivityThread::class.java,
                String::class.java,
                IBinder::class.java,
                Application::class.java,
                Any::class.java)
    }
}
