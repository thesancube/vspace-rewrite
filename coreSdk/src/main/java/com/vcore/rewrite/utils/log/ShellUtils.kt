package com.vcore.rewrite.utils.log

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader

/**
 * @author alex
 * Created 01/01/25 at 4:16 am
 * ShellUtils
 */

object ShellUtils {
    const val COMMAND_SU = "su"
    const val COMMAND_SH = "sh"
    const val COMMAND_EXIT = "exit\n"
    const val COMMAND_LINE_END = "\n"

    /**
     * Execute shell command, default return result msg
     *
     * @param command command
     * @param isRoot  whether need to run with root
     * @see ShellUtils.execCommand
     */
    fun execCommand(command: String, isRoot: Boolean) {
        execCommand(arrayOf(command), isRoot, true)
    }

    /**
     * Execute shell commands
     *
     * @param commands        command array
     * @param isRoot          whether need to run with root
     * @param isNeedResultMsg whether need result msg
     * @return CommandResult
     * <ul>
     * <li>if isNeedResultMsg is false, [CommandResult.successMsg] is null and
     * <li>if [CommandResult.result] is -1, there maybe some exception.</li>
     * </ul>
     */
    fun execCommand(commands: Array<String>?, isRoot: Boolean, isNeedResultMsg: Boolean): CommandResult {
        var result = -1
        if (commands == null || commands.isEmpty()) {
            return CommandResult(result, null)
        }

        var process: Process? = null
        var successResult: BufferedReader? = null
        var successMsg: StringBuilder? = null
        var os: DataOutputStream? = null

        try {
            process = Runtime.getRuntime().exec(if (isRoot) COMMAND_SU else COMMAND_SH)
            os = DataOutputStream(process.outputStream)

            for (command in commands) {
                if (command.isEmpty()) continue

                os.write(command.toByteArray())
                os.writeBytes(COMMAND_LINE_END)
                os.flush()
            }

            os.writeBytes(COMMAND_EXIT)
            os.flush()

            result = process.waitFor()
            if (isNeedResultMsg) {
                successMsg = StringBuilder()
                successResult = BufferedReader(InputStreamReader(process.inputStream))
                var s: String?
                while (successResult.readLine().also { s = it } != null) {
                    successMsg.append(s).append("\n")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                os?.close()
                successResult?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            process?.destroy()
        }
        return CommandResult(result, successMsg?.toString())
    }

    /**
     * Result of command
     * <ul>
     * <li>[CommandResult.result] means result of command, 0 means normal, else means error, same to execute in
     * linux shell</li>
     * <li>[CommandResult.successMsg] means success message of command result</li>
     * </ul>
     *
     * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-5-16
     */
    data class CommandResult(
        /**
         * result of command
         */
        val result: Int,
        /**
         * success message of command result
         */
        val successMsg: String?
    )
}