package com.example.febafriends

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.febafriends.databinding.ActivityAdminPanelBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class AdminPanel : AppCompatActivity() {
    lateinit var binding: ActivityAdminPanelBinding
    private var selectedBible: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityAdminPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        setSupportActionBar(binding.toolbarAdminPanel)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarAdminPanel.navigationIcon?.setTint(ContextCompat.getColor(this, android.R.color.holo_red_dark))
        binding.textAdminToolbar.text = "Admin Panel"

        binding.toolbarAdminPanel.setNavigationOnClickListener {
            onBackPressed()
        }

        // Initialize categories for the Song Spinner
        val songCategories = arrayOf("English", "Urdu", "Hamd-o-sana", "Pashto","Punjabi","Sindhi","Siraiki","Urdu audio Bible")
        val songAdapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, songCategories)
        songAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = songAdapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedCategory = parent.getItemAtPosition(position).toString()
                binding.categoryEditText.setText(selectedCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        // Initialize Bible options for the Spinner
        val bibleBooks = arrayOf(
            "Matthew", "Mark", "Luke", "John", "Acts", "Romans", "1Corinthians", "2Corinthians",
            "Galatians", "Ephesians", "Philippians", "Colossians", "1Thessalonians", "2Thessalonians",
            "1Timothy", "2Timothy", "Titus", "Philemon", "Hebrews", "James", "1Peter", "2Peter",
            "1John", "2John", "3John", "Jude", "Revelation"
        )
        val bibleAdapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, bibleBooks)
        bibleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.biblespinner.adapter = bibleAdapter

        binding.biblespinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedBible = parent.getItemAtPosition(position).toString()
                binding.bibleEditText.setText(selectedBible)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        binding.radioGroupOptions.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonSong -> {
                    binding.songUploadLayout.visibility = View.VISIBLE
                    binding.bibleUploadLayout.visibility = View.GONE
                }
                R.id.radioButtonBible -> {
                    binding.songUploadLayout.visibility = View.GONE
                    binding.bibleUploadLayout.visibility = View.VISIBLE
                }
            }
        }

        binding.uploadSongs.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "audio/*"
            songsLauncher.launch("audio/*")
        }

        binding.uploadbible.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "audio/*"
            bibleLauncher.launch("audio/*")
        }
    }

    private val songsLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val fileName = getFileName(it)
            val storageRef = FirebaseStorage.getInstance().reference.child("songs/$fileName")
            storageRef.putFile(it)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        val db = FirebaseFirestore.getInstance()
                        val category = binding.categoryEditText.text.toString()
                        val song = hashMapOf(
                            "id" to fileName,
                            "title" to fileName,
                            "url" to downloadUrl.toString()
                        )
                        db.collection("songs").document(category).collection("songs").add(song)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Song uploaded successfully", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to upload song", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to upload song", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private val bibleLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val fileName = getFileName(it)
            val storageRef = FirebaseStorage.getInstance().reference.child("songs/$fileName")
            storageRef.putFile(it)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        val db = FirebaseFirestore.getInstance()
                        val bibleCategory = binding.bibleEditText.text.toString()
                        val bible = hashMapOf(
                            "id" to fileName,
                            "title" to fileName,
                            "url" to downloadUrl.toString()
                        )
                        db.collection("songs").document(bibleCategory).collection("songs").add(bible)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Bible uploaded successfully", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to upload Bible", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to upload Bible", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            it.moveToFirst()
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex >= 0) {
                result = it.getString(nameIndex)
            }
        }
        return result ?: uri.toString()
    }
}
