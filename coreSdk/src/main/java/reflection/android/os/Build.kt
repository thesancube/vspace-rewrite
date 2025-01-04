package reflection.android.os

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:12 am
 * Build
 */
class Build {
    companion object {
        val REF: Reflector = Reflector.on("android.os.Build")
        val BOARD: Reflector.FieldWrapper<String?> = REF.field("BOARD")
        val BRAND: Reflector.FieldWrapper<String?> = REF.field("BRAND")
        val DEVICE: Reflector.FieldWrapper<String?> = REF.field("DEVICE")
        val DISPLAY: Reflector.FieldWrapper<String?> = REF.field("DISPLAY")
        val HOST: Reflector.FieldWrapper<String?> = REF.field("HOST")
        val ID: Reflector.FieldWrapper<String?> = REF.field("ID")
        val MANUFACTURER: Reflector.FieldWrapper<String?> = REF.field("MANUFACTURER")
        val MODEL: Reflector.FieldWrapper<String?> = REF.field("MODEL")
        val PRODUCT: Reflector.FieldWrapper<String?> = REF.field("PRODUCT")
        val TAGS: Reflector.FieldWrapper<String?> = REF.field("TAGS")
        val TYPE: Reflector.FieldWrapper<String?> = REF.field("TYPE")
        val USER: Reflector.FieldWrapper<String?> = REF.field("USER")
    }
}