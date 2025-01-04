package reflection.android.content.pm

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 5:02 am
 * PackageParserNougat
 */
class PackageParserNougat {
    companion object {
        val REF: Reflector = Reflector.on("android.content.pm.PackageParser")
        val collectCertificates: Reflector.StaticMethodWrapper<Void?> =
            REF.staticMethod("collectCertificates", sandbox.android.content.pm.PackageParser.Package::class.java, Int::class.java)
    }
}
