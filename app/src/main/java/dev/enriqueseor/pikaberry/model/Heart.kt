package dev.enriqueseor.pikaberry.model

import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import android.content.Context
import dev.enriqueseor.pikaberry.R

class Heart(var x: Int, var y: Int, context: Context) {
    val rect = RectF()
    var drawable: Drawable? = null

    init {
        drawable = ResourcesCompat.getDrawable(context.resources, R.drawable.heart, null)
    }

    fun draw(canvas: Canvas, radius: Int) {
        drawable?.setBounds(
            x - radius,
            y - radius,
            x + radius,
            y + radius
        )
        drawable?.draw(canvas)

        rect.set(
            (x - radius).toFloat(),
            (y - radius).toFloat(),
            (x + radius).toFloat(),
            (y + radius).toFloat()
        )
    }

    fun updatePosition(canvasWidth: Int, canvasHeight: Int, baseSpeed: Int, level: Int) {
        if (y > canvasHeight) {
            x = (0..canvasWidth).random()
            y = (-17500..-12500).random() * level
        } else {
            y += baseSpeed * level
        }
    }
}