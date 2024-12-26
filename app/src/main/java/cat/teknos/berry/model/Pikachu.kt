package cat.teknos.berry.model

import android.graphics.RectF

class Pikachu(private var x: Int, private var y: Int, private val radius: Int) {
    val rect = RectF()

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
}