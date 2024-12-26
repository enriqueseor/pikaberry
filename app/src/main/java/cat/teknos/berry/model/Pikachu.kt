package cat.teknos.berry.model

import android.graphics.RectF

class Pikachu(var x: Int, var y: Int, val radius: Int) {
    val rect = RectF()

    fun updatePosition(newX: Int, canvasWidth: Int) {
        x = newX.coerceIn(radius, canvasWidth - radius) // Evitar que Pikachu salga de los bordes
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