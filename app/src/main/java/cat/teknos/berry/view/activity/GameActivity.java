package cat.teknos.berry.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.Timer;
import java.util.TimerTask;

import cat.teknos.berry.view.util.GameEventListener;
import cat.teknos.berry.R;
import cat.teknos.berry.model.PlaylistManager;
import cat.teknos.berry.presenter.GamePresenter;

public class GameActivity extends AppCompatActivity implements GameEventListener {

    private int level, currentSoundResource, newValue;
    private GamePresenter game;
    private final Handler handler = new Handler();
    private PlaylistManager playlistManager;
    private ImageView live1, live2, live3;
    private MediaPlayer mediaPlayer;
    private String playerName;
    private int numLives = 3;
    private final int maxLives = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        game = findViewById(R.id.Screen);
        game.setGameEventListener(this);

        Intent intent = getIntent();
        level = intent.getIntExtra("level", 2);
        playerName = getIntent().getStringExtra("playerName");

        live1 = findViewById(R.id.live1);
        live2 = findViewById(R.id.live2);
        live3 = findViewById(R.id.live3);

        mediaPlayer = MediaPlayer.create(this, R.raw.berry_collected);
        currentSoundResource = R.raw.berry_collected;

        playList();
        obs();
        timer();
    }

    private void playSound(int soundResource) {
        if (currentSoundResource != soundResource) {
            mediaPlayer.reset();
            mediaPlayer = MediaPlayer.create(this, soundResource);
            currentSoundResource = soundResource;
        }
        mediaPlayer.start();
    }

    @Override
    public void onBerryCollected() {
        runOnUiThread(() -> {
            TextView textView = findViewById(R.id.points);
            newValue = Integer.parseInt(textView.getText().toString()) + 1;
            textView.setText(String.valueOf(newValue));
            playSound(R.raw.berry_collected);
        });
    }

    @Override
    public void onRockCollision() {
        if (numLives > 0) {
            numLives--;
            updateLifeIconsVisibility();
            playSound(R.raw.rock_collision);
        }
        if (numLives == 0) {
            onGameFinished();
        }
        playSound(R.raw.rock_collision);
    }

    public void onHeartCollected() {
        runOnUiThread(() -> {
            if (numLives <= maxLives) {
                numLives++;
                updateLifeIconsVisibility();
            }
        });
    }

    private void updateLifeIconsVisibility() {
        live1.setVisibility(numLives >= 1 ? View.VISIBLE : View.INVISIBLE);
        live2.setVisibility(numLives >= 2 ? View.VISIBLE : View.INVISIBLE);
        live3.setVisibility(numLives == 3 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        onGameFinished();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (playlistManager != null) {
            playlistManager.release();
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    private void onGameFinished(){
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("playerScore", newValue);
        intent.putExtra("playerName", playerName);
        startActivity(intent);
        finish();
    }

    private void playList(){
        int[] songResources = {
                R.raw.route_101,
                R.raw.route_104,
                R.raw.route_110,
                R.raw.route_113,
                R.raw.route_119,
                R.raw.route_120,
        };
        playlistManager = new PlaylistManager(this, songResources);
        playlistManager.start();
    }

    private void obs(){
        ViewTreeObserver obs = game.getViewTreeObserver();
        obs.addOnGlobalLayoutListener(() -> {
        });
    }

    private void timer(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    game.posBerryY += level * 10;
                    game.posRockY += level * 10;
                    game.invalidate();
                });
            }
        },0, 20);
    }
}