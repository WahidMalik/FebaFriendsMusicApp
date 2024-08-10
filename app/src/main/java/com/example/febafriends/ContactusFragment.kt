package com.example.febafriends

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class ContactusFragment : Fragment() {

    lateinit var num1: TextView
    lateinit var num2: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_contactus, container, false)

        // Initialize the views after inflating the layout
        num1 = view.findViewById(R.id.num1)
        num2 = view.findViewById(R.id.num2)

        num1.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${num1.text}")
            startActivity(intent)
        }

        num2.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${num2.text}")
            startActivity(intent)
        }

        return view
    }
}