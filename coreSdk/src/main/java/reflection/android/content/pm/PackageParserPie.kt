package reflection.android.content.pm

import reflection.Reflector

import java.io.File

/**
 * @author alex
 * Created 04/01/25 at 5:02 am
 * PackageParserPie
 */
class PackageParserPie {
    companion object {
        val REF: Reflector = Reflector.on("android.content.pm.PackageParser")

        val _new: Reflector.ConstructorWrapper<sandbox.android.content.pm.PackageParser> = REF.constructor()

        val collectCertificates: Reflector.StaticMethodWrapper<Void> =
            REF.staticMethod("collectCertificates",
                sandbox.android.content.pm.PackageParser.Package::class.java,
                Boolean::class.java // it have javaPrimitiveType on java
            )

        val parsePackage: Reflector.MethodWrapper<sandbox.android.content.pm.PackageParser.Package> =
            REF.method("parsePackage",
                File::class.java,
                Int::class.java // it have javaPrimitiveType on java
            )
    }
}


