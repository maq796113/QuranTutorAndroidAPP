package com.example.qurantutor

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qurantutor.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        binding.register.setOnClickListener {
            if (binding.email.text?.trim()!!.isNotEmpty() || binding.password.text?.trim()
                    !!.isNotEmpty() || binding.retypePassword.text?.trim()!!.isNotEmpty()
            ) {
                if (binding.password.text?.trim().toString() == binding.retypePassword.text?.trim().toString())
                {
                    createUser(
                        binding.email.text?.trim().toString(),
                        binding.password.text?.trim().toString()
                    )
                }
                else Toast.makeText(this, "Error: The Password did not match", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Error: Input Required", Toast.LENGTH_LONG).show()
            }

        }
        binding.signInIntent.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload()
        }
    }

    private fun reload() {
        finish()
        startActivity(intent)
    }

    private fun createUser(email: String, password: String) {
        Toast.makeText(this, "Im inside", Toast.LENGTH_LONG).show()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }



    companion object {
        private const val TAG = "EmailPassword"
    }
}


