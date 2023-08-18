package cat.teknos.berry.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.teknos.apulidofruites.R;

import java.util.Random;

public class Game extends View {

    public int ancho, alto, posX, posY, radio, posMonedaX, posMonedaY, posMonedaFalsaX, posMonedaFalsaY, puntuacion;
    public float escala;
    private GestureDetector gestos;
    private RectF rectCesta, rectMoneda, rectMonedaFalsa;
    private Random random = new Random();
    private MediaPlayer gameloop = new MediaPlayer();

    public Game(Context context) {
        super(context);
    }

    public Game(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        gameloop = MediaPlayer.create(context, R.raw.gameloop);
        gameloop.start();
        gameloop.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                gameloop.start();
            }
        });
    }

    public Game(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                posX=(int)event.getX();
                radio=50;
                this.invalidate();
        }
        return true;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint fondo = new Paint();
        Paint cesta = new Paint();
        Paint moneda = new Paint();
        Paint monedaFalsa = new Paint();
        Paint puntos = new Paint();

        fondo.setColor(Color.BLACK);
        fondo.setStyle(Paint.Style.FILL_AND_STROKE);
        cesta.setColor(Color.YELLOW);
        cesta.setStyle(Paint.Style.FILL_AND_STROKE);
        moneda.setColor(Color.RED);
        moneda.setStyle(Paint.Style.FILL_AND_STROKE);
        monedaFalsa.setColor(Color.GREEN);
        monedaFalsa.setStyle(Paint.Style.FILL_AND_STROKE);

        puntos.setTextAlign(Paint.Align.RIGHT);
        puntos.setTextSize(100);
        puntos.setColor(Color.WHITE);

        canvas.drawRect(new Rect(0,0,(ancho),(alto)),fondo);

        rectCesta= new RectF((posX-radio),(posY-radio),(posX+radio),(posY+radio));
        canvas.drawOval(rectCesta,cesta);

        if (posMonedaY>alto) {
            posMonedaY=50;
            posMonedaX= random.nextInt(ancho);
        }
        rectMoneda = new RectF((posMonedaX-radio),(posMonedaY-radio), (posMonedaX+radio),(posMonedaY+radio));
        canvas.drawOval(rectMoneda,moneda);

        if (RectF.intersects(rectCesta,rectMoneda)) {
            puntuacion += 1;
            posMonedaY = 50;
            posMonedaX = random.nextInt(ancho);
        }

        if (posMonedaFalsaY>alto) {posMonedaFalsaY=50;
            posMonedaFalsaX= random.nextInt(ancho);
        }
        rectMonedaFalsa = new RectF((posMonedaFalsaX-radio),(posMonedaFalsaY-radio), (posMonedaFalsaX+radio), (posMonedaFalsaY+radio));
        canvas.drawOval(rectMonedaFalsa,monedaFalsa);

        if (RectF.intersects(rectCesta,rectMonedaFalsa)) {
            puntuacion -= 5;
            posMonedaFalsaY=50;
            posMonedaFalsaX= random.nextInt(ancho);
        }

        canvas.drawText(String.valueOf(puntuacion), 150,150,puntos);
    }
}
