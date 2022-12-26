package com.example.qurantutor

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.AlphabeticIndex.Record
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.PutObjectRequest
import com.example.qurantutor.databinding.ActivityRecitationBinding
import java.io.File
import java.io.IOException
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.*

private const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
class RecitationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecitationBinding
    private var mediaRecorder: MediaRecorder? = null
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var fileName: String = ""
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(fileName)
            try {
                prepare()
            } catch (e: IOException) {
                Toast.makeText(this@RecitationActivity, "$LOG_TAG prepare() failed", Toast.LENGTH_LONG).show()
            }
            start()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        GlobalScope.async {
            uploadAudioFie2S3()
        }
        val intent = Intent(this, ResultActivity::class.java)
        startActivity(intent)
    }

    private suspend fun uploadAudioFie2S3() {
        coroutineScope {
            try {
                val env = dotenv()
                val accessKey = env["AWS_ACCESS_KEY"]
                val secretKey = env["AWS_SECRET_KEY"]
                val credentialsProvider = BasicAWSCredentials(accessKey, secretKey)
                val s3Client = AmazonS3Client(credentialsProvider)
                val file = File(fileName)
                val request = PutObjectRequest("audiofileswav", fileName, file)
                launch {
                    val result = s3Client.putObject(request)
                    Toast.makeText(
                        this@RecitationActivity,
                        "File uploaded with eTag: ${result.eTag} and versionId: ${result.versionId}",
                        Toast.LENGTH_LONG).show()
                }
            } catch  (e: AmazonServiceException) {
                println("Error: ${e.errorMessage}")
                println("Status code: ${e.statusCode}")
                println("Error type: ${e.errorType}")
                println("Request ID: ${e.requestId}")
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileName = "${externalCacheDir?.absolutePath}/audio.wav"
        binding = ActivityRecitationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        var mStartRecording = true
        binding.mic.setOnClickListener {
            onRecord(mStartRecording)
            var text = when (mStartRecording) {
                true -> "Recording Started"
                false -> "Recording Finished"
            }
            mStartRecording = !mStartRecording
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }

    }
    override fun onStop() {
        super.onStop()
        mediaRecorder?.release()
        mediaRecorder = null

    }
}