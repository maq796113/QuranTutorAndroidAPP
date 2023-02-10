package com.example.qurantutor

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.example.qurantutor.databinding.ActivityLoginBinding
import com.example.qurantutor.globalSingleton.Singleton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CheckResult")
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    @Inject
    lateinit var singleton: Singleton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (!Environment.isExternalStorageManager()) {
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            startActivity(intent)
        }
        auth = FirebaseAuth.getInstance()
        binding.loginButton.setOnClickListener {
            if (binding.email.text!!.trim().toString().isEmpty() || binding.password.text!!.trim().toString().isEmpty()) {
                Snackbar.make(binding.coordinator, "Input Required", Snackbar.LENGTH_LONG).show()
            } else {
                login(binding.email.text!!.trim().toString(), binding.password.text!!.trim().toString())
            }
        }

        binding.signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        if(currentUser != null){
//            recreate()
//        }
//    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                singleton.username = email.substringBefore("@")
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            }
            else {
                Snackbar.make(binding.coordinator, "Error !! If you don't have an account yet then sign up.", Snackbar.LENGTH_LONG).setAction("Sign Up") {
                    startActivity(Intent(this, SignUpActivity::class.java))
                }.show()
            }
        }

    }

}