package com.example.qurantutor

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qurantutor.databinding.ActivityRecitationBinding
import java.io.IOException

private const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
class RecitationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecitationBinding
    private lateinit var mediaRecorder: MediaRecorder
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
        mediaRecorder.apply {
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

    private fun stopRecording() {
        mediaRecorder.apply {
            stop()
            release()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileName = "${externalCacheDir?.absolutePath}/audio.wav"
        binding = ActivityRecitationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        var mStartRecording = true

        binding.mic.setOnClickListener {
            onRecord(mStartRecording)
            var text = when (mStartRecording) {
                true -> "Stop recording"
                false -> "Start recording"
            }
            mStartRecording = !mStartRecording
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }

    }
    override fun onStop() {
        super.onStop()
        mediaRecorder.release()
    }
}