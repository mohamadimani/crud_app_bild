package com.example.contacts.data.remote

import com.example.contacts.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** ساخت Retrofit و ContactApi (فقط یک بار در کل اپ). */
object ApiClient {

    /**
     * آدرس پوشه‌ی php-api روی سرور. حتماً با / تمام شود.
     * مقدارش در app/build.gradle.kts تعیین می‌شود:
     *
     * - شبیه‌ساز (Emulator) اندروید: پیش‌فرض http://10.0.2.2/contacts-api/
     *   (10.0.2.2 یعنی localhost همان کامپیوتری که XAMPP رویش است)
     * - گوشی واقعی: موقع بیلد IP کامپیوتر در شبکه‌ی وای‌فای را بدهید:
     *   gradle assembleDebug -PapiBaseUrl=http://192.168.1.5/contacts-api/
     */
    private val BASE_URL = BuildConfig.API_BASE_URL

    val api: ContactApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // تبدیل JSON به کلاس‌های Kotlin و برعکس
            .build()
            .create(ContactApi::class.java)
    }
}
