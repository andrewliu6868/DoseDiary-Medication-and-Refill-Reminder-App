// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

buildscript {
    val compose_version by extra("1.4.0") // Update to the latest version
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.0") // Ensure this is the latest version
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21") // Ensure this is the latest version
    }
}
