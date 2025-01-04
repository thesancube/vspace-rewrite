package reflection.android.os

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:21 am
 * StrictMode
 */
class StrictMode {
    companion object {
        val REF: Reflector = Reflector.on("android.os.StrictMode")
        val DETECT_VM_FILE_URI_EXPOSURE: Reflector.FieldWrapper<Int?> =
            REF.field("DETECT_VM_FILE_URI_EXPOSURE")
        val PENALTY_DEATH_ON_FILE_URI_EXPOSURE: Reflector.FieldWrapper<Int?> =
            REF.field("PENALTY_DEATH_ON_FILE_URI_EXPOSURE")
        val sVmPolicyMask: Reflector.FieldWrapper<Int?> = REF.field("sVmPolicyMask")

        val disableDeathOnFileUriExposure: Reflector.StaticMethodWrapper<Void?> =
            REF.staticMethod("disableDeathOnFileUriExposure")
    }
}
