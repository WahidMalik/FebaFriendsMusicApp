package com.example.febafriends

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.febafriends.databinding.FragmentAdminLoginBinding

class AdminLogin : Fragment() {

    private lateinit var binding: FragmentAdminLoginBinding
    val adminUsername: String = "admin"
    val adminPassword:String = "admin"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using ViewBinding
        binding = FragmentAdminLoginBinding.inflate(inflater, container, false)

        binding.loginButton.setOnClickListener {

            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if((username.trim() == adminUsername) && (password.trim() == adminPassword)){
                startActivity(Intent(requireContext(), AdminPanel::class.java))
            }
            else{
                Toast.makeText(requireContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }





}
