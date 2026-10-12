package com.example.contacts.data.remote

import com.example.contacts.data.local.ContactEntity

/**
 * DTO = Data Transfer Object: شکل داده‌ای که بین اپ و سرور PHP رد و بدل می‌شود.
 *
 * عمداً از ContactEntity جداست: ممکن است شکل JSON سرور با جدول Room فرق کند
 * (مثلاً اینجا email ممکن است null بیاید)، و هر لایه مدل خودش را دارد.
 */
data class ContactDto(
    val id: Int = 0, // موقع افزودن، سرور این مقدار را نادیده می‌گیرد و id جدید می‌سازد
    val name: String,
    val phone: String,
    val email: String?,
)

/** فرمت یکسان همه‌ی جواب‌های سرور: {"success": ..., "message": ..., "data": ...} */
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?,
)

/** بدنه‌ی درخواست حذف: {"id": 1} */
data class DeleteRequest(val id: Int)

// تبدیل مدل‌ها بین دو لایه
fun ContactDto.toEntity() = ContactEntity(id = id, name = name, phone = phone, email = email.orEmpty())

fun ContactEntity.toDto() = ContactDto(id = id, name = name, phone = phone, email = email)
