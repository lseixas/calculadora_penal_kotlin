package com.example.calculadorapenalkotlin

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform