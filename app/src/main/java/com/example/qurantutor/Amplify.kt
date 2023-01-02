package com.example.qurantutor

import android.content.Context
import android.widget.Toast
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify.addPlugin
import com.amplifyframework.core.Amplify.configure
import com.amplifyframework.storage.s3.AWSS3StoragePlugin


class Amplify {
    fun initializeAmplify(context: Context) {
        try {
            addPlugin(AWSCognitoAuthPlugin())
            addPlugin(AWSS3StoragePlugin())
            configure(context)
            Toast.makeText(context, "Initialized Amplify", Toast.LENGTH_LONG).show()
        } catch (e: AmplifyException) {
            Toast.makeText(context, "Could Not Initialize Amplify $e", Toast.LENGTH_LONG).show()
        }
    }
}