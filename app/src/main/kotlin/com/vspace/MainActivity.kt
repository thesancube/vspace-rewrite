package com.vspace

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vcore.client.core.VirtualCore
import com.vspace.ui.theme.VspaceTheme


class MainActivity : ComponentActivity() {

    private lateinit var virtualCore: VirtualCore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        virtualCore = VirtualCore.get()

        setContent {
            VspaceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        // Wait for VirtualCore to be ready before using it
        waitForVirtualCoreAndCloneApp()
    }

    private fun waitForVirtualCoreAndCloneApp() {
        Thread {
            try {
                // Check if VirtualCore startup was successful
                if (!virtualCore.isStartup()) {
                    Log.e("VirtualCore", "VirtualCore startup failed - cannot restart from background thread")
                    runOnUiThread {
                        Toast.makeText(this, "VirtualCore startup failed - please restart the app", Toast.LENGTH_LONG).show()
                    }
                    return@Thread
                }
                
                // Just wait a bit for VirtualCore to initialize
                Thread.sleep(2000)
                
                // Try to ensure server is started
                virtualCore.waitForEngine()
                
                // Wait a bit more for services
                Thread.sleep(3000)
                
                runOnUiThread {
                    Log.d("VirtualCore", "✅ Attempting to clone app...")
                    Toast.makeText(this, "Starting app cloning...", Toast.LENGTH_SHORT).show()
                    cloneInstalledApp("ru.zdevs.zarchiver") {
                        launchApp("ru.zdevs.zarchiver", 0)
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Log.e("VirtualCore", "Error initializing VirtualCore", e)
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }

    fun cloneInstalledApp(packageName: String, installsucess : () -> Unit) {
        Thread {
            try {
                // First check if the app is already installed in VirtualCore
                if (virtualCore.isAppInstalled(packageName)) {
                    Log.d("VirtualCore", "App $packageName is already installed in VirtualCore")
                    runOnUiThread {
                        Toast.makeText(this, "App is already cloned!", Toast.LENGTH_SHORT).show()
                        installsucess.invoke()
                    }
                    return@Thread
                }

                val pm = packageManager
                val appInfo = pm.getApplicationInfo(packageName, 0)
                val apkPath = appInfo.sourceDir

                Log.d("VirtualCore", "Installing app from: $apkPath")

                // Try to install the app with retry mechanism
                val result = installAppWithRetry(apkPath, packageName, 0, 5) // Pass package name

                runOnUiThread {
                    if (result.isSuccess) {
                        Log.d("VirtualCore", "✅ Cloned app: $packageName")
                        Toast.makeText(this, "✅ App cloned successfully!", Toast.LENGTH_SHORT).show()
                        installsucess.invoke()
                    } else {
                        Log.e("VirtualCore", "❌ Installation failed: ${result.error}")
                        Toast.makeText(this, "Installation failed: ${result.error}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("VirtualCore", "Clone error", e)
                runOnUiThread {
                    Toast.makeText(this, "Clone error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }

    private fun installAppWithRetry(apkPath: String, packageName: String, flags: Int, maxRetries: Int): com.vcore.remote.InstallResult {
        var lastError = ""
        
        for (i in 1..maxRetries) {
            try {
                Log.d("VirtualCore", "Installation attempt $i/$maxRetries")
                
                // Ensure server is started before each attempt
                virtualCore.waitForEngine()
                
                // Wait a bit more for services to be ready
                Thread.sleep(2000)
                
                // Try to install
                val result = virtualCore.installPackage(apkPath, flags)
                
                if (result.isSuccess) {
                    Log.d("VirtualCore", "✅ Installation successful on attempt $i")
                    return result
                } else {
                    lastError = result.error ?: "Unknown error"
                    Log.w("VirtualCore", "Installation attempt $i failed: $lastError")
                    
                    // If it's a version conflict, try to uninstall first
                    if (lastError.contains("version downrange") || lastError.contains("Can not update")) {
                        Log.d("VirtualCore", "Version conflict detected, trying to uninstall first...")
                        try {
                            if (virtualCore.isAppInstalled(packageName)) {
                                Log.d("VirtualCore", "Uninstalling existing version of $packageName")
                                virtualCore.uninstallPackage(packageName)
                                Thread.sleep(1000) // Wait a bit after uninstall
                            }
                        } catch (e: Exception) {
                            Log.w("VirtualCore", "Failed to uninstall existing version", e)
                        }
                    }
                }
                
            } catch (e: NullPointerException) {
                lastError = "Service not ready (null pointer)"
                Log.w("VirtualCore", "Installation attempt $i failed - service not ready: $lastError")
            } catch (e: Exception) {
                lastError = e.message ?: "Unknown error"
                Log.w("VirtualCore", "Installation attempt $i failed with exception: $lastError")
            }
            
            // Wait before retry (except on last attempt)
            if (i < maxRetries) {
                try {
                    Thread.sleep(2000) // Wait 2 seconds between retries
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    break
                }
            }
        }
        
        return com.vcore.remote.InstallResult.makeFailure("Installation failed after $maxRetries attempts. Last error: $lastError")
    }
    

    private fun launchApp(packageName: String, userid : Int = 0) {
        try {
            // Skip the service checks since they might fail due to null service
            // Just try to get the launch intent directly
            
            val launchIntent = virtualCore.getLaunchIntent(packageName, userid)

            if (launchIntent != null) {
                startActivity(launchIntent)
                Log.d("VirtualCore", "✅ Launched: $packageName")
                Toast.makeText(this, "✅ Launched: $packageName", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("VirtualCore", "❌ Cannot get launch intent for $packageName")
                Toast.makeText(this, "Cannot launch $packageName - app may not be installed or not launchable", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Log.e("VirtualCore", "Launch error", e)
            Toast.makeText(this, "Launch error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

