import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.apolloGraphQL)
    alias(libs.plugins.safeArgs)
}
fun getLocalProperty(key: String): String {
    val localProperties = Properties()
    localProperties.load(FileInputStream(rootProject.file("local.properties")))
    return localProperties.getProperty(key)
}

apollo {

    service("service") {
        packageName.set("dev.borisochieng")
        outputDirConnection {
            connectToKotlinSourceSet("main")
        }
        introspection {
            endpointUrl.set("https://api.github.com/graphql")
            headers.put("Authorization", "bearer ${getLocalProperty("client_secret")}")
            schemaFile.set(file("src/main/graphql/schema.graphqls"))
        }
    }
}

android {
    namespace = "dev.borisochieng.gitrack"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.borisochieng.gitrack"
        minSdk = 26
        targetSdk = 34
        versionCode = 3
        versionName = "1.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "CLIENT_ID",
            "\"${getLocalProperty("client_id")}\""
        )
        buildConfigField(
            "String",
            "CLIENT_SECRET",
            "\"${getLocalProperty("client_secret")}\""
        )
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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //splash screen
    implementation(libs.androidx.core.splashscreen)
    //apollo
    implementation(libs.apollo.runtime)
    //retrofit
    implementation(libs.retrofit)
    implementation(libs.gson)
    //okhttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
}