package reflection.com.android.internal.net

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 6:55 am
 * VpnConfig
 */
class VpnConfig {
    companion object {
        val REF: Reflector = Reflector.on("com.android.internal.net.VpnConfig")

        val user: Reflector.FieldWrapper<String> = REF.field("user")

        val disallowedApplications: Reflector.FieldWrapper<List<String>> = REF.field("disallowedApplications")

        val allowedApplications: Reflector.FieldWrapper<List<String>> = REF.field("allowedApplications")
    }
}
