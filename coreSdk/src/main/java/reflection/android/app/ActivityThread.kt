package reflection.android.app

import android.app.Application
import android.app.Instrumentation
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.ProviderInfo
import android.content.pm.ServiceInfo
import android.os.IBinder
import android.os.IInterface
import reflection.Reflector
import reflection.android.os.Handler
import reflection.android.providers.Settings

/**
 * @author alex
 * Created 04/01/25 at 7:28 am
 * ActivityThread
 */
class ActivityThread {
    companion object {
        val REF: Reflector = Reflector.on("android.app.ActivityThread")

        val sPackageManager: Reflector.FieldWrapper<IInterface> = REF.field("sPackageManager")
        val sPermissionManager: Reflector.FieldWrapper<IInterface> = REF.field("sPermissionManager")
        val mActivities: Reflector.FieldWrapper<Map<IBinder, Any>> = REF.field("mActivities")
        val mBoundApplication: Reflector.FieldWrapper<Any> = REF.field("mBoundApplication")
        val mH: Reflector.FieldWrapper<Handler> = REF.field("mH")
        val mInitialApplication: Reflector.FieldWrapper<Application> = REF.field("mInitialApplication")
        val mInstrumentation: Reflector.FieldWrapper<Instrumentation> = REF.field("mInstrumentation")
        val mProviderMap: Reflector.FieldWrapper<Map<*, *>> = REF.field("mProviderMap")

        val currentActivityThread: Reflector.StaticMethodWrapper<Any> = REF.staticMethod("currentActivityThread")

        val getApplicationThread: Reflector.MethodWrapper<IBinder> = REF.method("getApplicationThread")
        val getSystemContext: Reflector.MethodWrapper<Any> = REF.method("getSystemContext")
        val getLaunchingActivity: Reflector.MethodWrapper<Any> = REF.method("getLaunchingActivity", IBinder::class.java)
        val performNewIntents: Reflector.MethodWrapper<Void> = REF.method("performNewIntents", IBinder::class.java, List::class.java)
        val installProvider: Reflector.MethodWrapper<Void> = REF.method("installProvider", Context::class.java, sandbox.android.app.ContentProviderHolder::class.java, ProviderInfo::class.java, Boolean::class.javaPrimitiveType, Boolean::class.javaPrimitiveType, Boolean::class.javaPrimitiveType)
    }

    class CreateServiceData {
        companion object {
            val REF: Reflector = Reflector.on("android.app.ActivityThread\$CreateServiceData")
            val info: Reflector.FieldWrapper<ServiceInfo> = REF.field("info")
        }
    }

    class H {
        companion object {
            val REF: Reflector = Reflector.on("android.app.ActivityThread\$H")
            val CREATE_SERVICE: Reflector.FieldWrapper<Int> = REF.field("CREATE_SERVICE")
            val EXECUTE_TRANSACTION: Reflector.FieldWrapper<Int> = REF.field("EXECUTE_TRANSACTION")
            val LAUNCH_ACTIVITY: Reflector.FieldWrapper<Int> = REF.field("LAUNCH_ACTIVITY")
        }
    }

    class AppBindData {
        companion object {
            val REF: Reflector = Reflector.on("android.app.ActivityThread\$AppBindData")
            val appInfo: Reflector.FieldWrapper<ApplicationInfo> = REF.field("appInfo")
            val info: Reflector.FieldWrapper<Any> = REF.field("info")
            val instrumentationName: Reflector.FieldWrapper<ComponentName> = REF.field("instrumentationName")
            val processName: Reflector.FieldWrapper<String> = REF.field("processName")
            val providers: Reflector.FieldWrapper<List<ProviderInfo>> = REF.field("providers")
        }
    }

    class ProviderClientRecordP {
        companion object {
            val REF: Reflector = Reflector.on("android.app.ActivityThread\$ProviderClientRecord")
            val mNames: Reflector.FieldWrapper<Array<String>> = REF.field("mNames")
            val mProvider: Reflector.FieldWrapper<IInterface> = REF.field("mProvider")
        }
    }

    class ActivityClientRecord {
        companion object {
            val REF: Reflector = Reflector.on("android.app.ActivityThread\$ActivityClientRecord")
            val activity: Reflector.FieldWrapper<Activity> = REF.field("activity")
            val activityInfo: Reflector.FieldWrapper<ActivityInfo> = REF.field("activityInfo")
            val intent: Reflector.FieldWrapper<Intent> = REF.field("intent")
            val token: Reflector.FieldWrapper<IBinder> = REF.field("token")
            val packageInfo: Reflector.FieldWrapper<Any> = REF.field("packageInfo")
        }
    }

    class AndroidOs {
        companion object {
            val REF: Reflector = Reflector.on("android.app.ActivityThread\$AndroidOs")
        }
    }
}
