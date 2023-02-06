package com.example.qurantutor

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import com.anggrayudi.storage.SimpleStorageHelper
import com.anggrayudi.storage.file.*
import com.example.qurantutor.databinding.ActivityRecitationBinding
import com.example.qurantutor.globalSingleton.Singleton
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject


private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
@AndroidEntryPoint
class RecitationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecitationBinding
    private lateinit var storageFirebase: FirebaseStorage
    private var mediaRecorder: MediaRecorder? = null
    private var permissionToRecordAccepted = false
    private var permissionsToRecord: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var _fileName: String = ""
    private var filePath: String = ""
    private var promptText: String = ""
    private lateinit var file: File
    private var clicks: Int = 0
    private var uid: String = ""
    @Inject
    lateinit var singleton: Singleton
    private lateinit var textViews: List<TextView>


    private val storageHelper = SimpleStorageHelper(this)
    private lateinit var storageRef: StorageReference


    @RequiresApi(Build.VERSION_CODES.S)
    private fun createAudioFolder() {
        val root = this.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        file = File(root!!.absolutePath, singleton.username)
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
        var progress = 0.0f
        val recitationFileRef = storageRef.child("${singleton.username}/$_fileName")

        recitationFileRef.putFile(file.toUri()).addOnSuccessListener {
            Toast.makeText(this, "File Upload Started Successfully", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(this, "File Upload Failed", Toast.LENGTH_LONG).show()
        }.addOnProgressListener { taskSnapshot ->
            progress = 100.0f*(taskSnapshot.bytesTransferred/taskSnapshot.totalByteCount)
            binding.uploadProgress.setProgressCompat(progress.toInt(), true)
        }.addOnCompleteListener {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("fileName", _fileName)
            startActivity(intent)
        }
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecitationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        Log.d("SurahID", singleton.surahID.toString())
        var surahn: String = ""
        when (singleton.surahID+1) {
            1 -> {
                makeTextViewsClean()
                surahn = "Surah Fatihah"
                binding.verse1.text = resources.getText(R.string.surah_fatiha_1)
                binding.verse2.text = resources.getText(R.string.surah_fatiha_2)
                binding.verse3.text = resources.getText(R.string.surah_fatiha_3)
                binding.verse4.text = resources.getText(R.string.surah_fatiha_4)
                binding.verse5.text = resources.getText(R.string.surah_fatiha_5)
                binding.verse6.text = resources.getText(R.string.surah_fatiha_6)
            }
            94 -> {
                makeTextViewsClean()
                surahn = "Surah Ash-Sharh"
                binding.verse1.text = resources.getText(R.string.surah_ash_sharh_1)
                binding.verse2.text = resources.getText(R.string.surah_ash_sharh_2)
                binding.verse3.text = resources.getText(R.string.surah_ash_sharh_3)
                binding.verse4.text = resources.getText(R.string.surah_ash_sharh_4)
                binding.verse5.text = resources.getText(R.string.surah_ash_sharh_5)
                binding.verse6.text = resources.getText(R.string.surah_ash_sharh_6)
                binding.verse7.text = resources.getText(R.string.surah_ash_sharh_7)
                binding.verse8.text = resources.getText(R.string.surah_ash_sharh_8)
            }
            95 -> {
                makeTextViewsClean()
                surahn = "Surah At-Tin"
                binding.verse1.text = resources.getText(R.string.surah_at_tin_1)
                binding.verse2.text = resources.getText(R.string.surah_at_tin_2)
                binding.verse3.text = resources.getText(R.string.surah_at_tin_3)
                binding.verse4.text = resources.getText(R.string.surah_at_tin_4)
                binding.verse5.text = resources.getText(R.string.surah_at_tin_5)
                binding.verse6.text = resources.getText(R.string.surah_at_tin_6)
                binding.verse7.text = resources.getText(R.string.surah_at_tin_7)
                binding.verse8.text = resources.getText(R.string.surah_at_tin_8)
            }
            97 -> {
                makeTextViewsClean()
                surahn = "Surah Al-Qadr"
                binding.verse1.text = resources.getText(R.string.surah_al_qadr_1)
                binding.verse2.text = resources.getText(R.string.surah_al_qadr_2)
                binding.verse3.text = resources.getText(R.string.surah_al_qadr_3)
                binding.verse4.text = resources.getText(R.string.surah_al_qadr_4)
                binding.verse5.text = resources.getText(R.string.surah_al_qadr_5)
            }
            98 -> {
                makeTextViewsClean()
                surahn = "Surah Al-Bayyinah"

            }
            99 -> {
                makeTextViewsClean()
                surahn = "Surah Az-Zalzalah"
            }
            102 -> {
                makeTextViewsClean()
                surahn = "Surah At-Takathur"
            }
            103 -> {
                makeTextViewsClean()
                surahn = "Surah Al-'Asr"
            }
            105 -> {
                makeTextViewsClean()
                surahn = "Surah Al-Fil"
            }
            106 -> {
                makeTextViewsClean()
                surahn = "Surah Quraysh"
            }
            107 -> {
                makeTextViewsClean()
                surahn = "Surah Al-Ma'un"
            }
            108 -> {
                makeTextViewsClean()
                surahn = "Surah Al-Kawthar"
            }
            109 -> {
                makeTextViewsClean()
                surahn = "Surah Al-Kafirun"
            }
            110 -> {
                makeTextViewsClean()
                surahn = "Surah An-Nasr"
            }
            111 -> {
                makeTextViewsClean()
                surahn = "Surah Al-Massad"
            }
            112 -> {
                surahn = "Surah Al-Ikhlas"
                //default
            }
            113 -> {
                makeTextViewsClean()
                surahn = "Surah Al-Falaq"
            }
            114 -> {
                makeTextViewsClean()
                surahn = "Surah An-Nas"
            }

        }

        setTitleActionbar(surahn)
        var visible = true
        binding.toggleVisibility.setOnClickListener {
            if (visible) {
                for (textView in textViews) {
                    textView.visibility = View.INVISIBLE
                }
            } else {
                for (textView in textViews) {
                    textView.visibility = View.VISIBLE
                }
            }
            visible = !visible
        }
        uid = UUID.randomUUID().toString()
        _fileName = "surahIkhlas-$uid.wav"
        createAudioFolder()
        ActivityCompat.requestPermissions(this, permissionsToRecord, REQUEST_RECORD_AUDIO_PERMISSION)
        var mStartRecording = true
        Toast.makeText(this, "Files Will Be Stored Here: $filePath", Toast.LENGTH_LONG).show()
        filePath = "$filePath/$_fileName"
        file = File(filePath)
        storageFirebase = Firebase.storage("gs://quran-tutor-4ab8a.appspot.com")
        storageRef = storageFirebase.reference
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

    private fun makeTextViewsClean() {
        textViews = listOf(binding.verse1, binding.verse2, binding.verse3, binding.verse3, binding.verse4, binding.verse5, binding.verse6, binding.verse7, binding.verse8)
        for (textView in textViews) {
            textView.text = null
        }
    }

    private fun setTitleActionbar(surahName: String) {
        supportActionBar?.title =
            String.format(resources.getString(R.string.recitation_mode), surahName)
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


