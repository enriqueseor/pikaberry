package cat.teknos.berry.model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import cat.teknos.berry.R

class Scoreboard(
    private val canvasWidth: Int,
    private val canvasHeight: Int,
    private var score: Int,
    private var lives: Int,
    context: Context?,
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

    private val berryDrawable: Drawable? = ResourcesCompat.getDrawable(context!!.resources, R.drawable.berry, null)
    private val heartDrawable: Drawable? = ResourcesCompat.getDrawable(context!!.resources, R.drawable.heart, null)

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
        berryDrawable?.let {
            val iconSize = canvasHeight * 0.07f
            val iconX = canvasWidth * 0.05f
            val iconY = canvasHeight * 0.05f
            it.setBounds(
                (iconX - iconSize / 2).toInt(),
                (iconY - iconSize / 2).toInt(),
                (iconX + iconSize / 2).toInt(),
                (iconY + iconSize / 2).toInt()
            )
            it.draw(canvas)
        }

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