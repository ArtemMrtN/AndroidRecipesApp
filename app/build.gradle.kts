plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    id("androidx.navigation.safeargs.kotlin")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
}

android {
    namespace = "com.mrt.androidrecipesapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mrt.androidrecipesapp"
        minSdk = 28
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.glide)
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}