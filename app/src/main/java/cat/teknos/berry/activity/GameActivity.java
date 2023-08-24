package cat.teknos.berry.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewTreeObserver;


import java.util.Timer;
import java.util.TimerTask;

import cat.teknos.berry.R;
import cat.teknos.berry.game.Game;

public class GameActivity extends AppCompatActivity {

    private int level;
    private Game game;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        game = findViewById(R.id.Screen);

        Intent intent = getIntent();
        level = intent.getIntExtra("level", 1);

        obs();
        timer();
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
            game.posPokemonX = 200;
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