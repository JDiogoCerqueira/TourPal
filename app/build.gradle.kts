plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.tourpal"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tourpal"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["GOOGLE_MAPS_API_KEY"] =
            project.properties["GOOGLE_MAPS_API_KEY"]?.toString() ?: ""
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true // Enable Compose explicitly (replacing viewBinding)
        viewBinding = true // Optional: Keep only if you're mixing View-based UI with Compose
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14" // Match Kotlin version
    }
}

dependencies {
    // Core AndroidX and Kotlin
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx.v190)

    // Jetpack Compose
    implementation(libs.androidx.activity.compose) // For Compose Activity
    implementation(libs.androidx.ui) // Compose UI
    implementation(libs.androidx.ui.tooling.preview) // Preview support
    implementation(libs.androidx.material3.android) // Material3 components
    implementation(libs.androidx.navigation.compose) // Compose navigation

    // Traditional View-based dependencies (optional, only if needed)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Firebase BOM (Bill of Materials), analytics and authentication
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)

    // Google SignIn
    implementation(libs.play.services.auth)

    // Google Maps Compose
    implementation(libs.maps.compose)

    // Google Maps SDK
    implementation(libs.play.services.maps)

    // Credentials support
    implementation(libs.androidx.credentials)
    // optional - needed for credentials support from play services, for devices running
    // Android 13 and below.
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid.v110)

    // GSON
    implementation(libs.gson)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.ui.tooling)

    // Debugging tools (optional)
}

