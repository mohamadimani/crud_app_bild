package com.example.contacts.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * DAO = Data Access Object: همه‌ی کوئری‌های جدول contacts در Room.
 *
 * توابعی که Flow برمی‌گردانند «زنده» هستند: هر وقت جدول تغییر کند،
 * Room خودش لیست جدید را می‌فرستد و صفحه خودکار به‌روز می‌شود.
 */
@Dao
abstract class ContactDao {

    @Query("SELECT * FROM contacts ORDER BY name")
    abstract fun getAll(): Flow<List<ContactEntity>>

    @Query(
        """
        SELECT * FROM contacts
        WHERE name LIKE '%' || :query || '%'
           OR phone LIKE '%' || :query || '%'
           OR email LIKE '%' || :query || '%'
        ORDER BY name
        """
    )
    abstract fun search(query: String): Flow<List<ContactEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(contact: ContactEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(contacts: List<ContactEntity>)

    @Update
    abstract suspend fun update(contact: ContactEntity)

    @Delete
    abstract suspend fun delete(contact: ContactEntity)

    @Query("DELETE FROM contacts")
    abstract suspend fun deleteAll()

    /**
     * کل جدول را با لیست جدیدی که از سرور آمده جایگزین می‌کند.
     * @Transaction یعنی این دو کار با هم انجام می‌شوند و صفحه هیچ‌وقت لیست خالیِ وسط کار را نمی‌بیند.
     */
    @Transaction
    open suspend fun replaceAll(contacts: List<ContactEntity>) {
        deleteAll()
        insertAll(contacts)
    }
}
