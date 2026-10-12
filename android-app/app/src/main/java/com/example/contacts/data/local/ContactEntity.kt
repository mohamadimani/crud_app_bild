package com.example.contacts.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * یک ردیف از جدول contacts در دیتابیس آفلاین (Room).
 *
 * id را سرور (MySQL) می‌سازد، برای همین autoGenerate نداریم:
 * هر رکورد در گوشی همان id رکورد روی سرور را دارد.
 */
@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val phone: String,
    val email: String,
)
