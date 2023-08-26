package cat.teknos.berry.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewTreeObserver;


import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cat.teknos.berry.R;
import cat.teknos.berry.model.PlaylistManager;
import cat.teknos.berry.presenter.Game;

public class GameActivity extends AppCompatActivity {

    private int level;
    private Game game;
    private final Handler handler = new Handler();
    private final Random random = new Random();
    private PlaylistManager playlistManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        game = findViewById(R.id.Screen);

        Intent intent = getIntent();
        level = intent.getIntExtra("level", 2);

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

        obs();
        timer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (playlistManager != null) {
            playlistManager.release();
        }
    }

    private void obs(){
        ViewTreeObserver obs = game.getViewTreeObserver();
        obs.addOnGlobalLayoutListener(() -> {
            game.width = game.getWidth();
            game.height = game.getHeight();

            game.posPikachuX = game.width / 2;
            game.posPikachuY = game.height - 100;

            game.radio = 100;
            game.posBerryY = 50;
            game.posPokemonX = random.nextInt(game.width);
        });
    }

    private void timer(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    game.posBerryY += level * 10;
                    game.posPokemonY += level * 10;
                    game.invalidate();
                });
            }
        },0, 20);
    }
}