package com.example.qurantutor

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.qurantutor.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CheckResult")
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        binding.loginButton?.setOnClickListener {
            if (binding.email.text!!.trim().toString().isEmpty() || binding.password.text!!.trim().toString().isEmpty()) {
                Toast.makeText(this, "Input required", Toast.LENGTH_LONG).show()
            }
            else {
                    login(binding.email.text!!.trim().toString(), binding.password.text!!.trim().toString())
            }
        }

        binding.signUpBtn?.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this, RecitationActivity::class.java))
            }
            else {
                Toast.makeText(this, "Error !! "+ task.exception, Toast.LENGTH_LONG).show()
            }
        }

    }

}