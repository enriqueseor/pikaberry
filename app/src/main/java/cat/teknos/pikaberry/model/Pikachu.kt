package cat.teknos.pikaberry.model

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import cat.teknos.pikaberry.R

class Pikachu(private var x: Int, private var y: Int, private val radius: Int, context: Context) {
    val rect = RectF()
    private var pikachuDrawable: Drawable? = null

    init {
        pikachuDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.pikachu, null)
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
        pikachuDrawable?.setBounds(
            (x - radius),
            (y - radius),
            (x + radius),
            (y + radius)
        )
        pikachuDrawable?.draw(canvas)
    }
}