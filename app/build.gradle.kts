plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.warehouse"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.warehouse"
        minSdk = 24
        targetSdk = 33
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

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-firestore-ktx:24.7.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth-ktx:22.1.1")
    // Firebase Firestore
    implementation("com.google.firebase:firebase-storage-ktx:20.2.1")

    // Firebase ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.4.0")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.4.0")
    implementation ("androidx.lifecycle:lifecycle-common-java8:2.4.0")

    // Firebase UI Authentication (opcional, se desejar UIs prontas para autenticação)
    implementation ("com.firebaseui:firebase-ui-auth:8.0.0")

    implementation ("androidx.recyclerview:recyclerview:1.2.1") // ou versão mais recente
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0") // ou versão mais recente

    implementation ("com.airbnb.android:lottie:3.4.0")

}