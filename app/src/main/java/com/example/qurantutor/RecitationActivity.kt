package com.example.qurantutor

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleCoroutineScope
import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.PutObjectResult
import com.amplifyframework.core.Amplify.Storage
import com.amplifyframework.kotlin.core.Amplify.Companion.Storage
import com.amplifyframework.storage.options.StorageUploadFileOptions
import com.anggrayudi.storage.SimpleStorageHelper
import com.anggrayudi.storage.file.*
import com.example.qurantutor.databinding.ActivityRecitationBinding
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.util.*
import androidx.lifecycle.lifecycleScope
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.Consumer
import com.amplifyframework.storage.StorageException




private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
class RecitationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecitationBinding
    private var mediaRecorder: MediaRecorder? = null
    private var permissionToRecordAccepted = false
    private var permissionsToRecord: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var _fileName: String = ""
    private var filePath: String = ""
    private var promptText: String = ""
    private lateinit var file: File
    private var clicks: Int = 0
    private var uid: String = ""
    private var counter: Int = 0
    private val storageHelper = SimpleStorageHelper(this)



    private fun generateFileName(): String {
        counter++
        return "surahIkhlas-$counter.wav"
    }
    @RequiresApi(Build.VERSION_CODES.S)
    private fun createAudioFolder() {
        val email = intent.getStringExtra("emailAddress")
        val root = this.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        file = File(root!!.absolutePath, email!!)
        val makeFolder = file.mkdirs()
        if (!makeFolder && !file.exists()) {
            Toast.makeText(this, "!!ERROR!! Failed To Make File", Toast.LENGTH_LONG).show()
        }
        filePath = file.absolutePath
    }

//    private suspend fun uploadAudioFie2S3(): PutObjectResult? {
//        val result: PutObjectResult?
//        try {
//            val dotenv = dotenv {
//                directory = "/assets"
//                filename = "env"
//            }
//            val accessKey = dotenv["AWS_ACCESS_KEY"]
//            val secretKey = dotenv["AWS_SECRET_KEY"]
//            val credentialsProvider = BasicAWSCredentials(accessKey, secretKey)
//            val s3Client = AmazonS3Client(credentialsProvider)
//            val request = PutObjectRequest("audiofileswav", generateFileName(), filePath)
//            delay(1)
//            result = s3Client.putObject(request)
//            if (result!=null) {
//                Log.d("TAG", "Coroutine Successful")
//            }
//            return result
//        } catch  (e: AmazonServiceException) {
//            Log.d("error", "Amazon Service")
//        }
//        return null
//    }
//
//    private suspend fun letsUploadAudioFie2S3() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val result = async { uploadAudioFie2S3() }
//            result.await()
//        }
//    }
    private suspend fun uploadFile() {
        val options = StorageUploadFileOptions.defaultInstance()
        val upload = Amplify.Storage.uploadFile(
                generateFileName(),
                file,
                options,
                { progress ->
                    Log.i("QuranTutor", "Fraction Completed: ${progress.fractionCompleted}")
                },
                { result ->
                    Log.i("QuranTutor", "Successfully uploaded ${result.key}")
                },
                { error ->
                    Log.i("QuranTutor", "Upload failed, ${error.message}")
                }
            )
        try {
            delay(1)
            val result = upload.request
            Log.i("QuranTutor", "Successfully uploaded: ${result.toString()}")
        } catch (error: StorageException) {
            Log.e("QuranTutor", "Upload failed", error)
        }
    }
    private suspend fun letsUploadAudioFiles() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = async { uploadFile() }
            result.await()
        }
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        storageHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) finish()
    }
    private fun onRecord(start: Boolean) = if (start) {
        startRecording()
    } else {
        stopRecording()
    }

    private fun startRecording() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(file)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            try {
                prepare()
                Toast.makeText(this@RecitationActivity, "SUCCESS", Toast.LENGTH_LONG).show()
            } catch (e: IOException) {
                Toast.makeText(this@RecitationActivity, "prepare() failed with error: $e", Toast.LENGTH_LONG).show()
            }
            start()
            Toast.makeText(this@RecitationActivity, "Started Recording", Toast.LENGTH_LONG).show()
        }
    }



    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        CoroutineScope(Dispatchers.IO).launch {
            //letsUploadAudioFie2S3()
            letsUploadAudioFiles()
        }
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("fileName", _fileName)
        intent.putExtra("filePath", filePath)
        startActivity(intent)
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Amplify().initializeAmplify(this)
        uid = UUID.randomUUID().toString()
        _fileName = "audio$uid.wav"
        binding = ActivityRecitationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        createAudioFolder()
        ActivityCompat.requestPermissions(this, permissionsToRecord, REQUEST_RECORD_AUDIO_PERMISSION)
        var mStartRecording = true
        Toast.makeText(this, "Files Will Be Stored Here: $filePath", Toast.LENGTH_LONG).show()
        filePath = "$filePath/$_fileName"
        file = File(filePath)
        binding.mic.setOnClickListener {
            clicks++
            if (clicks==2) {
                mStartRecording = !mStartRecording
            }
            else if (clicks>2) {
                promptText = "Hold Your Horses"
            }
            onRecord(mStartRecording)
            promptText = when (mStartRecording) {
                true -> "Recording Started"
                false -> "Recording Finished"
            }
            Toast.makeText(this, promptText, Toast.LENGTH_LONG).show()
        }

    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        storageHelper.storage.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        storageHelper.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        storageHelper.onRestoreInstanceState(savedInstanceState)
    }
}