import java.util.Properties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.apolloGraphQL) apply false
    alias(libs.plugins.safeArgs) apply false
}

val properties = Properties()
file("local.properties").inputStream().use { properties.load(it) }