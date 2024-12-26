package cat.teknos.berry.model

import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import android.content.Context
import cat.teknos.berry.R

class Heart(var x: Int, var y: Int, val radius: Int, context: Context) {
    val rect = RectF()
    private var heartDrawable: Drawable? = null

    init {
        heartDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.heart, null)
    }

    fun updatePosition(newX: Int, canvasWidth: Int) {
        x = newX.coerceIn(radius, canvasWidth - radius)
    }

    fun setBounds() {
        rect.set(
            (x - radius).toFloat(),
            (y - radius).toFloat(),
            (x + radius).toFloat(),
            (y + radius).toFloat()
        )
    }

    fun draw(canvas: Canvas) {
        heartDrawable?.setBounds(
            (x - radius),
            (y - radius),
            (x + radius),
            (y + radius)
        )
        heartDrawable?.draw(canvas)
    }
}