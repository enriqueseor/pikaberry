package cat.teknos.berry.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.ViewTreeObserver;


import java.util.Timer;
import java.util.TimerTask;

import cat.teknos.berry.R;
import cat.teknos.berry.game.Game;

public class GameActivity extends AppCompatActivity {

    private Game game;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        game = findViewById(R.id.Screen);
        ViewTreeObserver obs = game.getViewTreeObserver();

        obs.addOnGlobalLayoutListener(() -> {
            game.width = game.getWidth();
            game.height = game.getHeight();
            game.posX=game.width /2;
            game.posY=game.height -100;

            game.radio=100;
            game.posBerryY =50;
            game.posPokemonX =200;
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    game.posBerryY +=10;
                    game.posPokemonY +=10;
                    game.invalidate();
                });
            }
        }, 0, 20);
    }
}