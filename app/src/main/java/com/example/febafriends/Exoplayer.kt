package com.example.febafriends

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

object Exoplayer {
    private var exoPlayer: ExoPlayer? = null
    private var currentsong: SongsData? = null

    fun getInstance(): ExoPlayer? {
        return exoPlayer
    }

    fun currentSong(): SongsData? {
        return currentsong
    }

    fun startPlaying(context: Context, song: SongsData) {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }

        currentsong = song
        song.url?.let { url ->
            val mediaItem = MediaItem.fromUri(url)
            exoPlayer?.setMediaItem(mediaItem)
            exoPlayer?.prepare()
            exoPlayer?.play()
        }
    }
}
