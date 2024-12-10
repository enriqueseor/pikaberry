package cat.teknos.berry.model

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener

class PlaylistManager(context: Context, songResources: IntArray) : OnCompletionListener {
    private lateinit var playlist: Array<MediaPlayer?>
    private var currentSongIndex = 0

    init {
        initPlaylist(context, songResources)
    }

    private fun initPlaylist(context: Context, songResources: IntArray) {
        playlist = arrayOfNulls(songResources.size)

        for (i in songResources.indices) {
            playlist[i] = MediaPlayer.create(context, songResources[i])
            val volume = 0.5f
            playlist[i]?.setVolume(volume, volume)
            playlist[i]?.setOnCompletionListener(this)
        }
    }

    fun start() {
        playCurrentSong()
    }

    fun pause() {
        pauseCurrentSong()
    }

    private fun pauseCurrentSong() {
        if (currentSongIndex >= 0 && currentSongIndex < playlist.size) {
            val currentSong = playlist[currentSongIndex]
            if (currentSong != null && currentSong.isPlaying) {
                currentSong.pause()
            }
        }
    }

    fun release() {
        for (mediaPlayer in playlist) {
            mediaPlayer?.release()
        }
    }

    private fun playCurrentSong() {
        if (currentSongIndex >= 0 && currentSongIndex < playlist.size) {
            val currentSong = playlist[currentSongIndex]
            currentSong?.start()
        }
    }

    private fun playNextSong() {
        currentSongIndex = (currentSongIndex + 1) % playlist.size
        playCurrentSong()
    }

    override fun onCompletion(mp: MediaPlayer) {
        playNextSong()
    }
}