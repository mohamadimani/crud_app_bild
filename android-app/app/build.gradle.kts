plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp) // برای تولید خودکار کدهای Room
}

android {
    namespace = "com.example.contacts"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.contacts"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // آدرس سرور PHP. پیش‌فرض برای شبیه‌ساز است (10.0.2.2 = localhost کامپیوتر).
        // برای گوشی واقعی موقع بیلد آدرس دیگری بدهید:
        //   gradle assembleDebug -PapiBaseUrl=http://192.168.1.5/contacts-api/
        val apiBaseUrl = (project.findProperty("apiBaseUrl") as String?) ?: "http://10.0.2.2/contacts-api/"
        buildConfigField("String", "API_BASE_URL", "\"$apiBaseUrl\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
        buildConfig = true // برای BuildConfig.API_BASE_URL
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
}
