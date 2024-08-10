package com.example.febafriends

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.febafriends.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class Login : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val email = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if(!Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), email)){
                binding.usernameEditText.error = "Invalid Email"
                return@setOnClickListener
            }

            if(password.length < 6){
                binding.passwordEditText.error = "Password length must be greater than 6"
                return@setOnClickListener
            }



            loginWithFirebase(email,password)
        }

        binding.donthaveaccount.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }


    }
    fun loginWithFirebase(email : String, password : String){
        progressbar(true)
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                progressbar(false)
                startActivity(Intent(this@Login,MainActivity::class.java))
                finish()
            }.addOnFailureListener {
                progressbar(false)
                Toast.makeText(this, "Login Account Failed", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onResume(){
        super.onResume()
        FirebaseAuth.getInstance().currentUser?.apply {
            startActivity(Intent(this@Login,MainActivity::class.java))
            finish()
        }

    }



    fun progressbar(inProgress : Boolean){
        if(inProgress){
            binding.progressBar.visibility = View.VISIBLE
            binding.loginButton.visibility = View.GONE
        }else{
            binding.progressBar.visibility = View.GONE
            binding.loginButton.visibility = View.VISIBLE
        }
    }
}