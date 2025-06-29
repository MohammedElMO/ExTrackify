plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
//    kotlin("kapt")
}

android {
    buildFeatures {
        viewBinding = true
        dataBinding = true

    }
    namespace = "com.example.extrackify"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.extrackify"
        minSdk = 30
        targetSdk = 35
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

    // Hilts core
    implementation("com.google.dagger:hilt-android:2.56.2") // Use the latest
    kapt("com.google.dagger:hilt-compiler:2.56.2")


    // Hilts ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")





    implementation("androidx.datastore:datastore-preferences:1.1.7")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("io.appwrite:sdk-for-android:8.1.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}