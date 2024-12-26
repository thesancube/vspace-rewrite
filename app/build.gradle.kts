plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dragger.hilt)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.vspace"
    compileSdk = Versions.compilesdk

    defaultConfig {
        applicationId = "com.vspace"
        minSdk = Versions.minimumSdk
        targetSdk = Versions.maximumSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        compose = true
    }
    sourceSets {
        getByName("main") {
            java {
                srcDirs("src/main/kotlin")
            }
        }
    }
}

dependencies {

    // androidx common
    implementation(libs.bundles.androidx.common)
    // jetpack compose
    implementation(libs.bundles.jetpack.compose)

    // compose bom
    implementation(platform(libs.androidx.compose.bom))
    
    // dragger hilt
    implementation(libs.dragger.hilt)
    ksp(libs.dragger.compiler)












    // ui testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}