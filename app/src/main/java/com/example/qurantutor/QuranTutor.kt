//package com.example.qurantutor
//
//import android.app.Application
//import android.util.Log
//import com.amplifyframework.AmplifyException
//
//import com.amplifyframework.kotlin.core.Amplify
//
//class QuranTutor : Application() {
//    override fun onCreate() {
//        super.onCreate()
//
//        try {
//            Amplify.configure(applicationContext)
//            Log.d("MyAmplifyApp", "Initialized Amplify")
//        } catch (error: AmplifyException) {
//            Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
//        }
//    }
//}