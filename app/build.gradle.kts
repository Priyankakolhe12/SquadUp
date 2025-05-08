plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.squadup"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.squadup"
        minSdk = 23
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
}

dependencies {
    // Core AndroidX libraries
    implementation(libs.appcompat)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)

    // Firebase libraries
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation("com.google.firebase:firebase-messaging:23.4.1")
    implementation("com.google.firebase:firebase-database:20.3.0")
    // UI Libraries
    implementation(libs.material) // Use material from libs.versions.toml
    implementation("androidx.cardview:cardview:1.0.0")

    // Navigation
    implementation("androidx.navigation:navigation-fragment:2.7.3")
    implementation("androidx.navigation:navigation-ui:2.7.3")

    // Network (OkHttp)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Google sign-in
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Image loading (Glide)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // Calendar view (excluding old support libs)
    implementation("com.prolificinteractive:material-calendarview:1.4.3") {
        exclude(group = "com.android.support", module = "support-compat")
    }

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
