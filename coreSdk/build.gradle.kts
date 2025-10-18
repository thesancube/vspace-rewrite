
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
}


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
        viewBinding = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    externalNativeBuild {
        ndkBuild {
            path = file("src/main/cpp/Android.mk")
        }
    }
}

dependencies {
    implementation(project(":freereflection"))
    implementation(libs.bundles.androidx.common)

//    implementation("com.bytedance.android:shadowhook:2.0.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:2.1.0")
    implementation("org.lsposed.hiddenapibypass:hiddenapibypass:6.1")



    // unit tests impl
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
