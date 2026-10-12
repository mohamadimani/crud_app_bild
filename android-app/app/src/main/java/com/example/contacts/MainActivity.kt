package com.example.contacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.contacts.ui.ContactsScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // متن‌ها فارسی هستند، پس صفحه را راست‌به‌چپ می‌کنیم (حتی اگر زبان گوشی انگلیسی باشد)
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    ContactsScreen()
                }
            }
        }
    }
}
