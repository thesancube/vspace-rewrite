package reflection.android.location

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:07 am
 * LocationManager
 */

// may have PrimitiveType so this is a bit tricky

class LocationManager {
    class GnssStatusListenerTransport {
        companion object {
            val REF: Reflector = Reflector.on("android.location.LocationManager\$GnssStatusListenerTransport")

            val onGnssStarted: Reflector.MethodWrapper<Void> = REF.method("onGnssStarted")
            val onNmeaReceived: Reflector.MethodWrapper<Void> = REF.method("onNmeaReceived", Long::class.javaPrimitiveType!!, String::class.java)
        }
    }

    class GpsStatusListenerTransport {
        companion object {
            val REF: Reflector = Reflector.on("android.location.LocationManager\$GpsStatusListenerTransport")

            val onNmeaReceived: Reflector.MethodWrapper<Void> = REF.method("onNmeaReceived", Long::class.javaPrimitiveType!!, String::class.java)
        }
    }

    class LocationListenerTransport {
        companion object {
            val REF: Reflector = Reflector.on("android.location.LocationManager\$LocationListenerTransport")

            val mListener: Reflector.FieldWrapper<Any> = REF.field("mListener")
        }
    }
}
