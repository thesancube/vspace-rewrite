package reflection.android.content.pm

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:36 am
 * ApplicationInfoN
 */
class ApplicationInfoN {
    companion object {
        val REF: Reflector = Reflector.on("android.content.pm.ApplicationInfo")

        val credentialEncryptedDataDir: Reflector.FieldWrapper<String?> = REF.field("credentialEncryptedDataDir")
        val credentialProtectedDataDir: Reflector.FieldWrapper<String?> = REF.field("credentialProtectedDataDir")
        val deviceProtectedDataDir: Reflector.FieldWrapper<String?> = REF.field("deviceProtectedDataDir")
    }
}
