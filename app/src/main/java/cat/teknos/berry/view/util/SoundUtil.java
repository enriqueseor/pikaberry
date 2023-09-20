package cat.teknos.berry.view.util;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundUtil {

    public static MediaPlayer mediaPlayer;
    private static int SoundResource;

    public static void playSound(Context context, int soundResource) {
        if (SoundResource != soundResource) {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(context, soundResource);
            SoundResource = soundResource;
        }
        mediaPlayer.start();
    }

    public static void releasePlaySound(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}