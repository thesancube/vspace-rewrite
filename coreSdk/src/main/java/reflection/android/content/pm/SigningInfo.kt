package reflection.android.content.pm

import reflection.Reflector


/**
 * @author alex
 * Created 04/01/25 at 5:15 am
 * SigningInfo
 */
class SigningInfo {
    companion object {
        val REF: Reflector = Reflector.on("android.content.pm.SigningInfo")

        val _new: Reflector.ConstructorWrapper<android.content.pm.SigningInfo> = REF.constructor(
            sandbox.android.content.pm.PackageParser.SigningDetails::class.java) // from sandboxed class
    }
}
