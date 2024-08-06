package com.example.febafriends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Find views
        val usernameIcon: ImageView = view.findViewById(R.id.username_icon)
        val passwordIcon: ImageView = view.findViewById(R.id.password_icon)
        val usernameEditText: EditText = view.findViewById(R.id.username_edit_text)
        val passwordEditText: EditText = view.findViewById(R.id.password_edit_text)

        // Set color filter based on focus
        usernameEditText.setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) R.color.blue else R.color.black
            usernameIcon.setColorFilter(ContextCompat.getColor(requireContext(), color))
        }

        passwordEditText.setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) R.color.blue else R.color.black
            passwordIcon.setColorFilter(ContextCompat.getColor(requireContext(), color))
        }

        return view
    }
}
