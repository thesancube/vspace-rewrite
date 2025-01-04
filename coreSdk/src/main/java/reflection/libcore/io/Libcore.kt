package reflection.libcore.io

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 7:13 am
 * Libcore
 */
class Libcore {
    companion object {
        val REF: Reflector = Reflector.on("libcore.io.Libcore")

        val os: Reflector.FieldWrapper<Any> = REF.field("os")
    }
}
