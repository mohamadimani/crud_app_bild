package com.example.contacts.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * تعریف دیتابیس Room: چه جدول‌هایی دارد و نسخه‌اش چند است.
 * پیاده‌سازی واقعی این کلاس را Room موقع بیلد خودش می‌سازد.
 */
@Database(entities = [ContactEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}
