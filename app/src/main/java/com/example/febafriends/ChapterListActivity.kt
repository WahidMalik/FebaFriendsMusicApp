package com.example.febafriends

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.febafriends.databinding.ActivityChapterListBinding

class ChapterListActivity : AppCompatActivity() {

    companion object {
        lateinit var bibleList: Subcategory
    }

    private lateinit var binding: ActivityChapterListBinding
    private lateinit var adapter: BibleAdapter
    private lateinit var originalSongsList: List<String>

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


        originalSongsList = bibleList.bile

        adapter = BibleAdapter(originalSongsList)
        binding.biblerecycleView.adapter = adapter
        binding.biblerecycleView.layoutManager = LinearLayoutManager(this)
        binding.biblerecycleView.adapter=adapter
    }
}
