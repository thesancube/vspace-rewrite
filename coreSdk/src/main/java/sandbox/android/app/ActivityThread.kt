package sandbox.android.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.app.Instrumentation
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ProviderInfo
import android.os.Handler
import android.os.IBinder
import android.util.ArrayMap
import java.lang.ref.WeakReference

/**
 * @author alex
 * Created 05/01/25 at 3:26 am
 * ActivityThread
 */
class ActivityThread {
    var mH: H? = null
    var mBoundApplication: AppBindData? = null
    var mInitialApplication: Application? = null
    var mInstrumentation: Instrumentation? = null
    var mPackages: MutableMap<String, WeakReference<*>>? = null
    var mActivities: MutableMap<IBinder, ActivityClientRecord>? = null
    var mProviderMap: ArrayMap<ProviderKey, Any>? = null

    // Handler for processing lifecycle and interaction messages
    class H : Handler()

    // Retrieve the current ActivityThread instance for the app process
    companion object {
        fun currentActivityThread(): ActivityThread? {
            throw UnsupportedOperationException("Not implemented")
        }
    }

    // Retrieve the process name of the current application
    fun getProcessName(): String? {
        throw UnsupportedOperationException("Not implemented")
    }

    // Retrieve the handler for managing lifecycle events
    fun getHandler(): Handler? {
        return mH
    }

    // Install a content provider and handle its configuration
    fun installProvider(
        context: Context,
        holder: ContentProviderHolder,
        info: ProviderInfo,
        noisy: Boolean,
        noReleaseNeeded: Boolean,
        stable: Boolean
    ): ContentProviderHolder {
        throw UnsupportedOperationException("Not implemented")
    }

    // Holds application binding data
    class AppBindData

    // Represents the client-side information for an activity
    class ActivityClientRecord {
        var activity: Activity? = null
        var token: IBinder? = null
        var activityInfo: ActivityInfo? = null
        var intent: Intent? = null
    }

    // Represents a key for identifying content providers uniquely
    class ProviderKey(val authority: String, val userId: Int) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ProviderKey) return false
            return authority == other.authority && userId == other.userId
        }

        override fun hashCode(): Int {
            return authority.hashCode() xor userId
        }
    }
}
