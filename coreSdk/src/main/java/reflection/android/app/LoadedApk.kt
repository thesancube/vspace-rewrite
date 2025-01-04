package reflection.android.app

import android.app.Application;
import android.app.Instrumentation;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import reflection.Reflector
import java.lang.ref.WeakReference;

import reflection.android.content.IIntentReceiver;
//import android.content.IIntentReceiver;

/**
 * @author alex
 * Created 04/01/25 at 7:56 am
 * LoadedApk
 */
class LoadedApk {
    companion object {
        val REF: Reflector = Reflector.on("android.app.LoadedApk")

        val mApplicationInfo: Reflector.FieldWrapper<ApplicationInfo> = REF.field("mApplicationInfo")
        val mSecurityViolation: Reflector.FieldWrapper<Boolean> = REF.field("mSecurityViolation")

        val getClassLoader: Reflector.MethodWrapper<ClassLoader> = REF.method("getClassLoader")
        val makeApplication: Reflector.MethodWrapper<Application> = REF.method("makeApplication",
            Boolean::class.javaPrimitiveType!!, Instrumentation::class.java)
    }

    class ServiceDispatcher {
        companion object {
            val REF: Reflector = Reflector.on("android.app.LoadedApk\$ServiceDispatcher")

            val mConnection: Reflector.FieldWrapper<ServiceConnection> = REF.field("mConnection")
        }

        class InnerConnection {
            companion object {
                val REF: Reflector = Reflector.on("android.app.LoadedApk\$ServiceDispatcher\$InnerConnection")

                val mDispatcher: Reflector.FieldWrapper<WeakReference<*>> = REF.field("mDispatcher")
            }
        }
    }

    class ReceiverDispatcher {
        companion object {
            val REF: Reflector = Reflector.on("android.app.LoadedApk\$ReceiverDispatcher")

            val mIIntentReceiver: Reflector.FieldWrapper<IIntentReceiver> = REF.field("mIIntentReceiver")
        }

        class InnerReceiver {
            companion object {
                val REF: Reflector = Reflector.on("android.app.LoadedApk\$ReceiverDispatcher\$InnerReceiver")

                val mDispatcher: Reflector.FieldWrapper<WeakReference<*>> = REF.field("mDispatcher")
            }
        }
    }
}
