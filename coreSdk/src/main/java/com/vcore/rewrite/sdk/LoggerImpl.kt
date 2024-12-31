package com.vcore.rewrite.sdk

import android.content.Context
import android.os.Environment
import android.util.Log
import com.vcore.rewrite.utils.log.ShellUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * @author alex
 * Created 29/12/24 at 2:31 am
 * LoggerImpl
 */
class LoggerImpl :  Logger {

    override fun log() {
        runCatching {
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                val logfile = File(
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS), vspace.getContext().packageName + "_log.txt"
                    )
                logfile.delete()
                ShellUtils.execCommand("logcat -c", false)
                ShellUtils.execCommand("logcat -f ${logfile.absolutePath}",false)
            }
        }.onFailure {
            Log.e(vspace.TAG, "log error", it)
        }


    }

}