package cat.teknos.berry.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;


import java.util.Random;
import cat.teknos.berry.R;

public class Game extends View {

    public int width, height;
    public int radio, punctuation;
    public int posX, posY, posBerryX, posBerryY, posPokemonX, posPokemonY;
    private int currentBerryType = 0;

    private final Random random = new Random();
    private MediaPlayer gameloop;

    private final RectF rectForPikachu = new RectF();
    private final RectF rectForBerry = new RectF();
    private final RectF rectForPokemon = new RectF();

    private final Paint pointsPaint = new Paint();

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

        backgroundDrawable = getResources().getDrawable(R.drawable.background);
        pikachuDrawable = getResources().getDrawable(R.drawable.pikachu);
        berryDrawable = new Drawable[3];
        berryDrawable[0] = getResources().getDrawable(R.drawable.razz_berry);
        berryDrawable[1] = getResources().getDrawable(R.drawable.nanap_berry);
        berryDrawable[2] = getResources().getDrawable(R.drawable.pinap_berry);
        pokemonDrawable = getResources().getDrawable(R.drawable.cherubi);

        pointsPaint.setTextAlign(Paint.Align.RIGHT);
        pointsPaint.setTextSize(100);
        pointsPaint.setColor(Color.WHITE);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            posX = (int) event.getX();
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
        pikachuDrawable.setBounds(posX - radio, posY - radio, posX + radio, posY + radio);
        pikachuDrawable.draw(canvas);
        rectForPikachu.set(posX - radio, posY - radio, posX + radio, posY + radio);

        //BERRY
        berryDrawable[currentBerryType].setBounds(posBerryX - radio, posBerryY - radio, posBerryX + radio, posBerryY + radio);
        berryDrawable[currentBerryType].draw(canvas);
        rectForBerry.set(posBerryX - radio, posBerryY - radio, posBerryX + radio, posBerryY + radio);
        updateBerry();

        //CHERUBI
        pokemonDrawable.setBounds(posPokemonX - radio, posPokemonY - radio, posPokemonX + radio, posPokemonY + radio);
        pokemonDrawable.draw(canvas);
        rectForPokemon.set(posPokemonX - radio, posPokemonY - radio, posPokemonX + radio, posPokemonY + radio);
        updateCherubi();

        //PUNCTUATION
        canvas.drawText(String.valueOf(punctuation), 150,150, pointsPaint);
    }

    private void updateBerry() {
        if (posBerryY > height) {
            posBerryY = 50;
            posBerryX = random.nextInt(width);
        }
        if (RectF.intersects(rectForPikachu, rectForBerry)) {
            punctuation += 1;
            posBerryY = 50;
            posBerryX = random.nextInt(width);
            currentBerryType = random.nextInt(3);
        }
    }

    private void updateCherubi() {
        if (posPokemonY > height) {
            posPokemonY = 50;
            posPokemonX = random.nextInt(width);
        }
        if (RectF.intersects(rectForPikachu, rectForPokemon)) {
            punctuation -= 1;
            posPokemonY = 50;
            posPokemonX = random.nextInt(width);
        }
    }
}