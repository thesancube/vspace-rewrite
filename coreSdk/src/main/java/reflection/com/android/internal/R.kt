package reflection.com.android.internal

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 6:50 am
 * R
 */
class R {
    class styleable {
        companion object {
            val REF: Reflector = Reflector.on("com.android.internal.R\$styleable")

            val AccountAuthenticator: Reflector.FieldWrapper<Array<Int>?> = REF.field("AccountAuthenticator")
            val AccountAuthenticator_accountPreferences: Reflector.FieldWrapper<Int?> = REF.field("AccountAuthenticator_accountPreferences")
            val AccountAuthenticator_accountType: Reflector.FieldWrapper<Int?> = REF.field("AccountAuthenticator_accountType")
            val AccountAuthenticator_customTokens: Reflector.FieldWrapper<Int?> = REF.field("AccountAuthenticator_customTokens")
            val AccountAuthenticator_icon: Reflector.FieldWrapper<Int?> = REF.field("AccountAuthenticator_icon")
            val AccountAuthenticator_label: Reflector.FieldWrapper<Int?> = REF.field("AccountAuthenticator_label")
            val AccountAuthenticator_smallIcon: Reflector.FieldWrapper<Int?> = REF.field("AccountAuthenticator_smallIcon")
            val Window: Reflector.FieldWrapper<Array<Int>?> = REF.field("Window")
            val Window_windowFullscreen: Reflector.FieldWrapper<Int?> = REF.field("Window_windowFullscreen")
            val Window_windowIsTranslucent: Reflector.FieldWrapper<Int?> = REF.field("Window_windowIsTranslucent")
            val Window_windowShowWallpaper: Reflector.FieldWrapper<Int?> = REF.field("Window_windowShowWallpaper")
        }
    }
}
