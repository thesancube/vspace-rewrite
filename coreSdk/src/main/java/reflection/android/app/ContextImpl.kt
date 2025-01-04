package reflection.android.app

import android.content.Context
import android.content.pm.PackageManager
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 7:48 am
 * ContextImpl
 */
class ContextImpl {
    companion object {
        val REF: Reflector = Reflector.on("android.app.ContextImpl")

        val mBasePackageName: Reflector.FieldWrapper<String> = REF.field("mBasePackageName")
        val mPackageInfo: Reflector.FieldWrapper<Any> = REF.field("mPackageInfo")
        val mPackageManager: Reflector.FieldWrapper<PackageManager> = REF.field("mPackageManager")

        val setOuterContext: Reflector.MethodWrapper<Void> = REF.method("setOuterContext", Context::class.java)
        val getAttributionSource: Reflector.MethodWrapper<Any> = REF.method("getAttributionSource")
    }
}
