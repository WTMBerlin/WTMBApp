plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(Deps.compileSdkVersion)
    defaultConfig {
        applicationId = "com.wtmberlin"
        minSdkVersion(Deps.minSdkVersion)
        targetSdkVersion(Deps.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"))
        }
    }

    dataBinding {
        isEnabled = true
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Deps.kotlinVersion}")

    // Architecture Components
    implementation("androidx.lifecycle:lifecycle-extensions:${Deps.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-common-java8:${Deps.lifecycleVersion}")
    implementation("androidx.core:core-ktx:${Deps.coreVersion}")
    implementation("androidx.fragment:fragment-ktx:${Deps.fragmentVersion}")
    implementation("androidx.constraintlayout:constraintlayout:${Deps.constraintLayoutVersion}")

    // Support Library
    implementation("androidx.appcompat:appcompat:${Deps.appcompatVersion}")
    implementation("com.google.android.material:material:${Deps.materialVersion}")
    implementation("androidx.recyclerview:recyclerview:${Deps.recyclerViewVersion}")

    // Koin
    implementation("org.koin:koin-android:${Deps.koinVersion}")
    implementation("org.koin:koin-android-viewmodel:${Deps.koinVersion}")

    // Moshi
    implementation("com.squareup.moshi:moshi:${Deps.moshiVersion}")

    // Navigation
    implementation("android.arch.navigation:navigation-fragment-ktx:${Deps.navigationVersion}")
    implementation("android.arch.navigation:navigation-ui-ktx:${Deps.navigationVersion}")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:${Deps.retrofitVersion}")
    implementation("com.squareup.retrofit2:adapter-rxjava2:${Deps.retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-moshi:${Deps.retrofitVersion}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Deps.loggingInterceptorVersion}")

    // RxJava 2
    implementation("io.reactivex.rxjava2:rxjava:${Deps.rxjavaVersion}")
    implementation("io.reactivex.rxjava2:rxandroid:${Deps.rxandroidVersion}")

    //Room
    implementation("androidx.room:room-runtime:${Deps.roomVersion}")
    implementation("androidx.room:room-rxjava2:${Deps.roomVersion}")
    kapt("androidx.room:room-compiler:${Deps.roomVersion}") // use kapt for Kotlin

    // ThreeTenBp
    testImplementation("org.threeten:threetenbp:${Deps.threetenbpVersion}")
    implementation("com.jakewharton.threetenabp:threetenabp:${Deps.threetenbpAndroidVersion}")

    // Timber
    implementation("com.jakewharton.timber:timber:${Deps.timberVersion}")

    //Picasso
    implementation("com.squareup.picasso:picasso:${Deps.picassoVersion}")

    testImplementation("junit:junit:${Deps.junitVersion}")
}
