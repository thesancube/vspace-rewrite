package reflection.android.app

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 7:58 am
 * NotificationChannel
 */
class NotificationChannel {
    companion object {
        val REF: Reflector = Reflector.on("android.app.NotificationChannel")

        val mId: Reflector.FieldWrapper<String> = REF.field("mId")
    }
}
