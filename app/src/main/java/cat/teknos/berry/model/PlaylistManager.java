package cat.teknos.berry.model;

import android.content.Context;
import android.media.MediaPlayer;

public class PlaylistManager implements MediaPlayer.OnCompletionListener {
    private MediaPlayer[] playlist;
    private int currentSongIndex = 0;

    public PlaylistManager(Context context, int[] songResources) {
        initPlaylist(context, songResources);
    }

    private void initPlaylist(Context context, int[] songResources) {
        playlist = new MediaPlayer[songResources.length];

        for (int i = 0; i < songResources.length; i++) {
            playlist[i] = MediaPlayer.create(context, songResources[i]);
            playlist[i].setOnCompletionListener(this);
        }
    }

    public void start() {
        playCurrentSong();
    }

    public void release() {
        for (MediaPlayer mediaPlayer : playlist) {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
        }
    }

    private void playCurrentSong() {
        if (currentSongIndex >= 0 && currentSongIndex < playlist.length) {
            MediaPlayer currentSong = playlist[currentSongIndex];
            if (currentSong != null) {
                currentSong.start();
            }
        }
    }

    private void playNextSong() {
        currentSongIndex = (currentSongIndex + 1) % playlist.length;
        playCurrentSong();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playNextSong();
    }
}