package reflection.android.content.pm

import reflection.Reflector
import java.io.File

/**
 * @author alex
 * Created 04/01/25 at 5:00 am
 * PackageParserLollipop22
 */
class PackageParserLollipop22 {
    companion object {
        val REF: Reflector = Reflector.on("android.content.pm.PackageParser")

        val _new: Reflector.ConstructorWrapper<sandbox.android.content.pm.PackageParser?> = REF.constructor() // or may be default PackageParser and Package class

        val collectCertificates: Reflector.MethodWrapper<Void?> =
            REF.method("collectCertificates", sandbox.android.content.pm.PackageParser.Package::class.java, Int::class.java)

        val parsePackage: Reflector.MethodWrapper<sandbox.android.content.pm.PackageParser.Package?> =
            REF.method("parsePackage", File::class.java, Int::class.java)
    }
}