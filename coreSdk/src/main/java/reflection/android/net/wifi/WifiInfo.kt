package reflection.android.net.wifi

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 3:59 am
 * WifiInfo
 */
class WifiInfo {
    companion object {
        val REF: Reflector = Reflector.on("android.net.wifi.WifiInfo")

        val mBSSID: Reflector.FieldWrapper<String?> = REF.field("mBSSID")
        val mMacAddress: Reflector.FieldWrapper<String?> = REF.field("mMacAddress")
        val mWifiSsid: Reflector.FieldWrapper<Any?> = REF.field("mWifiSsid")
    }
}
