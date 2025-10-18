# 🚀 VSpace Quick Start - 5-Minute App Cloner

## The Simplest Possible Implementation

Here's a **minimal working example** to get you started in 5 minutes:

---

## Step 1: Create Application Class

**File: `app/src/main/kotlin/com/yourapp/MyApp.kt`**

```kotlin
package com.yourapp

import android.app.Application
import com.vcore.client.core.VirtualCore

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        VirtualCore.get().startup(this)
    }
}
```

Update **AndroidManifest.xml**:
```xml
<application
    android:name=".MyApp"
    ... >
```

---

## Step 2: Create Main Activity

**File: `app/src/main/kotlin/com/yourapp/MainActivity.kt`**

```kotlin
package com.yourapp

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vcore.client.core.VirtualCore
import com.vcore.client.ipc.VDeviceManager
import com.vcore.remote.VDeviceInfo

class MainActivity : AppCompatActivity() {
    
    private val virtualCore by lazy { VirtualCore.get() }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Wait for VirtualCore engine to start
        virtualCore.waitForEngine()
        
        setupButtons()
    }
    
    private fun setupButtons() {
        // Clone WhatsApp button
        findViewById<Button>(R.id.btnCloneWhatsApp).setOnClickListener {
            cloneAndLaunchApp("com.whatsapp")
        }
        
        // Clone Instagram button
        findViewById<Button>(R.id.btnCloneInstagram).setOnClickListener {
            cloneAndLaunchApp("com.instagram.android")
        }
        
        // Clone any app by package name
        findViewById<Button>(R.id.btnCloneCustom).setOnClickListener {
            cloneAndLaunchApp("com.android.chrome") // Change this
        }
    }
    
    private fun cloneAndLaunchApp(packageName: String) {
        Toast.makeText(this, "Cloning $packageName...", Toast.LENGTH_SHORT).show()
        
        Thread {
            try {
                // Step 1: Get APK path from installed app
                val appInfo = packageManager.getApplicationInfo(packageName, 0)
                val apkPath = appInfo.sourceDir
                
                // Step 2: Install in virtual environment
                val result = virtualCore.installPackage(apkPath, 0)
                
                if (result.isSuccess) {
                    // Step 3: Install for user 0
                    virtualCore.installPackageAsUser(0, result.packageName)
                    
                    // Step 4: Customize device (optional but recommended)
                    customizeDevice(0)
                    
                    // Step 5: Launch the clone
                    runOnUiThread {
                        virtualCore.launchApp(result.packageName, 0)
                        Toast.makeText(this, "✅ Clone launched!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "❌ Clone failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "❌ Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
    
    private fun customizeDevice(userId: Int) {
        val deviceManager = VDeviceManager.get()
        val deviceInfo = VDeviceInfo().apply {
            deviceId = generateRandomIMEI()
            androidId = generateRandomAndroidId()
            serial = "VSpace" + (1000..9999).random()
        }
        deviceManager.updateDeviceInfo(userId, deviceInfo)
    }
    
    private fun generateRandomIMEI(): String {
        return (100000000000000L..999999999999999L).random().toString()
    }
    
    private fun generateRandomAndroidId(): String {
        return java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 16)
    }
}
```

---

## Step 3: Create Layout

**File: `app/src/main/res/layout/activity_main.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp"
    android:gravity="center">
    
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="VSpace App Cloner"
        android:textSize="24sp"
        android:textStyle="bold"
        android:layout_marginBottom="32dp"/>
    
    <Button
        android:id="@+id/btnCloneWhatsApp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Clone WhatsApp"
        android:textSize="18sp"
        android:layout_marginBottom="16dp"/>
    
    <Button
        android:id="@+id/btnCloneInstagram"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Clone Instagram"
        android:textSize="18sp"
        android:layout_marginBottom="16dp"/>
    
    <Button
        android:id="@+id/btnCloneCustom"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Clone Chrome"
        android:textSize="18sp"/>
    
</LinearLayout>
```

---

## Step 4: Update Permissions

**File: `app/src/main/AndroidManifest.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    
    <application
        android:name=".MyApp"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="VSpace Cloner"
        android:theme="@style/Theme.AppCompat.Light">
        
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        
    </application>
</manifest>
```

---

## That's It! 🎉

**3 files, ~100 lines of code**, and you have a working app cloner!

### What This Does:

1. ✅ **Initializes VirtualCore** when app starts
2. ✅ **Clones any installed app** with one button click
3. ✅ **Customizes device info** (IMEI, Android ID) for each clone
4. ✅ **Launches the cloned app** automatically
5. ✅ **Isolates data** - clone and original are separate

### Test It:

1. Build and install your app
2. Click "Clone WhatsApp" (make sure WhatsApp is installed)
3. Wait 2-3 seconds
4. The WhatsApp clone launches! 🎉

---

## Advanced: Add App List

Want to show installed virtual apps? Add this to MainActivity:

```kotlin
import androidx.recyclerview.widget.RecyclerView
import com.vcore.remote.InstalledAppInfo

private fun showInstalledApps() {
    val apps = virtualCore.getInstalledApps(0)
    apps.forEach { app ->
        val label = app.appInfo.loadLabel(packageManager)
        val icon = app.appInfo.loadIcon(packageManager)
        println("📱 $label - ${app.packageName}")
        // Display in RecyclerView
    }
}
```

---

## Advanced: Multiple Instances

Want to run 3 WhatsApp accounts?

```kotlin
fun createMultipleWhatsApps() {
    val packageName = "com.whatsapp"
    
    // Clone once
    val apkPath = packageManager.getApplicationInfo(packageName, 0).sourceDir
    val result = virtualCore.installPackage(apkPath, 0)
    
    if (result.isSuccess) {
        // Install for 3 users (3 instances)
        for (userId in 0..2) {
            virtualCore.installPackageAsUser(userId, result.packageName)
            customizeDevice(userId) // Different device info per instance
        }
        
        Toast.makeText(this, "✅ 3 WhatsApp instances created!", Toast.LENGTH_SHORT).show()
    }
}

// Launch instance 0, 1, or 2
fun launchWhatsAppInstance(instanceNumber: Int) {
    virtualCore.launchApp("com.whatsapp", instanceNumber)
}
```

---

## Troubleshooting

### App doesn't launch?
```kotlin
// Check if installed
if (!virtualCore.isAppInstalled(packageName)) {
    println("❌ Not installed")
}

// Check if installed for user
if (!virtualCore.isAppInstalledAsUser(0, packageName)) {
    virtualCore.installPackageAsUser(0, packageName)
}
```

### Need to see logs?
```kotlin
import com.vcore.helper.utils.VLog

VLog.d("MyApp", "Debug message")
VLog.e("MyApp", "Error message")
```

### Want to uninstall a clone?
```kotlin
virtualCore.uninstallPackage(packageName)
// or for specific user:
virtualCore.uninstallPackageAsUser(packageName, userId)
```

---

## 🎯 Summary

```kotlin
// 1. Initialize (in Application class)
VirtualCore.get().startup(this)

// 2. Clone app
val result = virtualCore.installPackage(apkPath, 0)
virtualCore.installPackageAsUser(0, result.packageName)

// 3. Launch clone
virtualCore.launchApp(packageName, 0)
```

**That's literally it!** You now have a functional app cloner. 🚀

---

## Next Steps

- Read **INTEGRATION_GUIDE.md** for advanced features
- Add location spoofing
- Add device customization UI
- Add app management (uninstall, clear data)
- Add multi-instance support with tabs

**Happy Cloning!** 🎉

