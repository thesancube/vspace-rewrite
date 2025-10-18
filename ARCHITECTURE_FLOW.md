# VSpace CoreSDK - Architecture Flow

## How App Cloning Works Under the Hood

---

## 📊 High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                       Your Cloner App                        │
│  (MainActivity, UI, User Interface)                          │
└──────────────────────┬──────────────────────────────────────┘
                       │ Uses VirtualCore API
                       ↓
┌─────────────────────────────────────────────────────────────┐
│                    VirtualCore SDK                           │
│  • VirtualCore.java (Main API)                              │
│  • VPackageManager (App management)                          │
│  • VActivityManager (Launch control)                         │
│  • VDeviceManager (Device spoofing)                          │
│  • VirtualLocationManager (GPS mock)                         │
└──────────────────────┬──────────────────────────────────────┘
                       │ IPC (Binder)
                       ↓
┌─────────────────────────────────────────────────────────────┐
│              Virtual Engine Process (:engine)                │
│  • VPackageManagerService                                    │
│  • VActivityManagerService                                   │
│  • 50+ System Service Hooks                                  │
└──────────────────────┬──────────────────────────────────────┘
                       │ Creates & Manages
                       ↓
┌─────────────────────────────────────────────────────────────┐
│         Virtual App Processes (:p0, :p1, :p2... :p49)       │
│  • Isolated per app instance                                 │
│  • Custom device info                                        │
│  • Redirected file system                                    │
│  • Hooked system services                                    │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔄 App Cloning Flow

### Step 1: Installation

```
User clicks "Clone WhatsApp"
        ↓
Get APK path: /data/app/com.whatsapp/base.apk
        ↓
VirtualCore.installPackage(apkPath, 0)
        ↓
┌──────────────────────────────────────┐
│ VPackageManagerService               │
│ 1. Parse APK                         │
│ 2. Extract manifest                  │
│ 3. Copy to virtual storage           │
│ 4. Create package metadata           │
│ 5. Store in internal DB              │
└──────────────────────────────────────┘
        ↓
VirtualCore.installPackageAsUser(0, "com.whatsapp")
        ↓
Install for User 0 (first instance)
        ↓
✅ Clone Created!
```

**Files Created:**
```
/data/data/<your.app>/virtual/
  └── user_0/
      └── com.whatsapp/
          ├── data/      (app data)
          ├── cache/     (cache)
          ├── lib/       (native libs)
          └── base.apk   (APK copy)
```

---

## 🚀 App Launch Flow

```
User clicks "Launch Clone"
        ↓
VirtualCore.launchApp("com.whatsapp", 0)
        ↓
┌──────────────────────────────────────┐
│ VActivityManagerService              │
│ 1. Find available stub process       │
│ 2. Allocate process :p0              │
│ 3. Bind app to process               │
│ 4. Inject hooks                      │
└──────────────────────────────────────┘
        ↓
┌──────────────────────────────────────┐
│ Process :p0 (Virtual App Process)    │
│ 1. VClientImpl.bindApplication()     │
│ 2. Load app's APK                    │
│ 3. Create Application class          │
│ 4. Hook system services              │
│ 5. Redirect I/O operations           │
│ 6. Apply device customization        │
└──────────────────────────────────────┘
        ↓
Load WhatsApp's MainActivity
        ↓
┌──────────────────────────────────────┐
│ WhatsApp Runs in Virtual Environment │
│ • Isolated from real WhatsApp        │
│ • Custom device info                 │
│ • Redirected storage                 │
│ • All system calls hooked            │
└──────────────────────────────────────┘
        ↓
✅ Clone Running!
```

---

## 🔐 System Service Hooking Flow

When cloned app calls Android APIs:

```
Cloned WhatsApp calls:
Context.getSystemService("phone")
        ↓
┌──────────────────────────────────────┐
│ VirtualCore Hook Layer               │
│ TelephonyStub intercepts call        │
└──────────────────────────────────────┘
        ↓
getDeviceId() called
        ↓
┌──────────────────────────────────────┐
│ VDeviceManager                       │
│ Returns CUSTOM IMEI                  │
│ (not real device IMEI)               │
└──────────────────────────────────────┘
        ↓
WhatsApp gets: "352441087654321"
Real device has: "123456789012345"
        ↓
✅ Device Spoofing Works!
```

**Hooked Services:**
- ✅ ActivityManager (app lifecycle)
- ✅ PackageManager (app queries)
- ✅ TelephonyManager (phone info)
- ✅ LocationManager (GPS)
- ✅ WifiManager (WiFi info)
- ✅ 45+ more...

---

## 💾 File System Redirection

When cloned app accesses files:

```
WhatsApp tries to write:
/data/data/com.whatsapp/files/profile.jpg
        ↓
┌──────────────────────────────────────┐
│ Native IOUniformer (C++ layer)       │
│ Intercepts open() syscall            │
└──────────────────────────────────────┘
        ↓
Path translated to:
/data/data/<your.app>/virtual/user_0/com.whatsapp/files/profile.jpg
        ↓
File written to isolated location
        ↓
✅ Storage Isolated!
```

**Redirected Paths:**
```
Original Path              →  Virtual Path
─────────────────────────────────────────────────────────
/data/data/com.whatsapp/   →  /.../virtual/user_0/com.whatsapp/
/sdcard/WhatsApp/          →  /.../virtual/user_0/vsdcard/WhatsApp/
/data/user/0/com.whatsapp/ →  /.../virtual/user_0/com.whatsapp/
```

---

## 📱 Multi-Instance Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      Your Cloner App                         │
└────┬──────────────┬─────────────────┬─────────────────┬─────┘
     │              │                 │                 │
     ↓              ↓                 ↓                 ↓
┌─────────┐   ┌─────────┐      ┌─────────┐      ┌─────────┐
│Process  │   │Process  │      │Process  │      │Process  │
│  :p0    │   │  :p1    │  ... │  :p2    │  ... │  :p49   │
│         │   │         │      │         │      │         │
│WhatsApp │   │WhatsApp │      │Instagram│      │Chrome   │
│Instance1│   │Instance2│      │Instance1│      │Instance1│
│         │   │         │      │         │      │         │
│User 0   │   │User 1   │      │User 0   │      │User 0   │
│IMEI:    │   │IMEI:    │      │IMEI:    │      │IMEI:    │
│352441...│   │869123...│      │742985...│      │963741...│
└─────────┘   └─────────┘      └─────────┘      └─────────┘

Each process:
• Independent Linux process
• Separate UID
• Isolated memory
• Custom device info
• Redirected I/O
```

---

## 🎯 Device Customization Flow

```
You call: customizeDeviceInfo(userId = 0)
        ↓
Create VDeviceInfo:
  - IMEI: 352441087654321
  - Android ID: 8f2a7b4c9d3e1f5a
  - Serial: VS7829
  - Brand: Samsung
  - Model: SM-G998B
        ↓
VDeviceManager.updateDeviceInfo(0, deviceInfo)
        ↓
Stored in database per user
        ↓
When app launches in user 0:
        ↓
All device queries return custom values
        ↓
┌──────────────────────────────────────┐
│ App calls:                           │
│ TelephonyManager.getDeviceId()       │
│ Build.MODEL                          │
│ Settings.Secure.ANDROID_ID           │
└──────────────────────────────────────┘
        ↓
Returns custom values, not real device
        ↓
✅ Each instance has unique identity!
```

---

## 📍 Location Spoofing Flow

```
You call: mockLocation(40.7128, -74.0060, userId = 0)
        ↓
VirtualLocationManager.setLocation(0, location)
        ↓
Location stored for user 0
        ↓
When cloned app requests location:
        ↓
┌──────────────────────────────────────┐
│ LocationManager.getLastKnownLocation()│
└──────────────────────────────────────┘
        ↓
LocationManagerStub intercepts
        ↓
Returns mock location (New York)
        ↓
Real device location: Tokyo
App sees location: New York
        ↓
✅ Location Spoofed!
```

---

## 🔍 Process Lifecycle

### Startup Sequence

```
1. Your App Starts
   └─> VirtualCore.startup(context)
       └─> Initialize engine process
           └─> Start service managers
               └─> Load system hooks
                   └─> ✅ Ready

2. User Clones App
   └─> installPackage()
       └─> Parse APK
           └─> Copy to virtual storage
               └─> ✅ Installed

3. User Launches Clone
   └─> launchApp()
       └─> Allocate stub process
           └─> Bind application
               └─> Inject hooks
                   └─> Launch activity
                       └─> ✅ Running
```

### Runtime Architecture

```
Main Process (Your App)
    ├── UI Thread
    ├── Background Threads
    └── VirtualCore API

:engine Process (Background)
    ├── VPackageManagerService
    ├── VActivityManagerService
    ├── VDeviceManagerService
    └── 10+ other services

:p0 Process (WhatsApp Clone 1)
    ├── VClientImpl
    ├── Hooked Services
    ├── Native I/O Redirector
    └── WhatsApp Application

:p1 Process (WhatsApp Clone 2)
    ├── VClientImpl
    ├── Hooked Services
    ├── Native I/O Redirector
    └── WhatsApp Application

... up to :p49
```

---

## 📊 API Call Flow Example

### Cloning WhatsApp:

```java
// Your code:
virtualCore.installPackage("/path/to/whatsapp.apk", 0);

// Internal flow:
VirtualCore.installPackage()
    → IAppManager.installPackage() [IPC]
        → VAppManagerService.installPackage() [Engine Process]
            → PackageParserEx.parsePackage()
            → Copy APK to virtual storage
            → Extract native libs
            → Create PackageSetting
            → Save to database
            → Broadcast PACKAGE_ADDED
    ← Return InstallResult

// Then:
virtualCore.installPackageAsUser(0, "com.whatsapp");
    → Install for user 0
    → Create user-specific data directories
    ← Return true

// Launch:
virtualCore.launchApp("com.whatsapp", 0);
    → IActivityManager.startActivity() [IPC]
        → VActivityManagerService.startActivity() [Engine]
            → Find available stub process (:p0)
            → Bind app to process
            → VClientImpl.bindApplication() [Virtual Process]
                → Load APK
                → Create Application class
                → Install content providers
                → Call Application.onCreate()
            → Start MainActivity
    ← Activity launched
```

---

## 🎯 Summary

VSpace CoreSDK creates a **complete virtual Android environment** by:

1. ✅ **Process Isolation** - Each clone runs in separate process
2. ✅ **System Hook Injection** - Intercepts all Android API calls
3. ✅ **File System Redirection** - Redirects storage to isolated paths
4. ✅ **Device Virtualization** - Fakes device info per instance
5. ✅ **IPC Management** - Coordinates between processes

**Result:** Apps run completely isolated, thinking they're on different devices! 🎉

---

## 📚 Key Components Summary

| Component | Purpose | Location |
|-----------|---------|----------|
| **VirtualCore** | Main API entry point | Client side |
| **VClientImpl** | Per-process manager | Virtual process |
| **VActivityManager** | Launch control | IPC + Service |
| **VPackageManager** | App management | IPC + Service |
| **VDeviceManager** | Device spoofing | IPC + Service |
| **IOUniformer** | File redirection | Native (C++) |
| **MethodInvocationStub** | Hook framework | Client side |
| **Stub Components** | Process placeholders | Manifest |

---

Now you understand how it all works! 🚀

