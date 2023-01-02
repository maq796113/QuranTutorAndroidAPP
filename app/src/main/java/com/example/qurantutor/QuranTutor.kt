package com.example.qurantutor

import android.app.Application
import android.util.Log
import aws.sdk.kotlin.runtime.config.AwsSdkSetting
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthService
import com.amplifyframework.core.Amplify.configure
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import com.amplifyframework.storage.s3.options.AWSS3StorageUploadFileOptions


class QuranTutor : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            com.amplifyframework.kotlin.core.Amplify.addPlugin(AWSCognitoAuthPlugin())
            com.amplifyframework.kotlin.core.Amplify.addPlugin(AWSS3StoragePlugin())
            com.amplifyframework.kotlin.core.Amplify.configure(applicationContext)
            Log.i("MyAmplifyApp", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
        }
    }
}