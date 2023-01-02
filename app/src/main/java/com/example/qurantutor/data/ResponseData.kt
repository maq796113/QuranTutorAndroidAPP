package com.example.qurantutor.data

data class ResponseData(
    val language: String,
    val transcribed_text: String,
    val bleu_score: Int
)
