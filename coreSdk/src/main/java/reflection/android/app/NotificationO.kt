package reflection.android.app

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 8:00 am
 * NotificationO
 */
class NotificationO {
    companion object {
        val REF: Reflector = Reflector.on("android.app.Notification")

        val mChannelId: Reflector.FieldWrapper<String> = REF.field("mChannelId")
        val mGroupKey: Reflector.FieldWrapper<String> = REF.field("mGroupKey")
    }
}
