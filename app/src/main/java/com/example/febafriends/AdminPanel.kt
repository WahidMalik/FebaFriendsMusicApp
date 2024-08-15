package com.example.febafriends

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
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

        binding.uploadSongs.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "audio/*"
            songsLauncher.launch(intent)
        }
    }

    val songsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            if (it.data != null) {
                val audioUri = it.data!!.data
                val fileName = getFileName(audioUri)
                if (fileName != null) {
                    val storageRef = FirebaseStorage.getInstance().reference.child("songs/$fileName")

                    if (audioUri != null) {
                        storageRef.putFile(audioUri)
                            .addOnSuccessListener {
                                storageRef.downloadUrl.addOnSuccessListener { uri ->
                                    val category = binding.categoryEditText.text.toString()
                                    val songTitle = binding.songTitleEditText.text.toString()

                                    saveSongToFirestore(category, songTitle, uri.toString())
                                }
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(this, "Song Upload Failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "Failed to get file name", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getFileName(uri: Uri?): String? {
        var fileName: String? = null
        uri?.let {
            contentResolver.query(it, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        fileName = cursor.getString(nameIndex)
                    }
                }
            }
        }
        return fileName
    }

    private fun saveSongToFirestore(category: String, title: String, url: String) {
        val db = FirebaseFirestore.getInstance()
        val songRef = db.collection("songs").document()

        val songId = songRef.id

        val songData = mapOf(
            "id" to songId,
            "title" to title,
            "url" to url
        )
        songRef.set(songData)
            .addOnSuccessListener {
                // Add song to category
                addSongToCategory(category, songRef.id)
                Toast.makeText(this, "Song Added to Firestore", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to Add to Firestore: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addSongToCategory(category: String, songId: String) {
        val db = FirebaseFirestore.getInstance()
        val categoryRef = db.collection("category").document(category)

        categoryRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                // Update the existing document
                val songs = document.get("songs") as? List<String> ?: emptyList()
                val updatedSongs = songs.toMutableList().apply {
                    add(songId)
                }
                categoryRef.update("songs", updatedSongs)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Song Added to Category", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Failed to Update Category: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // Create a new document with the song
                val newCategory = mapOf("songs" to listOf(songId))
                categoryRef.set(newCategory)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Song Added to Category", Toast.LENGTH_SHORT).show()
                    }                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Failed to Add to Category: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}