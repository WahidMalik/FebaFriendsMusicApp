package com.example.febafriends

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.febafriends.databinding.ActivityChapterListBinding

class ChapterListActivity : AppCompatActivity() {

    companion object {
        lateinit var bibleList: Subcategory
    }

    lateinit var binding: ActivityChapterListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChapterListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarChapter)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.bible.text = bibleList.name
        binding.toolbarChapter.navigationIcon?.setTint(resources.getColor(android.R.color.holo_red_dark))

    }
}