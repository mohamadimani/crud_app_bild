package com.example.contacts

import android.app.Application
import androidx.room.Room
import com.example.contacts.data.ContactRepository
import com.example.contacts.data.local.AppDatabase
import com.example.contacts.data.remote.ApiClient

/**
 * کلاس Application قبل از هر صفحه‌ای، فقط یک بار ساخته می‌شود.
 * اینجا دیتابیس Room و Repository را دستی می‌سازیم (بدون Hilt)
 * تا همه‌ی صفحه‌ها از همین یک نمونه استفاده کنند.
 */
class ContactsApp : Application() {

    lateinit var repository: ContactRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val database = Room.databaseBuilder(this, AppDatabase::class.java, "contacts.db").build()
        repository = ContactRepository(database.contactDao(), ApiClient.api)
    }
}
