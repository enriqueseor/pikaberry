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

    public int width, height, posX, posY, radio, posBerryX, posBerryY, posCherubiX, posCherubiY, punctuation;
    private final Random random = new Random();
    private MediaPlayer gameloop = new MediaPlayer();

    private final RectF rectForPikachu = new RectF();
    private final RectF rectForBerry = new RectF();
    private final RectF rectForCherubi = new RectF();

    private final Paint berryPaint = new Paint();
    private final Paint monedaFalsaPaint = new Paint();
    private final Paint pointsPaint = new Paint();

    private Drawable pikachuDrawable;

    public Game(Context context) {
        super(context);
        init();
    }

    public Game(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        gameloop = MediaPlayer.create(context, R.raw.gameloop);
        gameloop.start();
        gameloop.setOnCompletionListener(mp -> gameloop.start());
        init();
    }

    private void init() {
        setClickable(true);

        pikachuDrawable = getResources().getDrawable(R.drawable.pikachu);

        berryPaint.setColor(Color.GREEN);
        berryPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        monedaFalsaPaint.setColor(Color.RED);
        monedaFalsaPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        pointsPaint.setTextAlign(Paint.Align.RIGHT);
        pointsPaint.setTextSize(100);
        pointsPaint.setColor(Color.WHITE);
    }

    public Game(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        Drawable backgroundDrawable = getResources().getDrawable(R.drawable.background);
        backgroundDrawable.setBounds(0, 0, getWidth(), getHeight());
        backgroundDrawable.draw(canvas);

        //PIKACHU
        pikachuDrawable.setBounds(posX - radio, posY - radio, posX + radio, posY + radio);
        pikachuDrawable.draw(canvas);
        rectForPikachu.set(posX - radio, posY - radio, posX + radio, posY + radio);

        //BERRY
        if (posBerryY > height) {
            posBerryY =50;
            posBerryX = random.nextInt(width);
        }
        rectForBerry.set(posBerryX - radio, posBerryY - radio, posBerryX + radio, posBerryY + radio);
        canvas.drawOval(rectForBerry, berryPaint);

        if (RectF.intersects(rectForPikachu, rectForBerry)) {
            punctuation += 1;
            posBerryY = 50;
            posBerryX = random.nextInt(width);
        }

        //CHERUBI
        if (posCherubiY > height) {
            posCherubiY =50;
            posCherubiX = random.nextInt(width);
        }
        rectForCherubi.set(posCherubiX - radio, posCherubiY - radio, posCherubiX + radio, posCherubiY + radio);
        canvas.drawOval(rectForCherubi, monedaFalsaPaint);

        if (RectF.intersects(rectForPikachu, rectForCherubi)) {
            punctuation -= 1;
            posCherubiY =50;
            posCherubiX = random.nextInt(width);
        }

        //PUNCTUATION
        canvas.drawText(String.valueOf(punctuation), 150,150, pointsPaint);
    }
}