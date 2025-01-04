package reflection.android.telephony

import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 3:47 am
 * TelephonyManager
 */
class TelephonyManager {
    companion object {
        val REF: Reflector = Reflector.on("android.telephony.TelephonyManager")

        val getSubscriberInfoService: Reflector.StaticMethodWrapper<Any?> =
            REF.staticMethod("getSubscriberInfoService")

        val sServiceHandleCacheEnabled: Reflector.FieldWrapper<Boolean?> =
            REF.field("sServiceHandleCacheEnabled")

        val sIPhoneSubInfo: Reflector.FieldWrapper<IInterface?> =
            REF.field("sIPhoneSubInfo")

        val getSubscriberInfo: Reflector.MethodWrapper<IInterface?> =
            REF.method("getSubscriberInfo")
    }
}
