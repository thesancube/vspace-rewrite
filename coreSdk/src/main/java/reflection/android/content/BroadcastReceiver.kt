package reflection.android.content

import android.os.Bundle
import android.os.IBinder
import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:27 am
 * BroadcastReceiver
 */
class BroadcastReceiver {
    companion object {
        val REF: Reflector = Reflector.on("android.content.BroadcastReceiver")

        val getPendingResult: Reflector.MethodWrapper<android.content.BroadcastReceiver.PendingResult> =
            REF.method("getPendingResult")
        val setPendingResult: Reflector.MethodWrapper<Void> =
            REF.method("setPendingResult", Reflector.findClass("android.content.BroadcastReceiver\$PendingResult"))
    }

    class PendingResultM {
        companion object {
            val REF: Reflector = Reflector.on("android.content.BroadcastReceiver\$PendingResult")

            val _new: Reflector.ConstructorWrapper<android.content.BroadcastReceiver.PendingResult> = REF.constructor(
                Int::class.javaPrimitiveType!!,
                String::class.java, Bundle::class.java,
                Int::class.javaPrimitiveType!!,
                Boolean::class.javaPrimitiveType!!,
                Boolean::class.javaPrimitiveType!!,
                IBinder::class.java, Int::class.javaPrimitiveType!!,
                Int::class.javaPrimitiveType!!
            )

            val mAbortBroadcast: Reflector.FieldWrapper<Boolean> = REF.field("mAbortBroadcast")
            val mFinished: Reflector.FieldWrapper<Boolean> = REF.field("mFinished")
            val mFlags: Reflector.FieldWrapper<Int> = REF.field("mFlags")
            val mInitialStickyHint: Reflector.FieldWrapper<Boolean> = REF.field("mInitialStickyHint")
            val mOrderedHint: Reflector.FieldWrapper<Boolean> = REF.field("mOrderedHint")
            val mResultData: Reflector.FieldWrapper<String> = REF.field("mResultData")
            val mResultExtras: Reflector.FieldWrapper<Bundle> = REF.field("mResultExtras")
            val mSendingUser: Reflector.FieldWrapper<Int> = REF.field("mSendingUser")
            val mToken: Reflector.FieldWrapper<IBinder> = REF.field("mToken")
            val mType: Reflector.FieldWrapper<Int> = REF.field("mType")
        }
    }

    class PendingResult {
        companion object {
            val REF: Reflector = Reflector.on("android.content.BroadcastReceiver\$PendingResult")

            val _new: Reflector.ConstructorWrapper<android.content.BroadcastReceiver.PendingResult> = REF.constructor(
                Int::class.javaPrimitiveType!!,
                String::class.java, Bundle::class.java,
                Int::class.javaPrimitiveType!!,
                Boolean::class.javaPrimitiveType!!,
                Boolean::class.javaPrimitiveType!!,
                IBinder::class.java,
                Int::class.javaPrimitiveType!!
            )

            val mAbortBroadcast: Reflector.FieldWrapper<Boolean> = REF.field("mAbortBroadcast")
            val mFinished: Reflector.FieldWrapper<Boolean> = REF.field("mFinished")
            val mInitialStickyHint: Reflector.FieldWrapper<Boolean> = REF.field("mInitialStickyHint")
            val mOrderedHint: Reflector.FieldWrapper<Boolean> = REF.field("mOrderedHint")
            val mResultData: Reflector.FieldWrapper<String> = REF.field("mResultData")
            val mResultExtras: Reflector.FieldWrapper<Bundle> = REF.field("mResultExtras")
            val mSendingUser: Reflector.FieldWrapper<Int> = REF.field("mSendingUser")
            val mToken: Reflector.FieldWrapper<IBinder> = REF.field("mToken")
            val mType: Reflector.FieldWrapper<Int> = REF.field("mType")
        }
    }
}

