package com.example.febafriends

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.febafriends.databinding.ActivityAdminPanelBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

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

        binding.radioButtonSong.setOnClickListener {
            binding.bibleUploadLayout.visibility = View.GONE
            binding.songUploadLayout.visibility = View.VISIBLE
        }

        binding.radioButtonBible.setOnClickListener {
            binding.songUploadLayout.visibility = View.GONE
            binding.bibleUploadLayout.visibility = View.VISIBLE
        }







        binding.toolbarAdminPanel.setNavigationOnClickListener {
            onBackPressed()
        }

        val categories = arrayOf("Urdu", "Sindhi", "Punjabi","English","Pashto","Hamd-o-sana","Sindhi","Siraiki")
        val bibles = arrayOf("Matthew", "Mark", "Luke", "John", "Acts", "Romans", "1Corinthians", "2Corinthians", "Galatians",
            "Ephesians", "Philippians", "Colossians", "1Thessalonians", "2Thessalonians", "1Timothy", "2Timothy", "Titus", "Philemon",
            "Hebrews", "James", "1Peter", "2Peter", "1John", "2John", "3John", "Jude", "Revelation")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter

        val bibleadapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bibles)
        bibleadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.bibleSpinner.adapter = bibleadapter


        binding.uploadSongs.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "audio/*"
            songsLauncher.launch(intent)
        }

        binding.uploadBible.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "audio/*"
            bibleLauncher.launch(intent)
        }
    }

    val songsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            it.data?.data?.let { audioUri ->
                val fileName = getFileName(audioUri)
                if (fileName != null) {
                    val storageRef = FirebaseStorage.getInstance().reference.child("songs/$fileName")

                    storageRef.putFile(audioUri)
                        .addOnSuccessListener {
                            storageRef.downloadUrl.addOnSuccessListener { uri ->
                                val title = binding.songTitleEditText.text.toString()
                                val songId = UUID.randomUUID().toString()
                                saveSongToFirestore(songId, title, uri.toString())
                            }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Song Upload Failed: ${exception.message}", Toast.LENGTH_SHORT).show()
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

    private fun saveSongToFirestore(id: String, title: String, url: String) {
        val db = FirebaseFirestore.getInstance()
        val songData = mapOf(
            "id" to id,
            "title" to title,
            "url" to url
        )


        db.collection("songs").document(id).set(songData)
            .addOnSuccessListener {
                Toast.makeText(this, "Song Added to Firestore", Toast.LENGTH_SHORT).show()

                val selectedCategory = binding.categorySpinner.selectedItem.toString()

                val categoryRef = db.collection("category").document(selectedCategory)

                categoryRef.update("songs", FieldValue.arrayUnion(id))
                    .addOnSuccessListener {
                        Toast.makeText(this, "Song added to category '$selectedCategory'", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Failed to add song to category: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to Add to Firestore: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    val bibleLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            it.data?.data?.let { audioUri ->
                val fileName = getFileName(audioUri)
                if (fileName != null) {
                    val storageRef =
                        FirebaseStorage.getInstance().reference.child("songs/$fileName")

                    storageRef.putFile(audioUri)
                        .addOnSuccessListener {
                            storageRef.downloadUrl.addOnSuccessListener { uri ->
                                val title = binding.bibleEditText.text.toString()
                                val songId = UUID.randomUUID().toString()
                                saveBibleToFirestore(songId, title, uri.toString())
                            }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                this,
                                "Bible Upload Failed: ${exception.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(this, "Failed to get file name", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveBibleToFirestore(id: String, title: String, url: String) {
        val db = FirebaseFirestore.getInstance()
        val songData = mapOf(
            "id" to id,
            "title" to title,
            "url" to url
        )


        db.collection("songs").document(id).set(songData)
            .addOnSuccessListener {
                Toast.makeText(this, "Song Added to Firestore", Toast.LENGTH_SHORT).show()

                val selectedCategory = binding.bibleSpinner.selectedItem.toString()

                val categoryRef = db.collection("Bible").document(selectedCategory)

                categoryRef.update("songs", FieldValue.arrayUnion(id))
                    .addOnSuccessListener {
                        Toast.makeText(this, "Bible added to category '$selectedCategory'", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Failed to add bible to category: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to Add to Firestore: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
