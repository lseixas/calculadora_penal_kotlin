package com.example.calculadorapenalkotlin

import androidx.compose.runtime.Composable
import com.example.calculadorapenalkotlin.repositories.ContactRepository

@Composable
expect fun rememberAppDependencies(): AppDependencies

class AppDependencies(
    val contactRepository: ContactRepository
)