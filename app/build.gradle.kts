plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "gr.makrygenis.anastasia"
    compileSdk = 35

    defaultConfig {
        applicationId = "gr.makrygenis.anastasia"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    // Statheri ypografi, oste kathe nea ekdosi na mpainei pano stin proigoumeni
    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("anastasia.keystore")
            storePassword = "anastasia"
            keyAlias = "anastasia"
            keyPassword = "anastasia"
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
}
