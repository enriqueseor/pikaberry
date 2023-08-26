package cat.teknos.berry.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;


import java.util.Random;
import cat.teknos.berry.R;

public class Game extends View {

    public int width, height, radio;
    public int posPikachuX, posPikachuY, posBerryX, posBerryY, posPokemonX, posPokemonY;
    private int currentBerryType = 0;

    private final Random random = new Random();
    private MediaPlayer gameloop;

    private final RectF rectForPikachu = new RectF();
    private final RectF rectForBerry = new RectF();
    private final RectF rectForPokemon = new RectF();

    private Drawable backgroundDrawable;
    private Drawable pikachuDrawable;
    private Drawable[] berryDrawable;
    private Drawable pokemonDrawable;

    public Game(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        gameloop = new MediaPlayer();
        gameloop = MediaPlayer.create(context, R.raw.route_101);
        gameloop.start();
        gameloop.setOnCompletionListener(mp -> gameloop.start());
        init();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (gameloop != null) {
            gameloop.release();
            gameloop = null;
        }
    }

    private void init() {
        setClickable(true);
        backgroundDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.background, null);
        pikachuDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.pikachu, null);
        berryDrawable = new Drawable[3];
        berryDrawable[0] = ResourcesCompat.getDrawable(getResources(), R.drawable.razz_berry, null);
        berryDrawable[1] = ResourcesCompat.getDrawable(getResources(), R.drawable.nanap_berry, null);
        berryDrawable[2] = ResourcesCompat.getDrawable(getResources(), R.drawable.pinap_berry, null);
        pokemonDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.cherubi, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            posPikachuX = (int) event.getX();
            this.invalidate();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //BACKGROUND
        backgroundDrawable.setBounds(0, 0, getWidth(), getHeight());
        backgroundDrawable.draw(canvas);

        //PIKACHU
        pikachuDrawable.setBounds(posPikachuX - radio, posPikachuY - radio, posPikachuX + radio, posPikachuY + radio);
        pikachuDrawable.draw(canvas);
        rectForPikachu.set(posPikachuX - radio, posPikachuY - radio, posPikachuX + radio, posPikachuY + radio);

        //BERRY
        berryDrawable[currentBerryType].setBounds(posBerryX - radio, posBerryY - radio, posBerryX + radio, posBerryY + radio);
        berryDrawable[currentBerryType].draw(canvas);
        rectForBerry.set(posBerryX - radio, posBerryY - radio, posBerryX + radio, posBerryY + radio);
        newBerry();
        onBerryCollected();

        //POKEMON
        pokemonDrawable.setBounds(posPokemonX - radio, posPokemonY - radio, posPokemonX + radio, posPokemonY + radio);
        pokemonDrawable.draw(canvas);
        rectForPokemon.set(posPokemonX - radio, posPokemonY - radio, posPokemonX + radio, posPokemonY + radio);
        newPokemon();
        onPokemonCollision();
    }

    private void newBerry(){
        if (posBerryY > height) {
            posBerryY = 50;
            posBerryX = random.nextInt(width);
        }
    }

    private void onBerryCollected() {
        if (RectF.intersects(rectForPikachu, rectForBerry)) {
            posBerryY = 50;
            posBerryX = random.nextInt(width);
            currentBerryType = random.nextInt(3);
        }
    }

    private void newPokemon() {
        if (posPokemonY > height) {
            posPokemonY = 50;
            posPokemonX = random.nextInt(width);
        }
    }

    private void onPokemonCollision() {
        if (RectF.intersects(rectForPikachu, rectForPokemon)) {
            posPokemonY = 50;
            posPokemonX = random.nextInt(width);
        }
    }
}