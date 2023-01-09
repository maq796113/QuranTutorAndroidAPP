package com.example.qurantutor

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.anggrayudi.storage.SimpleStorageHelper
import com.anggrayudi.storage.file.*
import com.example.qurantutor.databinding.ActivityRecitationBinding
import com.example.qurantutor.network.RetrofitBuilderSmb.smbService
import kotlinx.coroutines.*
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.*
import retrofit2.Callback


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
            } catch (e: IOException) {
                Toast.makeText(this@RecitationActivity, "prepare() failed with error: $e", Toast.LENGTH_LONG).show()
            }
            start()
        }
    }



    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        uploadRecitation()
    }

    private fun uploadRecitation() {
        val requestFile = RequestBody.create(MediaType.parse("audio/x-wav"), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val call = smbService.uploadFile(body)
        call.enqueue(object : Callback<ResponseBody> {

            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@RecitationActivity, "Upload Failed With The Throwable: $t", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: retrofit2.Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    // the upload was successful
                    Toast.makeText(this@RecitationActivity, "Upload Was A Successful", Toast.LENGTH_LONG).show()
                } else {
                    // the upload was not successful
                    Toast.makeText(this@RecitationActivity, "Error uploading file", Toast.LENGTH_SHORT).show()
                }
            }

        })
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


