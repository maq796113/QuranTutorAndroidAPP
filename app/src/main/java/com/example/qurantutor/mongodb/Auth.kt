package com.example.qurantutor.mongodb


import com.example.qurantutor.BuildConfig
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.AppConfiguration
import io.realm.kotlin.mongodb.Credentials

object Auth {

    val app = App.create("qurantutor-swerk")


    val credentials = Credentials.anonymous()




}