package com.vcore.rewrite.sdk

import android.content.Context
import com.vcore.rewrite.init.VcoreEntrypoint
import dagger.hilt.EntryPoints
import reflection.android.os.Build

/**
 * @author alex
 * Created 28/12/24 at 1:47 am
 * vspace
 */

object vspace {
    const val TAG = "Vspace"
    lateinit var logger: Logger
    private lateinit var mainAppContext: Context
    lateinit var entryPoint: VcoreEntrypoint

    fun initialize(context: Context) {
        val entryPoint = EntryPoints.get(context.applicationContext, VcoreEntrypoint::class.java)
        mainAppContext = context
        logger = entryPoint.provideLogger()
        logger.log()
    }
    fun getContext(): Context {
        if (!::mainAppContext.isInitialized) throw IllegalStateException("Library not initialized")
        return mainAppContext
    }


    


}


