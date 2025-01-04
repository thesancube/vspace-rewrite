package reflection.android.content.pm

import android.content.pm.ApplicationInfo;
import android.content.pm.Signature;
import android.util.DisplayMetrics;
import reflection.Reflector
import java.io.File

/**
 * @author alex
 * Created 04/01/25 at 4:38 am
 * PackageParser
 */
class PackageParser {
    companion object {
        val REF: Reflector = Reflector.on("android.content.pm.PackageParser")

        val collectCertificates: Reflector.MethodWrapper<Void?> =
            REF.method("collectCertificates", sandbox.android.content.pm.PackageParser.Package::class.java, Int::class.java)

        val parsePackage: Reflector.MethodWrapper<sandbox.android.content.pm.PackageParser.Package?> =
            REF.method("parsePackage", File::class.java, String::class.java, DisplayMetrics::class.java, Int::class.java)
    }

    class Package {
        companion object {
            val REF: Reflector = Reflector.on("android.content.pm.PackageParser\$Package")

            val applicationInfo: Reflector.FieldWrapper<ApplicationInfo?> =
                REF.field("applicationInfo")
        }
    }

    class SigningDetails {
        companion object {
            val REF: Reflector = Reflector.on("android.content.pm.PackageParser\$SigningDetails")

            val signatures: Reflector.FieldWrapper<Array<Signature>?> =
                REF.field("signatures")
        }
    }
}
