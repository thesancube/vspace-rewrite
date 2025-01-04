package sandbox.android.app

import android.content.pm.ProviderInfo
import android.os.IBinder
import android.os.IInterface

/**
 * @author alex
 * Created 05/01/25 at 3:32 am
 * ContentProviderHolder
 */

class ContentProviderHolder {
    val info: ProviderInfo? = null
    var provider: IContentProvider? = null
    var connection: IBinder? = null
}