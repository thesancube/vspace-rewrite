

## 2. Basic Integration

### Step 1: Create Application Class

**MyApplication.kt**:
```kotlin
package com.yourapp.cloner

import android.app.Application
import com.vcore.client.core.VirtualCore

class MyApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize VirtualCore
        try {
            VirtualCore.get().startup(this)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}
```

### Step 2: Initialize in MainActivity

**MainActivity.kt**:
```kotlin
package com.yourapp.cloner

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vcore.client.core.VirtualCore

class MainActivity : AppCompatActivity() {
    
    private lateinit var virtualCore: VirtualCore
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Get VirtualCore instance
        virtualCore = VirtualCore.get()
        
        // Wait for engine to be ready
        virtualCore.waitForEngine()
        
        Toast.makeText(this, "VirtualCore Ready!", Toast.LENGTH_SHORT).show()
    }
}
```

---

## 3. Installing Apps

### Method 1: Install from APK File

```kotlin
import com.vcore.remote.InstallResult
import java.io.File

fun installApp(apkPath: String) {
    val file = File(apkPath)
    if (!file.exists()) {
        println("APK file not found: $apkPath")
        return
    }
    
    // Install the app into virtual environment
    val result: InstallResult = virtualCore.installPackage(apkPath, 0)
    
    when {
        result.isSuccess -> {
            println("✅ App installed successfully: ${result.packageName}")
            
            // Auto-install for user 0
            val installed = virtualCore.installPackageAsUser(0, result.packageName)
            if (installed) {
                println("✅ App installed for user 0")
            }
        }
        else -> {
            println("❌ Installation failed: ${result.error}")
        }
    }
}
```

### Method 2: Install from Installed Apps

```kotlin
fun cloneInstalledApp(packageName: String) {
    try {
        val pm = packageManager
        val appInfo = pm.getApplicationInfo(packageName, 0)
        val apkPath = appInfo.sourceDir
        
        // Install the app
        val result = virtualCore.installPackage(apkPath, 0)
        
        if (result.isSuccess) {
            virtualCore.installPackageAsUser(0, result.packageName)
            println("✅ Cloned app: $packageName")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
```

### Check if App is Installed

```kotlin
fun isAppInstalled(packageName: String): Boolean {
    return virtualCore.isAppInstalled(packageName)
}
```

### Get All Installed Virtual Apps

```kotlin
import com.vcore.remote.InstalledAppInfo

fun getAllVirtualApps(): List<InstalledAppInfo> {
    return virtualCore.getInstalledApps(0)
}

// Usage
fun displayInstalledApps() {
    val apps = getAllVirtualApps()
    apps.forEach { app ->
        println("📱 ${app.packageName} - ${app.appInfo.loadLabel(packageManager)}")
    }
}
```

---

## 4. Launching Apps

### Method 1: Launch App Directly

```kotlin
import android.content.Intent

fun launchApp(packageName: String, userId: Int = 0) {
    try {
        // Get launch intent for the app
        val intent = virtualCore.getLaunchIntent(packageName, userId)
        
        if (intent != null) {
            // Add flags to start in virtual environment
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            
            // Launch the app
            virtualCore.launchApp(packageName, userId)
            
            println("🚀 Launching $packageName")
        } else {
            println("❌ No launch intent found for $packageName")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
```

### Method 2: Launch with Custom Intent

```kotlin
fun launchAppWithIntent(packageName: String, userId: Int = 0) {
    val intent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
        setPackage(packageName)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    
    virtualCore.startActivity(intent, userId)
}
```

### Check if App is Running

```kotlin
fun isAppRunning(packageName: String, userId: Int = 0): Boolean {
    return virtualCore.isAppRunning(packageName, userId)
}
```

### Kill Running App

```kotlin
fun killApp(packageName: String, userId: Int = 0) {
    virtualCore.killApp(packageName, userId)
    println("🛑 Killed $packageName")
}
```

---

## 5. Device Customization

### Customize Device Info (IMEI, Android ID, etc.)

```kotlin
import com.vcore.remote.VDeviceInfo
import com.vcore.client.ipc.VDeviceManager

fun customizeDeviceInfo(userId: Int = 0) {
    val deviceManager = VDeviceManager.get()
    
    // Get current device info
    var deviceInfo = deviceManager.getDeviceInfo(userId)
    
    if (deviceInfo == null) {
        // Create new device info if not exists
        deviceInfo = VDeviceInfo().apply {
            deviceId = generateRandomIMEI()
            androidId = generateRandomAndroidId()
            serial = generateRandomSerial()
            
            // Build info
            brand = "Samsung"
            model = "SM-G998B"
            manufacturer = "Samsung"
            
            // WiFi MAC
            wifiMac = generateRandomMAC()
            bluetoothMac = generateRandomMAC()
        }
        
        deviceManager.updateDeviceInfo(userId, deviceInfo)
        println("✅ Device info customized for user $userId")
    }
}

// Helper functions
fun generateRandomIMEI(): String {
    val random = (100000000000000L..999999999999999L).random()
    return random.toString()
}

fun generateRandomAndroidId(): String {
    return java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 16)
}

fun generateRandomSerial(): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return (1..10).map { chars.random() }.joinToString("")
}

fun generateRandomMAC(): String {
    val mac = (1..6).map { 
        "%02X".format((0..255).random()) 
    }.joinToString(":")
    return mac
}
```

---

## 6. Location Spoofing

### Mock GPS Location

```kotlin
import com.vcore.client.ipc.VirtualLocationManager
import android.location.Location

fun mockLocation(latitude: Double, longitude: Double, userId: Int = 0) {
    val locationManager = VirtualLocationManager.get()
    
    // Create mock location
    val location = Location("gps").apply {
        this.latitude = latitude
        this.longitude = longitude
        accuracy = 10f
        time = System.currentTimeMillis()
    }
    
    // Set mock location for user
    locationManager.setLocation(userId, location)
    
    println("📍 Location set to: $latitude, $longitude")
}

// Example: Mock location to New York
fun mockLocationToNewYork(userId: Int = 0) {
    mockLocation(40.7128, -74.0060, userId)
}

// Example: Mock location to Tokyo
fun mockLocationToTokyo(userId: Int = 0) {
    mockLocation(35.6762, 139.6503, userId)
}

// Disable location mocking
fun disableLocationMock(userId: Int = 0) {
    VirtualLocationManager.get().setLocation(userId, null)
    println("📍 Location mocking disabled")
}
```

---

## 7. Multi-User Support

### Create Multiple Virtual Users

```kotlin
fun createMultipleInstances(packageName: String, count: Int) {
    // Install app if not already installed
    if (!virtualCore.isAppInstalled(packageName)) {
        println("❌ App not installed: $packageName")
        return
    }
    
    // Install for multiple users
    for (userId in 0 until count) {
        val installed = virtualCore.installPackageAsUser(userId, packageName)
        if (installed) {
            println("✅ Installed $packageName for user $userId")
            
            // Customize device for each user
            customizeDeviceInfo(userId)
        }
    }
}

// Launch specific instance
fun launchInstance(packageName: String, instanceNumber: Int) {
    launchApp(packageName, instanceNumber)
}
```

---

## 8. Advanced Features

### Get App Icon

```kotlin
import android.graphics.drawable.Drawable

fun getAppIcon(packageName: String): Drawable? {
    return try {
        val info = virtualCore.getInstalledAppInfo(packageName, 0)
        info?.appInfo?.loadIcon(packageManager)
    } catch (e: Exception) {
        null
    }
}
```

### Get App Label

```kotlin
fun getAppLabel(packageName: String): String {
    return try {
        val info = virtualCore.getInstalledAppInfo(packageName, 0)
        info?.appInfo?.loadLabel(packageManager)?.toString() ?: packageName
    } catch (e: Exception) {
        packageName
    }
}
```

### Uninstall App

```kotlin
fun uninstallApp(packageName: String) {
    val success = virtualCore.uninstallPackage(packageName)
    if (success) {
        println("✅ Uninstalled $packageName")
    } else {
        println("❌ Failed to uninstall $packageName")
    }
}

// Uninstall for specific user
fun uninstallAppForUser(packageName: String, userId: Int) {
    val success = virtualCore.uninstallPackageAsUser(packageName, userId)
    if (success) {
        println("✅ Uninstalled $packageName for user $userId")
    }
}
```

### Clear App Data

```kotlin
fun clearAppData(packageName: String, userId: Int = 0) {
    val success = virtualCore.clearPackageAsUser(userId, packageName)
    if (success) {
        println("🗑️ Cleared data for $packageName")
    }
}
```

---

## 9. Complete Example Activity

**AppClonerActivity.kt**:

```kotlin
package com.yourapp.cloner

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vcore.client.core.VirtualCore

class AppClonerActivity : AppCompatActivity() {
    
    private lateinit var virtualCore: VirtualCore
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloner)
        
        virtualCore = VirtualCore.get()
        
        setupButtons()
    }
    
    private fun setupButtons() {
        findViewById<Button>(R.id.btnCloneWhatsApp).setOnClickListener {
            cloneApp("com.whatsapp")
        }
        
        findViewById<Button>(R.id.btnLaunchClone).setOnClickListener {
            launchClone("com.whatsapp", 0)
        }
        
        findViewById<Button>(R.id.btnCustomizeDevice).setOnClickListener {
            customizeDevice(0)
        }
        
        findViewById<Button>(R.id.btnMockLocation).setOnClickListener {
            setMockLocation(0)
        }
    }
    
    private fun cloneApp(packageName: String) {
        Thread {
            try {
                // Get APK path from installed app
                val appInfo = packageManager.getApplicationInfo(packageName, 0)
                val apkPath = appInfo.sourceDir
                
                // Install in virtual environment
                val result = virtualCore.installPackage(apkPath, 0)
                
                runOnUiThread {
                    if (result.isSuccess) {
                        virtualCore.installPackageAsUser(0, result.packageName)
                        Toast.makeText(this, "✅ App cloned successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "❌ Clone failed: ${result.error}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "❌ Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
    
    private fun launchClone(packageName: String, userId: Int) {
        try {
            virtualCore.launchApp(packageName, userId)
            Toast.makeText(this, "🚀 Launching clone...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "❌ Launch failed", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun customizeDevice(userId: Int) {
        // Use the customizeDeviceInfo function from section 5
        customizeDeviceInfo(userId)
        Toast.makeText(this, "✅ Device customized", Toast.LENGTH_SHORT).show()
    }
    
    private fun setMockLocation(userId: Int) {
        // Mock location to New York
        mockLocation(40.7128, -74.0060, userId)
        Toast.makeText(this, "📍 Location set to New York", Toast.LENGTH_SHORT).show()
    }
}
```

---

## 10. ProGuard Rules

Add to **proguard-rules.pro**:

```proguard
# VirtualCore
-keep class com.vcore.** { *; }
-keep class mirror.** { *; }

# Reflection
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses

# AIDL
-keep class * implements android.os.IInterface { *; }

# Native methods
-keepclasseswithmembernames class * {
    native <methods>;
}
```

---

## 11. Testing Your App Cloner

### Test Checklist

```kotlin
fun runTests() {
    // 1. Install test
    println("Test 1: Installing app...")
    cloneInstalledApp("com.android.chrome")
    
    // 2. Launch test
    println("Test 2: Launching app...")
    Thread.sleep(2000)
    launchApp("com.android.chrome", 0)
    
    // 3. Device info test
    println("Test 3: Customizing device...")
    customizeDeviceInfo(0)
    
    // 4. Location test
    println("Test 4: Mocking location...")
    mockLocationToNewYork(0)
    
    // 5. Multi-user test
    println("Test 5: Creating multiple instances...")
    createMultipleInstances("com.android.chrome", 3)
    
    println("✅ All tests completed!")
}
```

---

## 12. Common Issues & Solutions

### Issue 1: App Won't Launch
**Solution:** Check if app is installed for the user
```kotlin
if (!virtualCore.isAppInstalledAsUser(userId, packageName)) {
    virtualCore.installPackageAsUser(userId, packageName)
}
```

