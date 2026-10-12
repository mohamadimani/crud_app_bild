// فایل Gradle سطح پروژه: فقط پلاگین‌ها را معرفی می‌کند؛ تنظیمات اصلی در app/build.gradle.kts است
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
}
