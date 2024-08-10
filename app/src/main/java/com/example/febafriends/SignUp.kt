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
import com.example.febafriends.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class SignUp : AppCompatActivity() {

    lateinit var binding : ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createAccount.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            if(!Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), email)){
                binding.emailEditText.error = "Invalid Email"
                return@setOnClickListener
            }

            if(password.length < 6){
                binding.passwordEditText.error = "Password length must be greater than 6"
                return@setOnClickListener
            }

            if(password != confirmPassword){
                binding.confirmPasswordEditText.error = "Password not matched"
                return@setOnClickListener
            }
            
            createAccountFirebase(email,password)

        }

        binding.alreadyaccount.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }


    }

    fun createAccountFirebase(email : String, password : String){
        progressbar(true)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                progressbar(false)
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                progressbar(false)
                Toast.makeText(this, "Already have this Account", Toast.LENGTH_SHORT).show()
            }
    }

    fun progressbar(inProgress : Boolean){
        if(inProgress){
            binding.progressBar.visibility = View.VISIBLE
            binding.createAccount.visibility = View.GONE
        }else{
            binding.progressBar.visibility = View.GONE
            binding.createAccount.visibility = View.VISIBLE
        }
    }

}