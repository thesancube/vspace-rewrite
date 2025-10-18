# How to Launch Cloned Apps with VirtualCore

## Overview
This guide explains how to install, manage, and launch cloned/virtual apps using the VirtualCore SDK.

---

## 🚀 Quick Start

### 1. **Initialize VirtualCore**
```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            VirtualCore.get().startup(this)
            Log.d("VirtualCore", "✅ Initialized successfully!")
        } catch (e: Exception) {
            Log.e("VirtualCore", "❌ Initialization failed", e)
        }
    }
}
```

### 2. **Install a Cloned App**
```kotlin
// Install an APK file
val apkPath = "/path/to/your/app.apk"
val result = VirtualCore.get().installPackage(apkPath, 0)

if (result.isSuccess) {
    Log.d("VirtualCore", "App installed: ${result.packageName}")
} else {
    Log.e("VirtualCore", "Installation failed: ${result.error}")
}
```

### 3. **Launch the Cloned App**
```kotlin
val packageName = "com.example.app"
val userId = 0 // Default user

// Check if app is installed
if (VirtualCore.get().isAppInstalled(packageName)) {
    // Get launch intent
    val launchIntent = VirtualCore.get().getLaunchIntent(packageName, userId)
    
    if (launchIntent != null) {
        // Launch the app
        startActivity(launchIntent)
    } else {
        Log.e("VirtualCore", "Cannot get launch intent for $packageName")
    }
}
```

---

## 📱 Complete Implementation Example

### **MainActivity.kt**
```kotlin
class MainActivity : AppCompatActivity() {
    
    private lateinit var virtualCore: VirtualCore
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        virtualCore = VirtualCore.get()
        
        // Install and launch app when button is clicked
        findViewById<Button>(R.id.launchAppBtn).setOnClickListener {
            installAndLaunchApp()
        }
        
        // List installed virtual apps
        findViewById<Button>(R.id.listAppsBtn).setOnClickListener {
            listInstalledApps()
        }
    }
    
    private fun installAndLaunchApp() {
        // Example: Install WhatsApp
        val apkPath = "/storage/emulated/0/Download/whatsapp.apk"
        val packageName = "com.whatsapp"
        
        // Check if already installed
        if (virtualCore.isAppInstalled(packageName)) {
            launchApp(packageName)
        } else {
            installApp(apkPath, packageName)
        }
    }
    
    private fun installApp(apkPath: String, packageName: String) {
        Thread {
            try {
                val result = virtualCore.installPackage(apkPath, 0)
                
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
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }
    
    private fun launchApp(packageName: String) {
        try {
            val userId = 0
            val launchIntent = virtualCore.getLaunchIntent(packageName, userId)
            
            if (launchIntent != null) {
                startActivity(launchIntent)
                Log.d("VirtualCore", "✅ Launched: $packageName")
            } else {
                Log.e("VirtualCore", "❌ Cannot get launch intent for $packageName")
                Toast.makeText(this, "Cannot launch $packageName", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("VirtualCore", "Launch error", e)
            Toast.makeText(this, "Launch error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun listInstalledApps() {
        Thread {
            try {
                val installedApps = virtualCore.getInstalledApps(0)
                
                runOnUiThread {
                    Log.d("VirtualCore", "📱 Installed Virtual Apps:")
                    for (app in installedApps) {
                        Log.d("VirtualCore", "- ${app.packageName} (${app.apkPath})")
                    }
                    
                    // Show in UI
                    val appList = installedApps.joinToString("\n") { 
                        "• ${it.packageName}" 
                    }
                    Toast.makeText(this, "Installed apps:\n$appList", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Log.e("VirtualCore", "Error listing apps", e)
                }
            }
        }.start()
    }
}
```

---

## 🔧 Advanced Usage

### **1. Install with Different Flags**
```kotlin
// Install with specific flags
val flags = InstallStrategy.DEPEND_SYSTEM_IF_EXIST or InstallStrategy.IGNORE_NEW_VERSION
val result = virtualCore.installPackage(apkPath, flags)
```

### **2. Install for Specific User**
```kotlin
// Install for user 1
val result = virtualCore.installPackageAsUser(1, packageName)
```

### **3. Check App Status**
```kotlin
val packageName = "com.example.app"
val userId = 0

// Check if installed
val isInstalled = virtualCore.isAppInstalled(packageName)

// Check if running
val isRunning = virtualCore.isAppRunning(packageName, userId)

// Check if launchable
val isLaunchable = virtualCore.isPackageLaunchable(packageName)

// Get app info
val appInfo = virtualCore.getInstalledAppInfo(packageName, 0)
if (appInfo != null) {
    Log.d("VirtualCore", "App: ${appInfo.packageName}")
    Log.d("VirtualCore", "APK Path: ${appInfo.apkPath}")
    Log.d("VirtualCore", "Lib Path: ${appInfo.libPath}")
    Log.d("VirtualCore", "Depend System: ${appInfo.dependSystem}")
}
```

### **4. Create App Shortcuts**
```kotlin
// Create shortcut for the app
val success = virtualCore.createShortcut(userId, packageName) { name, icon ->
    // Customize shortcut name and icon
    Pair("My $name", icon) // Return modified name and icon
}

if (success) {
    Log.d("VirtualCore", "✅ Shortcut created")
}
```

### **5. Manage App Visibility**
```kotlin
// Make app visible to other apps
virtualCore.addVisibleOutsidePackage(packageName)

// Hide app from other apps
virtualCore.removeVisibleOutsidePackage(packageName)

// Check visibility
val isVisible = virtualCore.isOutsidePackageVisible(packageName)
```

---

## 🗂️ Available Methods

### **Installation Methods**
```kotlin
// Basic installation
virtualCore.installPackage(apkPath: String, flags: Int): InstallResult

// Install for specific user
virtualCore.installPackageAsUser(userId: Int, packageName: String): InstallResult

// Install with dependency on system
virtualCore.installPackage(apkPath, InstallStrategy.DEPEND_SYSTEM_IF_EXIST)
```

### **App Management Methods**
```kotlin
// Check installation status
virtualCore.isAppInstalled(packageName: String): Boolean

// Get app information
virtualCore.getInstalledAppInfo(packageName: String, flags: Int): InstalledAppInfo?

// List all installed apps
virtualCore.getInstalledApps(flags: Int): List<InstalledAppInfo>

// List apps for specific user
virtualCore.getInstalledAppsAsUser(userId: Int, flags: Int): List<InstalledAppInfo>
```

### **Launch Methods**
```kotlin
// Get launch intent
virtualCore.getLaunchIntent(packageName: String, userId: Int): Intent?

// Check if app is launchable
virtualCore.isPackageLaunchable(packageName: String): Boolean

// Check if app is running
virtualCore.isAppRunning(packageName: String, userId: Int): Boolean
```

### **Cleanup Methods**
```kotlin
// Clear app data
virtualCore.clearPackage(packageName: String): Boolean

// Clear app data for specific user
virtualCore.clearPackageAsUser(userId: Int, packageName: String): Boolean
```

---

## 📋 Installation Flags

### **InstallStrategy Constants**
```kotlin
// Basic installation
0

// Depend on system if app is already installed
InstallStrategy.DEPEND_SYSTEM_IF_EXIST

// Ignore version checks
InstallStrategy.IGNORE_NEW_VERSION

// Skip dex optimization
InstallStrategy.SKIP_DEX_OPT

// Replace existing app
InstallStrategy.REPLACE_EXISTING
```

---

## 🚨 Error Handling

### **Common Error Scenarios**
```kotlin
private fun safeInstallApp(apkPath: String, packageName: String) {
    try {
        // Check if file exists
        if (!File(apkPath).exists()) {
            Log.e("VirtualCore", "APK file not found: $apkPath")
            return
        }
        
        // Check if already installed
        if (virtualCore.isAppInstalled(packageName)) {
            Log.d("VirtualCore", "App already installed: $packageName")
            launchApp(packageName)
            return
        }
        
        // Install the app
        val result = virtualCore.installPackage(apkPath, 0)
        
        if (result.isSuccess) {
            Log.d("VirtualCore", "✅ Installation successful")
            launchApp(packageName)
        } else {
            Log.e("VirtualCore", "❌ Installation failed: ${result.error}")
        }
        
    } catch (e: SecurityException) {
        Log.e("VirtualCore", "Permission denied", e)
    } catch (e: IOException) {
        Log.e("VirtualCore", "IO error", e)
    } catch (e: Exception) {
        Log.e("VirtualCore", "Unexpected error", e)
    }
}
```

---

## 🔍 Debugging Tips

### **Enable Verbose Logging**
```kotlin
// Add to your Application class
override fun onCreate() {
    super.onCreate()
    
    // Enable VirtualCore logging
    VLog.setLogLevel(VLog.VERBOSE)
    
    try {
        VirtualCore.get().startup(this)
    } catch (e: Exception) {
        Log.e("VirtualCore", "Startup failed", e)
    }
}
```

### **Check Installation Status**
```kotlin
private fun debugAppStatus(packageName: String) {
    Log.d("VirtualCore", "=== App Status Debug ===")
    Log.d("VirtualCore", "Package: $packageName")
    Log.d("VirtualCore", "Installed: ${virtualCore.isAppInstalled(packageName)}")
    Log.d("VirtualCore", "Launchable: ${virtualCore.isPackageLaunchable(packageName)}")
    
    val appInfo = virtualCore.getInstalledAppInfo(packageName, 0)
    if (appInfo != null) {
        Log.d("VirtualCore", "APK Path: ${appInfo.apkPath}")
        Log.d("VirtualCore", "Lib Path: ${appInfo.libPath}")
        Log.d("VirtualCore", "Depend System: ${appInfo.dependSystem}")
    }
    
    val launchIntent = virtualCore.getLaunchIntent(packageName, 0)
    Log.d("VirtualCore", "Launch Intent: ${launchIntent?.component}")
}
```

---

## 📱 UI Integration Example

### **RecyclerView for App List**
```kotlin
class VirtualAppAdapter(private val apps: List<InstalledAppInfo>) : 
    RecyclerView.Adapter<VirtualAppAdapter.ViewHolder>() {
    
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appName: TextView = view.findViewById(R.id.appName)
        val packageName: TextView = view.findViewById(R.id.packageName)
        val launchButton: Button = view.findViewById(R.id.launchButton)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_virtual_app, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val app = apps[position]
        val context = holder.itemView.context
        
        holder.appName.text = app.packageName
        holder.packageName.text = app.apkPath
        
        holder.launchButton.setOnClickListener {
            val launchIntent = VirtualCore.get().getLaunchIntent(app.packageName, 0)
            if (launchIntent != null) {
                context.startActivity(launchIntent)
            }
        }
    }
    
    override fun getItemCount() = apps.size
}
```

---

## ⚠️ Important Notes

### **Required Permissions**
Make sure your app has these permissions in `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

### **Thread Safety**
- Always run `installPackage()` on a background thread
- UI updates should be done on the main thread
- Use `runOnUiThread {}` for UI updates from background threads

### **File Access**
- Ensure APK files are accessible
- Use proper file paths (not relative paths)
- Check file permissions before installation

---

## 🎯 Complete Working Example

Here's a complete working example that you can copy and use:

```kotlin
class VirtualAppManager {
    private val virtualCore = VirtualCore.get()
    
    fun installAndLaunchApp(apkPath: String, packageName: String, context: Context) {
        Thread {
            try {
                // Check if already installed
                if (virtualCore.isAppInstalled(packageName)) {
                    launchApp(packageName, context)
                    return@Thread
                }
                
                // Install the app
                val result = virtualCore.installPackage(apkPath, 0)
                
                if (result.isSuccess) {
                    Log.d("VirtualCore", "✅ Installed: ${result.packageName}")
                    launchApp(packageName, context)
                } else {
                    Log.e("VirtualCore", "❌ Installation failed: ${result.error}")
                }
                
            } catch (e: Exception) {
                Log.e("VirtualCore", "Error", e)
            }
        }.start()
    }
    
    private fun launchApp(packageName: String, context: Context) {
        try {
            val launchIntent = virtualCore.getLaunchIntent(packageName, 0)
            if (launchIntent != null) {
                context.startActivity(launchIntent)
                Log.d("VirtualCore", "✅ Launched: $packageName")
            }
        } catch (e: Exception) {
            Log.e("VirtualCore", "Launch error", e)
        }
    }
    
    fun getInstalledApps(): List<InstalledAppInfo> {
        return try {
            virtualCore.getInstalledApps(0)
        } catch (e: Exception) {
            Log.e("VirtualCore", "Error getting apps", e)
            emptyList()
        }
    }
}
```

---

## 🎉 That's It!

You now have everything you need to install and launch cloned apps with VirtualCore! 

**Key Points:**
1. ✅ Initialize VirtualCore in your Application class
2. ✅ Install apps using `installPackage()`
3. ✅ Launch apps using `getLaunchIntent()` and `startActivity()`
4. ✅ Handle errors gracefully
5. ✅ Run installation on background threads

**Happy cloning!** 🚀
