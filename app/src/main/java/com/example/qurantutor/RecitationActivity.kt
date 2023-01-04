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
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3Client
import com.anggrayudi.storage.SimpleStorageHelper
import com.anggrayudi.storage.file.*
import com.example.qurantutor.databinding.ActivityRecitationBinding
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.util.*


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





    private fun uploadRecitations2S3() {
//        val credentialsProvider = CognitoCachingCredentialsProvider(
//            this,
//            "us-east-1:81d54ad8-307c-4519-88b0-40b9761e80d9",
//            Regions.US_EAST_1
//        )
        val dotenv = dotenv {
                directory = "/assets"
                filename = "env"
            }
        val accessKey = dotenv["aws_access_key_id"]
        val secretKey = dotenv["aws_secret_access_key"]
        val credentialsProvider = BasicAWSCredentials(accessKey, secretKey)
        val s3Client = AmazonS3Client(credentialsProvider, Region.getRegion("us-east-1"))
        val transferUtility = TransferUtility(s3Client, this)
        TransferNetworkLossHandler.getInstance(this)
        val observer = transferUtility.upload(
            "recitations-audio-files", // The name of the bucket
            _fileName, // The key of the object
            file // The file to upload
        )
        observer.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                when(state) {
                    TransferState.IN_PROGRESS -> {
                        Log.i("Transfer", "In Progress")
                    }
                    TransferState.COMPLETED -> {
                        Log.i("Transfer", "Completed")
                    }
                    TransferState.FAILED -> {
                        Log.d("Transfer", "Failed")
                    }
                    TransferState.PAUSED -> {
                        Log.i("Transfer", "Paused")
                    }
                    else -> {
                        Log.i("Transfer", "......")
                    }
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                // Handle progress updates
                val progress = (bytesCurrent * 100 / bytesTotal).toInt()
                binding.uploadProgress.setProgressCompat(progress, true)
            }

            override fun onError(id: Int, ex: Exception) {
                Toast.makeText(this@RecitationActivity, "Failed To Upload The File With The Exception: $ex", Toast.LENGTH_LONG).show()
            }
        })
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
        uploadRecitations2S3()
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("fileName", _fileName)
        intent.putExtra("filePath", filePath)
        startActivity(intent)
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uid = UUID.randomUUID().toString()
        _fileName = "surahIkhlas-$uid.wav"
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