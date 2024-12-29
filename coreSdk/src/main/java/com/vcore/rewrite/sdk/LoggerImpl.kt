package com.vcore.rewrite.sdk

/**
 * @author alex
 * Created 29/12/24 at 2:31 am
 * LoggerImpl
 */
class LoggerImpl : Logger {
    override fun log(message: String) {
        println("Log: $message")
    }

}