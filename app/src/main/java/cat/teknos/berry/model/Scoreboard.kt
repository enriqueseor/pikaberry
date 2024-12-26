package cat.teknos.berry.model

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable

class Scoreboard(
    private val canvasWidth: Int,
    private val canvasHeight: Int,
    private var score: Int,
    private var lives: Int,
    private val icon: Drawable?,
    private val heartDrawable: Drawable?,
) {
    private val scoreboard = RectF()
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
    }
    private val paintWhite = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    fun updateScore(score: Int, lives: Int) {
        this.score = score
        this.lives = lives
    }

    fun draw(canvas: Canvas) {
        /*****************************************************
         *                     BACKGROUND                    *
         *****************************************************/
        scoreboard.set(
            0f,
            0f,
            canvasWidth.toFloat(),
            canvasHeight * 0.1f
        )
        canvas.drawRect(scoreboard, paintWhite)

        /*****************************************************
         *                     BERRY ICON                    *
         *****************************************************/
        val iconX = canvasWidth * 0.16f / 2
        val iconY = canvasHeight * 0.1f / 2
        icon?.setBounds(
            (iconX - canvasHeight * 0.1f / 2).toInt(),
            (iconY - canvasHeight * 0.1f / 2).toInt(),
            (iconX + canvasHeight * 0.1f / 2).toInt(),
            (iconY + canvasHeight * 0.1f / 2).toInt()
        )
        icon?.draw(canvas)

        /*****************************************************
         *                       SCORE                       *
         *****************************************************/
        val textX = canvasWidth * 0.30f
        val textY = canvasHeight * 0.08f
        textPaint.textSize = canvasHeight * 0.08f
        canvas.drawText(
            "$score",
            textX,
            textY,
            textPaint
        )

        /*****************************************************
         *                    HEARTS ICON                    *
         *****************************************************/
        val heartSize = canvasHeight * 0.07f
        val heartPadding = canvasWidth * 0.001f

        for (i in 0 until lives) {
            heartDrawable?.setBounds(
                (canvasWidth - canvasWidth * 0.03f).toInt() - (heartSize.toInt() + heartPadding.toInt()) * (i + 1),
                (canvasHeight * 0.1f * 0.2f).toInt(),
                (canvasWidth - canvasWidth * 0.03f).toInt() - (heartSize.toInt() + heartPadding.toInt()) * i,
                (canvasHeight * 0.1f * 0.2f + heartSize).toInt()
            )
            heartDrawable?.draw(canvas)
        }
    }
}