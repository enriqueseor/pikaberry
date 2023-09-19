package cat.teknos.berry.presenter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
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
    protected int canvasWidth, canvasHeight;
    float aspectRatio, imageAspectRatio;

    public Rect srcRect = new Rect();
    private final RectF rectForPikachu = new RectF();
    private final RectF rectForBerry = new RectF();
    private final RectF rectForRock = new RectF();
    private final RectF rectForHeart = new RectF();

    private Drawable backgroundDrawable;
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
        backgroundDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.background_emerald, null);
        pikachuDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.pikachu, null);
        berriesDrawable = new Drawable[5];
        berriesDrawable[0] = ResourcesCompat.getDrawable(getResources(), R.drawable.pinap_berry, null);
        berriesDrawable[1] = ResourcesCompat.getDrawable(getResources(), R.drawable.nanap_berry, null);
        berriesDrawable[2] = ResourcesCompat.getDrawable(getResources(), R.drawable.razz_berry, null);
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
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //BACKGROUND
        drawBackgroundWithAspectRatio(canvas);

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

        //POKEMON
        rockDrawable.setBounds(posRockX - radius, posRockY - radius, posRockX + radius, posRockY + radius);
        rockDrawable.draw(canvas);
        rectForRock.set(posRockX - radius, posRockY - radius, posRockX + radius, posRockY + radius);
        newRock();
        onRockCollision();

        heartDrawable.setBounds(posHeartX - radius, posHeartY - radius, posHeartX + radius, posHeartY + radius);
        heartDrawable.draw(canvas);
        rectForHeart.set(posHeartX - radius, posHeartY - radius, posHeartX + radius, posHeartY + radius);
        newHeart();
        onHeartCollected();
    }

    private void drawBackgroundWithAspectRatio(Canvas canvas) {
        if (backgroundDrawable == null) {
            return;
        }

        int srcLeft = 0;
        int srcTop = 0;
        int srcRight = backgroundDrawable.getIntrinsicWidth();
        int srcBottom = backgroundDrawable.getIntrinsicHeight();

        canvasWidth = getWidth();
        canvasHeight = getHeight();

        aspectRatio = (float) canvasWidth / canvasHeight;
        imageAspectRatio = (float) srcRight / srcBottom;

        if (aspectRatio > imageAspectRatio) {
            int newHeight = (int) (canvasWidth / imageAspectRatio);
            int topOffset = (canvasHeight - newHeight) / 2;
            srcTop += topOffset;
            srcBottom = srcTop + newHeight;
        } else {
            int newWidth = (int) (canvasHeight * imageAspectRatio);
            int leftOffset = (canvasWidth - newWidth) / 2;
            srcLeft += leftOffset;
            srcRight = srcLeft + newWidth;
        }

        srcRect.set(srcLeft, srcTop, srcRight, srcBottom);
        backgroundDrawable.setBounds(srcRect);
        backgroundDrawable.draw(canvas);
    }

    public void setGameEventListener(GameEventListener listener) {
        this.gameEventListener = listener;
    }
    public void setOnBerryCollectedListener(OnBerryCollectedListener listener) {
        this.onBerryCollectedListener = listener;
    }

    private void newBerry() {
        if (posBerryY > height) {
            posBerryY = 50;
            posBerryX = random.nextInt(width);
        }
    }

    private void onBerryCollected() {
        if (RectF.intersects(rectForPikachu, rectForBerry)) {
            posBerryY = 50;
            posBerryX = random.nextInt(width);
            if (onBerryCollectedListener != null) {
                onBerryCollectedListener.onBerryCollected(berryType);
            }
            berryType = random.nextInt(5);
        }
    }

    private void newRock() {
        if (posRockY > height) {
            posRockY = 50;
            posRockX = random.nextInt(width);
        }
    }

    private void onRockCollision() {
        if (RectF.intersects(rectForPikachu, rectForRock)) {
            posRockY = 50;
            posRockX = random.nextInt(width);
            if (gameEventListener != null) {
                gameEventListener.onRockCollision();
            }
        }
    }

    private void newHeart() {
        if (posHeartY > height) {
            posHeartY = 50;
            posHeartX = random.nextInt(width);
        }
    }

    public void onHeartCollected() {
        if (RectF.intersects(rectForPikachu, rectForHeart)) {
            posHeartY = 50;
            posHeartX = random.nextInt(width);
            if (gameEventListener != null) {
                gameEventListener.onHeartCollected();
            }
        }
    }
}