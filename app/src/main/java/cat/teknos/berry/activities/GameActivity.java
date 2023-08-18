package cat.teknos.berry.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.ViewTreeObserver;

import com.teknos.berry.R;

import java.util.Timer;
import java.util.TimerTask;

import cat.teknos.berry.game.Game;

public class GameActivity extends AppCompatActivity {

    private Game game;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        game = (Game) findViewById(R.id.Pantalla);
        ViewTreeObserver obs = game.getViewTreeObserver();

        obs.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                game.ancho = game.getWidth();
                game.alto = game.getHeight();
                game.posX=game.ancho/2;
                game.posY=game.alto-50;

                game.radio=50;
                game.posMonedaY=50;
                game.posMonedaFalsaX=200;
            }
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        game.posMonedaY+=10;
                        game.posMonedaFalsaY+=10;
                        game.invalidate();
                    }
                });
            }
        }, 0, 20);
    }
}