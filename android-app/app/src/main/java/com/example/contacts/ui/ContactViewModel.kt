package com.example.contacts.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.contacts.ContactsApp
import com.example.contacts.data.ContactRepository
import com.example.contacts.data.local.ContactEntity
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException

/**
 * ViewModel وضعیت صفحه را نگه می‌دارد و کارهای کاربر را به Repository می‌سپارد.
 * با چرخاندن گوشی از بین نمی‌رود، پس داده‌ها و جستجو حفظ می‌شوند.
 *
 * UI فقط StateFlowها را می‌خواند و توابع عمومی این کلاس را صدا می‌زند.
 */
class ContactViewModel(private val repository: ContactRepository) : ViewModel() {

    /** متن کادر جستجو */
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    /** آیا درخواستی به سرور در حال انجام است؟ (برای نمایش نوار پیشرفت) */
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /** پیامی که باید در Snackbar نشان داده شود (موفقیت یا خطا) */
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    /**
     * لیست مخاطبین: هر بار query عوض شود، کوئری جدیدی روی Room اجرا می‌شود
     * (flatMapLatest کوئری قبلی را لغو می‌کند). چون منبع Room است، آفلاین هم کار می‌کند.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val contacts: StateFlow<List<ContactEntity>> = _query
        .flatMapLatest { repository.getContacts(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        // با باز شدن صفحه، داده‌های تازه را از سرور می‌گیریم
        refresh()
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    fun refresh() = runAction(successMessage = null) {
        repository.refresh()
    }

    fun addContact(name: String, phone: String, email: String) = runAction("مخاطب اضافه شد") {
        repository.add(name, phone, email)
    }

    fun updateContact(contact: ContactEntity) = runAction("مخاطب ویرایش شد") {
        repository.update(contact)
    }

    fun deleteContact(contact: ContactEntity) = runAction("مخاطب حذف شد") {
        repository.delete(contact)
    }

    /** UI بعد از نمایش پیام این را صدا می‌زند تا پیام دوباره نمایش داده نشود. */
    fun onMessageShown() {
        _message.value = null
    }

    /** الگوی مشترک همه‌ی عملیات‌ها: نمایش لودینگ، اجرای کار، و تبدیل خطا به پیام قابل فهم. */
    private fun runAction(successMessage: String?, action: suspend () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                action()
                if (successMessage != null) _message.value = successMessage
            } catch (e: CancellationException) {
                throw e // لغو شدن coroutine خطا نیست؛ نباید آن را بگیریم
            } catch (e: Exception) {
                _message.value = errorMessage(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun errorMessage(e: Exception): String = when (e) {
        is IOException -> "اتصال به سرور برقرار نشد. اطلاعات ذخیره‌شده‌ی گوشی نمایش داده می‌شود."
        is HttpException -> "خطای سرور (کد ${e.code()})"
        else -> e.message ?: "خطای ناشناخته"
    }

    companion object {
        /** چون ViewModel ورودی (repository) دارد، باید بگوییم چطور ساخته شود. */
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as ContactsApp
                ContactViewModel(app.repository)
            }
        }
    }
}
