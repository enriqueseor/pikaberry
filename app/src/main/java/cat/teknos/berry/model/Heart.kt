package cat.teknos.berry.model

import android.graphics.RectF

class Heart(var x: Int, var y: Int, val radius: Int) {
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