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

    fun startPlaying(context: Context, bibleData: Bibledata?) {
        val songData = convertBibleDataToSongData(bibleData)
        startPlaying(context, songData)
    }

    private fun convertBibleDataToSongData(bibleData: Bibledata?): SongsData? {
        return bibleData?.let {
            SongsData(
                id = it.id,
                title = it.title,
                url = it.url

            )
        }
    }
}
