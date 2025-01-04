package reflection.android.os.storage

import reflection.Reflector
import java.io.File

/**
 * @author alex
 * Created 04/01/25 at 4:25 am
 * StorageVolume
 */
class StorageVolume {
    companion object {
        val REF: Reflector = Reflector.on("android.os.storage.StorageVolume")

        val mInternalPath: Reflector.FieldWrapper<File?> = REF.field("mInternalPath")
        val mPath: Reflector.FieldWrapper<File?> = REF.field("mPath")
    }
}
