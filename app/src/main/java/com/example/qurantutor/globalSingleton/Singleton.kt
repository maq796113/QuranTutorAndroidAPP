package com.example.qurantutor.globalSingleton

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Singleton @Inject constructor(){

    var isLoading = false
    var isMale = false
    var surahID = 0
    var username = ""
}