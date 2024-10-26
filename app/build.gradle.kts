plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.notesapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.notesapp"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)             // AndroidX Core KTX
    implementation(libs.androidx.appcompat)            // AppCompat (AndroidX)
    implementation(libs.material)                      // Material Design Components
    implementation(libs.androidx.activity)             // Activity KTX
    implementation(libs.androidx.constraintlayout)     // ConstraintLayout (AndroidX)
    implementation(libs.firebase.auth)                 // Firebase Authentication
    implementation(libs.firebase.firestore)            // Firebase Firestore
    implementation("com.firebaseui:firebase-ui-firestore:8.0.0")  // Firebase Firestore UI

    testImplementation(libs.junit)                     // JUnit for testing
    androidTestImplementation(libs.androidx.junit)     // AndroidX JUnit for testing
    androidTestImplementation(libs.androidx.espresso.core)  // Espresso for testing
}
