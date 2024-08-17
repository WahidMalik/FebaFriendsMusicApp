package com.example.febafriends

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.febafriends.databinding.ActivityBibleChapterBinding

class BibleChapter : AppCompatActivity() {
    companion object {
        lateinit var categoryBible: CategoryModel
    }
    private lateinit var binding: ActivityBibleChapterBinding
    private lateinit var bibleAdapter: SongsAdapter
    private lateinit var originalSongsList: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBibleChapterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        originalSongsList = categoryBible.songs

        setSupportActionBar(binding.toolbarChapterlist)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView)
        binding.toolbarChapterlist.navigationIcon?.setTint(resources.getColor(android.R.color.holo_red_dark))
        windowInsetsController?.isAppearanceLightStatusBars = true

        binding.toolbarchapterlist.text = categoryBible.name
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        Toast.makeText(this, "Data size: ${originalSongsList.size}", Toast.LENGTH_SHORT).show()
        bibleAdapter = SongsAdapter(originalSongsList)
        binding.chapterrecycleView.layoutManager = LinearLayoutManager(this)
        binding.chapterrecycleView.adapter = bibleAdapter
    }
}
