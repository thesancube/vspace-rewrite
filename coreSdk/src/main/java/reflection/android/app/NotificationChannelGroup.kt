package reflection.android.app

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 7:59 am
 * NotificationChannelGroup
 */
class NotificationChannelGroup {
    companion object {
        val REF: Reflector = Reflector.on("android.app.NotificationChannelGroup")

        val mChannels: Reflector.FieldWrapper<List<android.app.NotificationChannel>> = REF.field("mChannels")
        val mId: Reflector.FieldWrapper<String> = REF.field("mId")
    }
}
