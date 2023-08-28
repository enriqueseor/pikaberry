package cat.teknos.berry.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewTreeObserver;
import android.widget.TextView;


import java.util.Timer;
import java.util.TimerTask;

import cat.teknos.berry.view.util.GameEventListener;
import cat.teknos.berry.R;
import cat.teknos.berry.model.PlaylistManager;
import cat.teknos.berry.presenter.GamePresenter;

public class GameActivity extends AppCompatActivity implements GameEventListener {

    private int level;
    private GamePresenter game;
    private final Handler handler = new Handler();
    private PlaylistManager playlistManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        game = findViewById(R.id.Screen);
        game.setGameEventListener(this);

        Intent intent = getIntent();
        level = intent.getIntExtra("level", 2);

        playList();
        obs();
        timer();
    }

    @Override
    public void onBerryCollected() {
        runOnUiThread(() -> {
            TextView textView = findViewById(R.id.points);
            int currentValue = Integer.parseInt(textView.getText().toString());
            int newValue = currentValue + 1;
            textView.setText(String.valueOf(newValue));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (playlistManager != null) {
            playlistManager.release();
        }
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