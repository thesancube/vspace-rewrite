package com.vspace

import android.app.Application
import com.vcore.client.core.VirtualCore

/**
 * @author alex
 * Created 29/12/24 at 2:34 am
 * app
 */

class app : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            android.util.Log.d("VSpace", "Initializing VirtualCore...")
            VirtualCore.get().startup(this)
            android.util.Log.d("VSpace", "✅ VirtualCore initialized successfully!")
        } catch (err: Exception) {
            android.util.Log.e("VSpace", "❌ VirtualCore initialization failed!", err)
            err.printStackTrace()
            
            // Log the specific error for debugging
            android.util.Log.e("VSpace", "Startup error details: ${err.message}")
            if (err.cause != null) {
                android.util.Log.e("VSpace", "Caused by: ${err.cause?.message}")
            }
        }
    }
}