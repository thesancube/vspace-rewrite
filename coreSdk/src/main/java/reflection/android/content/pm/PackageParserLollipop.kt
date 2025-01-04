package reflection.android.content.pm

import reflection.Reflector
import java.io.File

/**
 * @author alex
 * Created 04/01/25 at 4:38 am
 * PackageParserLollipop
 */
class PackageParserLollipop {
    companion object {
        val REF: Reflector = Reflector.on("android.content.pm.PackageParser")

        val _new: Reflector.ConstructorWrapper<sandbox.android.content.pm.PackageParser?> = REF.constructor()

        val collectCertificates: Reflector.MethodWrapper<Void?> =
            REF.method("collectCertificates", Package::class.java, Int::class.java)

        val parsePackage: Reflector.MethodWrapper<Package?> =
            REF.method("parsePackage", File::class.java, Int::class.java)
    }
}
