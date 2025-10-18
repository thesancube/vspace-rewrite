# Fixed VirtualCore Usage - Proper Initialization

## ❌ The Problem
You're getting this error:
```
java.lang.NullPointerException: Attempt to invoke interface method 'com.vcore.remote.InstallResult com.vcore.server.IAppManager.installPackage(java.lang.String, int)' on a null object reference
```

This happens because you're trying to use VirtualCore before it's fully initialized.

## ✅ The Solution

### **1. Fix Your Application Class**

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize VirtualCore
        try {
            VirtualCore.get().startup(this)
            Log.d("VirtualCore", "✅ VirtualCore startup initiated")
        } catch (e: Exception) {
            Log.e("VirtualCore", "❌ VirtualCore startup failed", e)
        }
    }
}
```

### **2. Fix Your MainActivity**

```kotlin
class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Wait for VirtualCore to be ready before using it
        waitForVirtualCoreAndCloneApp()
    }
    
    private fun waitForVirtualCoreAndCloneApp() {
        Thread {
            try {
                // Wait for VirtualCore to be ready (max 10 seconds)
                val isReady = VirtualCore.get().waitForReady(10000)
                
                runOnUiThread {
                    if (isReady) {
                        Log.d("VirtualCore", "✅ VirtualCore is ready!")
                        cloneInstalledApp()
                    } else {
                        Log.e("VirtualCore", "❌ VirtualCore not ready after timeout")
                        Toast.makeText(this, "VirtualCore initialization failed", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Log.e("VirtualCore", "Error waiting for VirtualCore", e)
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }
    
    private fun cloneInstalledApp() {
        try {
            // Check if VirtualCore is ready
            if (!VirtualCore.get().isReady()) {
                Log.e("VirtualCore", "VirtualCore is not ready!")
                return
            }
            
            // Your cloning logic here
            val packageName = "com.whatsapp" // Example package
            val apkPath = "/storage/emulated/0/Download/whatsapp.apk"
            
            if (VirtualCore.get().isAppInstalled(packageName)) {
                Log.d("VirtualCore", "App already installed: $packageName")
                launchApp(packageName)
            } else {
                installApp(apkPath, packageName)
            }
            
        } catch (e: Exception) {
            Log.e("VirtualCore", "Error in cloneInstalledApp", e)
        }
    }
    
    private fun installApp(apkPath: String, packageName: String) {
        Thread {
            try {
                val result = VirtualCore.get().installPackage(apkPath, 0)
                
                runOnUiThread {
                    if (result.isSuccess) {
                        Log.d("VirtualCore", "✅ Installed: ${result.packageName}")
                        launchApp(packageName)
                    } else {
                        Log.e("VirtualCore", "❌ Installation failed: ${result.error}")
                        Toast.makeText(this, "Installation failed: ${result.error}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Log.e("VirtualCore", "Installation error", e)
                    Toast.makeText(this, "Installation error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }
    
    private fun launchApp(packageName: String) {
        try {
            val launchIntent = VirtualCore.get().getLaunchIntent(packageName, 0)
            if (launchIntent != null) {
                startActivity(launchIntent)
                Log.d("VirtualCore", "✅ Launched: $packageName")
            } else {
                Log.e("VirtualCore", "❌ Cannot get launch intent for $packageName")
            }
        } catch (e: Exception) {
            Log.e("VirtualCore", "Launch error", e)
        }
    }
}
```

### **3. Alternative: Use Callback Pattern**

```kotlin
class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Initialize VirtualCore with callback
        initializeVirtualCore {
            // This runs when VirtualCore is ready
            cloneInstalledApp()
        }
    }
    
    private fun initializeVirtualCore(onReady: () -> Unit) {
        Thread {
            try {
                // Wait for VirtualCore to be ready
                val isReady = VirtualCore.get().waitForReady(10000)
                
                runOnUiThread {
                    if (isReady) {
                        Log.d("VirtualCore", "✅ VirtualCore is ready!")
                        onReady()
                    } else {
                        Log.e("VirtualCore", "❌ VirtualCore not ready after timeout")
                        Toast.makeText(this, "VirtualCore initialization failed", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Log.e("VirtualCore", "Error waiting for VirtualCore", e)
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }
    
    private fun cloneInstalledApp() {
        // Your app cloning logic here
        Log.d("VirtualCore", "Starting app cloning...")
    }
}
```

### **4. Safe VirtualCore Manager Class**

```kotlin
class VirtualCoreManager private constructor() {
    
    companion object {
        @Volatile
        private var INSTANCE: VirtualCoreManager? = null
        
        fun getInstance(): VirtualCoreManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: VirtualCoreManager().also { INSTANCE = it }
            }
        }
    }
    
    private val virtualCore = VirtualCore.get()
    private var isInitialized = false
    
    fun initialize(context: Context, callback: (Boolean) -> Unit) {
        Thread {
            try {
                virtualCore.startup(context)
                
                // Wait for services to be ready
                val isReady = virtualCore.waitForReady(10000)
                
                isInitialized = isReady
                callback(isReady)
                
            } catch (e: Exception) {
                isInitialized = false
                callback(false)
            }
        }.start()
    }
    
    fun installApp(apkPath: String, callback: (InstallResult) -> Unit) {
        if (!isInitialized) {
            callback(InstallResult.makeFailure("VirtualCore not initialized"))
            return
        }
        
        Thread {
            try {
                val result = virtualCore.installPackage(apkPath, 0)
                callback(result)
            } catch (e: Exception) {
                callback(InstallResult.makeFailure("Installation error: ${e.message}"))
            }
        }.start()
    }
    
    fun launchApp(packageName: String, context: Context): Boolean {
        if (!isInitialized) {
            Log.e("VirtualCore", "VirtualCore not initialized")
            return false
        }
        
        return try {
            val launchIntent = virtualCore.getLaunchIntent(packageName, 0)
            if (launchIntent != null) {
                context.startActivity(launchIntent)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("VirtualCore", "Launch error", e)
            false
        }
    }
    
    fun isReady(): Boolean = isInitialized && virtualCore.isReady()
}
```

### **5. Usage with Manager Class**

```kotlin
class MainActivity : AppCompatActivity() {
    
    private lateinit var virtualCoreManager: VirtualCoreManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        virtualCoreManager = VirtualCoreManager.getInstance()
        
        // Initialize VirtualCore
        virtualCoreManager.initialize(this) { success ->
            if (success) {
                Log.d("VirtualCore", "✅ VirtualCore ready!")
                cloneInstalledApp()
            } else {
                Log.e("VirtualCore", "❌ VirtualCore initialization failed")
                Toast.makeText(this, "VirtualCore initialization failed", Toast.LENGTH_LONG).show()
            }
        }
    }
    
    private fun cloneInstalledApp() {
        val apkPath = "/storage/emulated/0/Download/whatsapp.apk"
        val packageName = "com.whatsapp"
        
        virtualCoreManager.installApp(apkPath) { result ->
            runOnUiThread {
                if (result.isSuccess) {
                    Log.d("VirtualCore", "✅ Installed: ${result.packageName}")
                    virtualCoreManager.launchApp(packageName, this)
                } else {
                    Log.e("VirtualCore", "❌ Installation failed: ${result.error}")
                    Toast.makeText(this, "Installation failed: ${result.error}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
```

## 🔧 Key Points

1. **Always wait for VirtualCore to be ready** before using any methods
2. **Use `waitForReady()`** with a timeout to ensure services are initialized
3. **Check `isReady()`** before calling any VirtualCore methods
4. **Run initialization on background thread** to avoid blocking UI
5. **Handle errors gracefully** with proper try-catch blocks

## 🚨 Common Mistakes

❌ **Don't do this:**
```kotlin
// This will fail - VirtualCore not ready yet
VirtualCore.get().startup(this)
VirtualCore.get().installPackage(apkPath, 0) // NullPointerException!
```

✅ **Do this instead:**
```kotlin
// Proper initialization
VirtualCore.get().startup(this)
if (VirtualCore.get().waitForReady(10000)) {
    VirtualCore.get().installPackage(apkPath, 0) // This will work
}
```

## 📱 Test Your Fix

1. **Build and install** your app
2. **Check logcat** for "VirtualCore is ready!" message
3. **Verify** no more NullPointerException errors
4. **Test** app installation and launching

The key is **always waiting for VirtualCore to be fully initialized** before using any of its methods!
