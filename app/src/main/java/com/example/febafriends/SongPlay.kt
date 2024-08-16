package com.example.febafriends

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.media3.exoplayer.ExoPlayer
import com.example.febafriends.databinding.ActivitySongPlayBinding

class SongPlay : AppCompatActivity() {

    lateinit var binding: ActivitySongPlayBinding
    lateinit var exoPlayer: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySongPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        setSupportActionBar(binding.toolbarSongPlay)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbarSongPlay.navigationIcon?.setTint(ContextCompat.getColor(this, android.R.color.holo_red_dark))

        Exoplayer.currentSong?.apply {
            binding.toolbarplaysongname.text = title
            exoPlayer = Exoplayer.getInstance()!!
            binding.pViewlayer.player = exoPlayer
        }
    }
}
