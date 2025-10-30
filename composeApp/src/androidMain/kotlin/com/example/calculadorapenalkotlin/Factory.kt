package com.example.calculadorapenalkotlin

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.calculadorapenalkotlin.repositories.ContactRepository
import com.russhwolf.settings.SharedPreferencesSettings

@Composable
actual fun rememberAppDependencies(): AppDependencies {
    val context = LocalContext.current.applicationContext

    return remember {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val settings = SharedPreferencesSettings(prefs)
        val contactRepository = ContactRepository(settings)
        AppDependencies(contactRepository)
    }
}
