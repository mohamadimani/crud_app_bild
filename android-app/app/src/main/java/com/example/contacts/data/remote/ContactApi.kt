package com.example.contacts.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * تعریف APIهای سرور برای Retrofit.
 * هر تابع یکی از فایل‌های PHP را صدا می‌زند؛ Retrofit خودش پیاده‌سازی را می‌سازد.
 */
interface ContactApi {

    @GET("list.php")
    suspend fun getContacts(): ApiResponse<List<ContactDto>>

    @POST("create.php")
    suspend fun createContact(@Body contact: ContactDto): ApiResponse<ContactDto>

    @POST("update.php")
    suspend fun updateContact(@Body contact: ContactDto): ApiResponse<ContactDto>

    @POST("delete.php")
    suspend fun deleteContact(@Body request: DeleteRequest): ApiResponse<Unit>
}
