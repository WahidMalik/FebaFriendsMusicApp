package com.example.febafriends

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

object Exoplayer {
    private var exoPlayer: ExoPlayer? = null
    var currentSong: SongsData? = null
        private set

    fun getInstance(): ExoPlayer? {
        return exoPlayer
    }

    fun startPlaying(context: Context, song: SongsData?) {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }

        currentSong = song
        if (song != null) {
            song.url?.let { url ->
                val mediaItem = MediaItem.fromUri(url)
                exoPlayer?.setMediaItem(mediaItem)
                exoPlayer?.prepare()
                exoPlayer?.play()
            }
        }
    }




}
