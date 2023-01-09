package com.example.qurantutor.network

import io.github.cdimascio.dotenv.dotenv

object BaseURL {
    val dotenv = dotenv {
        directory = "/assets"
        filename = "env"
    }
    private val password = dotenv["passwd"]
    private val username = dotenv["username"]
    const val modelbaseUrl = "https://4.193.97.134:8080"
    val smbbaseUrl = "smb://$username:$password@4.193.97.134/shared/"
}