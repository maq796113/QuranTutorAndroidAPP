package com.example.qurantutor.mongodb



import io.realm.kotlin.mongodb.App

import io.realm.kotlin.mongodb.Credentials

object Auth {

    val app = App.create("qurantutor-swerk")


    val credentials = Credentials.anonymous()




}