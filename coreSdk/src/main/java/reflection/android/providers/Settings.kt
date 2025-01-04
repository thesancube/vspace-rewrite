package reflection.android.providers

import android.os.IInterface
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 3:52 am
 * Settings
 */
class Settings {
    class System {
        companion object {
            val REF: Reflector = Reflector.on("android.provider.Settings\$System")
            val sNameValueCache: Reflector.FieldWrapper<Any?> = REF.field("sNameValueCache")
        }
    }

    class Secure {
        companion object {
            val REF: Reflector = Reflector.on("android.provider.Settings\$Secure")
            val sNameValueCache: Reflector.FieldWrapper<Any?> = REF.field("sNameValueCache")
        }
    }

    class ContentProviderHolder {
        companion object {
            val REF: Reflector = Reflector.on("android.provider.Settings\$ContentProviderHolder")
            val mContentProvider: Reflector.FieldWrapper<IInterface?> = REF.field("mContentProvider")
        }
    }

    class NameValueCacheOreo {
        companion object {
            val REF: Reflector = Reflector.on("android.provider.Settings\$NameValueCache")
            val mProviderHolder: Reflector.FieldWrapper<Any?> = REF.field("mProviderHolder")
        }
    }

    class NameValueCache {
        companion object {
            val REF: Reflector = Reflector.on("android.provider.Settings\$NameValueCache")
            val mContentProvider: Reflector.FieldWrapper<Any?> = REF.field("mContentProvider")
        }
    }

    class Global {
        companion object {
            val REF: Reflector = Reflector.on("android.provider.Settings\$Global")
            val sNameValueCache: Reflector.FieldWrapper<Any?> = REF.field("sNameValueCache")
        }
    }
}
