package reflection.android.os.storage

import android.os.storage.StorageVolume
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:24 am
 * StorageManager
 */
class StorageManager {
    companion object {
        val REF: Reflector = Reflector.on("android.os.storage.StorageManager")

        val getVolumeList: Reflector.StaticMethodWrapper<Array<StorageVolume>?> =
            REF.staticMethod("getVolumeList", Int::class.java, Int::class.java)
    }
}
