plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
    id("dagger.hilt.android.plugin")
//    id("kotlin-kapt")
//    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.hi.zoo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.hi.zoo"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.3" // 確保這是最新版本
//    }
}

dependencies {

    // Compose 和常規的 Android 依賴
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // 測試依賴
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.hilt.core)
    implementation(libs.hilt.android)
    implementation(libs.hilt.ext.work)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.ext.compiler)
    implementation(libs.androidx.work.runtime.ktx)

    // Hilt 依賴
    implementation("com.google.dagger:hilt-android:2.50")
    ksp("com.google.dagger:hilt-android-compiler:2.50")

    // Jetpack Compose Navigation
    implementation ("androidx.navigation:navigation-compose:2.5.0") // 或最新版本

    // Hilt for Jetpack Compose
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Room 資料庫
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    //添加 Gson
    implementation("com.google.code.gson:gson:2.10.1")

    //Parcelize
    implementation("androidx.core:core-ktx:1.12.0")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Retrofit 核心庫
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Gson 解析器

    //okhttp3
    implementation("com.squareup.okhttp3:okhttp:4.11.0") // OkHttp 核心庫
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0") // 日誌攔截器（可選）
    implementation("com.squareup.okhttp3:okhttp-dnsoverhttps:4.11.0") // 支援 DoH

    //timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    //coil
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Jetpack Compose 需要這個
    implementation ("com.github.bumptech.glide:compose:1.0.0-beta01")
}