package reflection.android.content.pm

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:35 am
 * ApplicationInfoL
 */
class ApplicationInfoL {
    companion object {
        val REF: Reflector = Reflector.on("android.content.pm.ApplicationInfo")

        val primaryCpuAbi: Reflector.FieldWrapper<String?> = REF.field("primaryCpuAbi")
        val scanPublicSourceDir: Reflector.FieldWrapper<String?> = REF.field("scanPublicSourceDir")
        val scanSourceDir: Reflector.FieldWrapper<String?> = REF.field("scanSourceDir")
    }
}
