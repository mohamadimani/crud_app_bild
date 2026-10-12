package com.example.contacts.data

import com.example.contacts.data.local.ContactDao
import com.example.contacts.data.local.ContactEntity
import com.example.contacts.data.remote.ApiResponse
import com.example.contacts.data.remote.ContactApi
import com.example.contacts.data.remote.ContactDto
import com.example.contacts.data.remote.DeleteRequest
import com.example.contacts.data.remote.toDto
import com.example.contacts.data.remote.toEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository تنها جایی است که می‌داند داده از کجا می‌آید (Room یا سرور).
 * ViewModel و UI فقط با همین کلاس کار می‌کنند و از Room و Retrofit خبر ندارند.
 *
 * قانون این پروژه:
 * - خواندن (لیست و جستجو) همیشه از Room است؛ پس بدون اینترنت هم کار می‌کند.
 * - نوشتن (افزودن، ویرایش، حذف) اول روی سرور انجام می‌شود و اگر موفق بود، در Room هم اعمال می‌شود.
 *   پس بدون اینترنت امکان‌پذیر نیست و خطا برمی‌گرداند.
 */
class ContactRepository(
    private val dao: ContactDao,
    private val api: ContactApi,
) {

    /** لیست مخاطبین از Room؛ اگر query خالی نباشد، نتیجه‌ی جستجو. */
    fun getContacts(query: String): Flow<List<ContactEntity>> =
        if (query.isBlank()) dao.getAll() else dao.search(query.trim())

    /** گرفتن لیست تازه از سرور و جایگزین کردن آن در Room. */
    suspend fun refresh() {
        val contacts = api.getContacts().requireSuccess().orEmpty()
        dao.replaceAll(contacts.map { it.toEntity() })
    }

    suspend fun add(name: String, phone: String, email: String) {
        val created = api.createContact(ContactDto(name = name, phone = phone, email = email))
            .requireSuccess()
        // سرور رکورد ساخته‌شده را با id جدید برمی‌گرداند؛ همان را در Room ذخیره می‌کنیم
        if (created != null) dao.insert(created.toEntity())
    }

    suspend fun update(contact: ContactEntity) {
        api.updateContact(contact.toDto()).requireSuccess()
        dao.update(contact)
    }

    suspend fun delete(contact: ContactEntity) {
        api.deleteContact(DeleteRequest(contact.id)).requireSuccess()
        dao.delete(contact)
    }

    /** اگر سرور گفت عملیات ناموفق بوده، پیامش را به‌صورت خطا پرتاب کن. */
    private fun <T> ApiResponse<T>.requireSuccess(): T? {
        if (!success) throw IllegalStateException(message)
        return data
    }
}
