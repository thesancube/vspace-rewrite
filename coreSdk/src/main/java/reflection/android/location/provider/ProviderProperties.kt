package reflection.android.location.provider

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:03 am
 * ProviderProperties
 */
class ProviderProperties {
    companion object {
        val REF: Reflector = Reflector.on("android.location.provider.ProviderProperties")

        val mHasNetworkRequirement: Reflector.FieldWrapper<Boolean?> = REF.field("mHasNetworkRequirement")
        val mHasCellRequirement: Reflector.FieldWrapper<Boolean?> = REF.field("mHasCellRequirement")
    }
}