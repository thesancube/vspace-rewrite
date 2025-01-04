package reflection.com.android.internal.content

import android.content.Intent
import reflection.Reflector
import java.sql.Ref

/**
 * @author alex
 * Created 04/01/25 at 6:56 am
 * ReferrerIntent
 */
class ReferrerIntent
{
    companion object {
        val REF: Reflector = Reflector.on("com.android.internal.content.ReferrerIntent")
        val _new : Reflector.ConstructorWrapper<Intent> = REF.constructor(
            Intent::class.java,
            String::class.java
        )
    }
}