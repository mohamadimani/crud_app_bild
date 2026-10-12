package com.example.contacts.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.contacts.data.local.ContactEntity

/**
 * دیالوگ فرم مخاطب، هم برای افزودن و هم برای ویرایش.
 * اگر contact برابر null باشد یعنی افزودن، وگرنه ویرایش همان مخاطب.
 */
@Composable
fun ContactDialog(
    contact: ContactEntity?,
    onDismiss: () -> Unit,
    onSave: (name: String, phone: String, email: String) -> Unit,
) {
    // rememberSaveable: با چرخاندن گوشی، متن‌های تایپ‌شده پاک نمی‌شوند
    var name by rememberSaveable { mutableStateOf(contact?.name.orEmpty()) }
    var phone by rememberSaveable { mutableStateOf(contact?.phone.orEmpty()) }
    var email by rememberSaveable { mutableStateOf(contact?.email.orEmpty()) }
    var showErrors by rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (contact == null) "مخاطب جدید" else "ویرایش مخاطب") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("نام *") },
                    isError = showErrors && name.isBlank(),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("شماره تلفن *") },
                    isError = showErrors && phone.isBlank(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("ایمیل (اختیاری)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (name.isBlank() || phone.isBlank()) {
                    showErrors = true
                } else {
                    onSave(name.trim(), phone.trim(), email.trim())
                }
            }) { Text("ذخیره") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("انصراف") }
        },
    )
}
