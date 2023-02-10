package com.example.qurantutor.data

import java.time.Instant

data class ReciterProfile(
    var time: Instant,
    var username: String,
    var surahID: Int,
    var bleu_score: Float,
)
