import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.example.clipartapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.clipartapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Read from local.properties
        val localProperties = Properties()
        val localFile = rootProject.file("local.properties")
        if (localFile.exists()) {
            localProperties.load(localFile.inputStream())
        }

        val imgbbKey = localProperties.getProperty("IMGBB_API_KEY") ?: ""
        val replicateKey = localProperties.getProperty("REPLICATE_API_KEY") ?: ""
        val huggingfaceKey = localProperties.getProperty("HUGGINGFACE_API_KEY") ?: ""

        println("=========================================")
        println("IMGBB Key loaded: ${if(imgbbKey.isNotEmpty()) "YES (length: ${imgbbKey.length})" else "NO"}")
        println("Replicate Key loaded: ${if(replicateKey.isNotEmpty()) "YES (length: ${replicateKey.length})" else "NO"}")
        println("HuggingFace Key loaded: ${if(huggingfaceKey.isNotEmpty()) "YES (length: ${huggingfaceKey.length})" else "NO"}")
        println("=========================================")

        buildConfigField("String", "IMGBB_API_KEY", "\"$imgbbKey\"")
        buildConfigField("String", "REPLICATE_API_KEY", "\"$replicateKey\"")
        buildConfigField("String", "HUGGINGFACE_API_KEY", "\"$huggingfaceKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Compose BOM
    val composeBom = platform("androidx.compose:compose-bom:2024.10.00")
    implementation(composeBom)

    // Compose dependencies
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Core Android dependencies
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Image loading
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // JSON parsing
    implementation("org.json:json:20240303")

    // Splash screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Debug only
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}