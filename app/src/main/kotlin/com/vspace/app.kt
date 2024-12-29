package com.vspace

import android.app.Application
import com.vcore.rewrite.sdk.vspace
import dagger.hilt.android.HiltAndroidApp

/**
 * @author alex
 * Created 29/12/24 at 2:34 am
 * app
 */
@HiltAndroidApp
class app : Application() {
    override fun onCreate() {
        super.onCreate()
        vspace.initialize(this)
    }
}