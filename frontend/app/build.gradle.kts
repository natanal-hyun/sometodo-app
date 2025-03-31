plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.todolist"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.todolist"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val baseUrl: String = project.findProperty("BASE_URL") as String? ?: "http://13.124.253.27:8080/"
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")

        val googleClientId: String = project.findProperty("GOOGLE_CLIENT_ID") as String? ?: "YOUR_DEFAULT_CLIENT_ID"
        buildConfigField("String", "GOOGLE_CLIENT_ID", "\"$googleClientId\"")
        resValue("string", "google_client_id", googleClientId)

        val googleWebClientId: String = project.findProperty("GOOGLE_WEB_CLIENT_ID") as String? ?: "YOUR_DEFAULT_WEB_CLIENT_ID"
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$googleWebClientId\"")
    }

    buildFeatures {
        buildConfig = true
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    implementation(libs.splashscreen)

    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.okhttp)
    implementation(libs.scalars)

    implementation(libs.glide)

    implementation(libs.google.auth)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}