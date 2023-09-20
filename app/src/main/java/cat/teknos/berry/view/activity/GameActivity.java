package cat.teknos.berry.view.activity;

import static cat.teknos.berry.view.util.SoundUtil.mediaPlayer;

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
import cat.teknos.berry.view.util.OnBerryCollectedListener;
import cat.teknos.berry.view.util.SoundUtil;

public class GameActivity extends AppCompatActivity implements GameEventListener, OnBerryCollectedListener {

    private int level, score;
    private GamePresenter game;
    private final Handler handler = new Handler();
    private PlaylistManager playlistManager;
    private ImageView live1, live2, live3;
    private String playerName;
    private int numLives = 3;
    private final int maxLives = 3;
    private Timer heartTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        game = findViewById(R.id.Screen);
        game.setGameEventListener(this);
        game.setOnBerryCollectedListener(this);

        Intent intent = getIntent();
        level = intent.getIntExtra("level", 2);
        playerName = getIntent().getStringExtra("playerName");

        live1 = findViewById(R.id.live1);
        live2 = findViewById(R.id.live2);
        live3 = findViewById(R.id.live3);

        SoundUtil.playSound(this, R.raw.berry_collected);

        playList();
        obs();
        timer();
        delayedHeartTimer();
    }

    @Override
    public void onBerryCollected(int berryType) {
        runOnUiThread(() -> {
            TextView textView = findViewById(R.id.points);
            int berryPoints = 0;
            switch (berryType) {
                case 0:
                    berryPoints = 1;
                    break;
                case 1:
                    berryPoints = 2;
                    break;
                case 2:
                    berryPoints = 3;
                    break;
                case 3:
                    berryPoints = 5;
                    break;
                case 4:
                    berryPoints = 10;
                    break;
            }
            int points = Integer.parseInt(textView.getText().toString());
            score = points + berryPoints;
            textView.setText(String.valueOf(score));
            SoundUtil.playSound(this, R.raw.berry_collected);
        });
    }

    @Override
    public void onRockCollision() {
        if (numLives > 0) {
            numLives--;
            updateLifeIconsVisibility();
            SoundUtil.playSound(this, R.raw.rock_collision);
        }
        if (numLives == 0) {
            onGameFinished();
        }
        SoundUtil.playSound(this, R.raw.rock_collision);
    }

    public void onHeartCollected() {
        runOnUiThread(() -> {
            if (numLives < maxLives) {
                numLives++;
                updateLifeIconsVisibility();
            }
        });
        SoundUtil.playSound(this, R.raw.heart_collected);
        stopHeartTimer();
    }

    private void stopHeartTimer() {
        if (heartTimer != null) {
            heartTimer.cancel();
            heartTimer = null;
        }
    }

    private void updateLifeIconsVisibility() {
        live1.setVisibility(numLives >= 1 ? View.VISIBLE : View.INVISIBLE);
        live2.setVisibility(numLives >= 2 ? View.VISIBLE : View.INVISIBLE);
        live3.setVisibility(numLives == 3 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayerRelease();
        onGameFinished();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayerRelease();
    }

    private void mediaPlayerRelease(){
        if (playlistManager != null) {
            playlistManager.release();
        }
        SoundUtil.releasePlaySound(mediaPlayer);
    }

    private void onGameFinished(){
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("playerScore", score);
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

    private void heartTimer() {
        if (heartTimer != null) {
            heartTimer.cancel();
        }
        heartTimer = new Timer();
        heartTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    game.posHeartY += level * 10;
                    game.invalidate();
                });
            }
        }, 0, 20);
    }

    private void delayedHeartTimer() {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            heartTimer();
            delayedHeartTimer();
        }, 10000);
    }
}