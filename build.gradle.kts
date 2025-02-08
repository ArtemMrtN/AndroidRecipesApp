// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("androidx.navigation.safeargs.kotlin") version "2.8.6" apply false
    kotlin("plugin.serialization") version "2.1.0"
    id("com.google.devtools.ksp") version "2.0.0-1.0.23" apply false
}