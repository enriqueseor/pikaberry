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
import cat.teknos.berry.view.util.OnBerryCollectedListener;

public class GamePresenter extends View {

    public int width, height, radius;
    public int posPikachuX, posPikachuY;
    public int posBerryX, posBerryY;
    public int posRockX, posRockY;
    public int posHeartX, posHeartY;
    private int berryType = 0;

    private final RectF rectForPikachu = new RectF();
    private final RectF rectForBerry = new RectF();
    private final RectF rectForRock = new RectF();
    private final RectF rectForHeart = new RectF();

    private Drawable pikachuDrawable;
    private Drawable rockDrawable;
    private Drawable heartDrawable;
    private Drawable[] berriesDrawable;

    private GameEventListener gameEventListener;
    private OnBerryCollectedListener onBerryCollectedListener;
    private final Random random = new Random();

    public GamePresenter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        pikachuDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.pikachu, null);
        berriesDrawable = new Drawable[5];
        berriesDrawable[0] = ResourcesCompat.getDrawable(getResources(), R.drawable.razz_berry, null);
        berriesDrawable[1] = ResourcesCompat.getDrawable(getResources(), R.drawable.pinap_berry, null);
        berriesDrawable[2] = ResourcesCompat.getDrawable(getResources(), R.drawable.nanap_berry, null);
        berriesDrawable[3] = ResourcesCompat.getDrawable(getResources(), R.drawable.pinap_berry_silver, null);
        berriesDrawable[4] = ResourcesCompat.getDrawable(getResources(), R.drawable.razz_berry_golden, null);
        rockDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.rock, null);
        heartDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.heart, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        width = w;
        height = h;

        posPikachuX = width / 2;
        posPikachuY = height - 100;

        radius = 100;
        posBerryX = random.nextInt(width);
        posRockX = random.nextInt(width);
        posHeartX = random.nextInt(width);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            posPikachuX = (int) event.getX();
            this.invalidate();
        }else if (event.getAction() == MotionEvent.ACTION_UP) {
            posPikachuX = (int) event.getX();
            this.invalidate();
            performClick();
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //PIKACHU
        pikachuDrawable.setBounds(posPikachuX - radius, posPikachuY - radius, posPikachuX + radius, posPikachuY + radius);
        pikachuDrawable.draw(canvas);
        rectForPikachu.set(posPikachuX - radius, posPikachuY - radius, posPikachuX + radius, posPikachuY + radius);

        //BERRY
        berriesDrawable[berryType].setBounds(posBerryX - radius, posBerryY - radius, posBerryX + radius, posBerryY + radius);
        berriesDrawable[berryType].draw(canvas);
        rectForBerry.set(posBerryX - radius, posBerryY - radius, posBerryX + radius, posBerryY + radius);
        newBerry();
        onBerryCollected();

        //ROCK
        rockDrawable.setBounds(posRockX - radius, posRockY - radius, posRockX + radius, posRockY + radius);
        rockDrawable.draw(canvas);
        rectForRock.set(posRockX - radius, posRockY - radius, posRockX + radius, posRockY + radius);
        newRock();
        onRockCollision();

        //HEART
        heartDrawable.setBounds(posHeartX - radius, posHeartY - radius, posHeartX + radius, posHeartY + radius);
        heartDrawable.draw(canvas);
        rectForHeart.set(posHeartX - radius, posHeartY - radius, posHeartX + radius, posHeartY + radius);
        onNewHeartGenerated();
        onHeartCollected();
    }

    public void setGameEventListener(GameEventListener listener) {
        this.gameEventListener = listener;
    }
    public void setOnBerryCollectedListener(OnBerryCollectedListener listener) {
        this.onBerryCollectedListener = listener;
    }

    private int customRandomBerryType() {
        double[] probabilities = {0.60, 0.20, 0.10, 0.050, 0.025};
        double rand = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < probabilities.length; i++) {
            cumulativeProbability += probabilities[i];
            if (rand <= cumulativeProbability) {
                return i;
            }
        }
        return 0;
    }

    private void newBerry() {
        if (posBerryY > height) {
            posBerryY = 0;
            posBerryX = random.nextInt(width);
            berryType = customRandomBerryType();
        }
    }

    private void onBerryCollected() {
        if (RectF.intersects(rectForPikachu, rectForBerry)) {
            posBerryY = 0;
            posBerryX = random.nextInt(width);
            if (onBerryCollectedListener != null) {
                onBerryCollectedListener.onBerryCollected(berryType);
            }
            berryType = customRandomBerryType();
        }
    }

    private void newRock() {
        if (posRockY > height) {
            posRockY = 0;
            posRockX = random.nextInt(width);
        }
    }

    private void onRockCollision() {
        if (RectF.intersects(rectForPikachu, rectForRock)) {
            posRockY = 0;
            posRockX = random.nextInt(width);
            if (gameEventListener != null) {
                gameEventListener.onRockCollision();
            }
        }
    }

    private void onNewHeartGenerated() {
        if (posHeartY > height) {
            posHeartY = 0;
            posHeartX = random.nextInt(width);
            if (gameEventListener != null) {
                gameEventListener.onNewHeartGenerated();
            }
        }
    }

    public void onHeartCollected() {
        if (RectF.intersects(rectForPikachu, rectForHeart)) {
            posHeartY = 0;
            posHeartX = random.nextInt(width);
            if (gameEventListener != null) {
                gameEventListener.onHeartCollected();
            }
        }
    }
}