package cat.teknos.berry.presenter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.util.Random;

import cat.teknos.berry.view.util.GameEventListener;
import cat.teknos.berry.R;

public class GamePresenter extends View {

    public int width, height, radio;
    public int posPikachuX, posPikachuY, posBerryX, posBerryY, posPokemonX, posPokemonY;
    private int currentBerryType = 0;

    private final Random random = new Random();

    private final RectF rectForPikachu = new RectF();
    private final RectF rectForBerry = new RectF();
    private final RectF rectForPokemon = new RectF();

    private Drawable backgroundDrawable;
    private Drawable pikachuDrawable;
    private Drawable pokemonDrawable;

    private Drawable[] berriesDrawable;

    public GamePresenter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setClickable(true);
        backgroundDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.background, null);
        pikachuDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.pikachu, null);
        berriesDrawable = new Drawable[3];
        berriesDrawable[0] = ResourcesCompat.getDrawable(getResources(), R.drawable.razz_berry, null);
        berriesDrawable[1] = ResourcesCompat.getDrawable(getResources(), R.drawable.nanap_berry, null);
        berriesDrawable[2] = ResourcesCompat.getDrawable(getResources(), R.drawable.pinap_berry, null);
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
        berriesDrawable[currentBerryType].setBounds(posBerryX - radio, posBerryY - radio, posBerryX + radio, posBerryY + radio);
        berriesDrawable[currentBerryType].draw(canvas);
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

    private GameEventListener gameEventListener;

    public void setGameEventListener(GameEventListener listener) {
        this.gameEventListener = listener;
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
            if (gameEventListener != null) {
                gameEventListener.onBerryCollected();
            }
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