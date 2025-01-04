package reflection.android.net.wifi

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:01 am
 * WifiSsid
 */
class WifiSsid {
    companion object {
        val REF: Reflector = Reflector.on("android.net.wifi.WifiSsid")
        val createFromAsciiEncoded: Reflector.StaticMethodWrapper<Any?> =
            REF.staticMethod("createFromAsciiEncoded", String::class.java)
    }
}
