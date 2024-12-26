plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

// this source is on the way to work so please don't use it for now


android {
    namespace = "com.vcore"
    compileSdk = Versions.compilesdk
    ndkVersion = Versions.ndkVersion
    defaultConfig {
        ndk.apply {
            abiFilters.addAll(Versions.cpuArch)
        }
        minSdk = Versions.minimumSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        aidl = true
        prefab = true
//        compose = true
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    externalNativeBuild {
    /*    cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "Versions.cmakeVersion"
        }*/
    }
}

dependencies {
    implementation(libs.bundles.androidx.common)
    implementation ("com.bytedance.android:shadowhook:1.1.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:2.1.0")



    // unit tests impl
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}