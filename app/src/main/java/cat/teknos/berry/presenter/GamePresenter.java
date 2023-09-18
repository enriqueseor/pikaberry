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

public class GamePresenter extends View {

    public int width, height, radio;
    public int posPikachuX, posPikachuY;
    public int posBerryX, posBerryY;
    public int posRockX, posRockY;
    public int posHeartX, posHeartY;
    private int currentBerryType = 0;
    int canvasWidth, canvasHeight;
    float aspectRatio, imageAspectRatio;

    private final Random random = new Random();

    Rect srcRect = new Rect();

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

    public GamePresenter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        backgroundDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.background_emerald, null);
        pikachuDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.pikachu, null);
        berriesDrawable = new Drawable[3];
        berriesDrawable[0] = ResourcesCompat.getDrawable(getResources(), R.drawable.razz_berry, null);
        berriesDrawable[1] = ResourcesCompat.getDrawable(getResources(), R.drawable.nanap_berry, null);
        berriesDrawable[2] = ResourcesCompat.getDrawable(getResources(), R.drawable.pinap_berry, null);
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

        radio = 100;
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
        rockDrawable.setBounds(posRockX - radio, posRockY - radio, posRockX + radio, posRockY + radio);
        rockDrawable.draw(canvas);
        rectForRock.set(posRockX - radio, posRockY - radio, posRockX + radio, posRockY + radio);
        newRock();
        onRockCollision();

        heartDrawable.setBounds(posHeartX - radio, posHeartY - radio, posHeartX + radio, posHeartY + radio);
        heartDrawable.draw(canvas);
        rectForHeart.set(posHeartX - radio, posHeartY - radio, posHeartX + radio, posHeartY + radio);
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
            currentBerryType = random.nextInt(3);
            if (gameEventListener != null) {
                gameEventListener.onBerryCollected();
            }
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