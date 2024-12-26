package cat.teknos.berry.model

import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.Drawable

class Rock(var x: Int, var y: Int) {
    val rect = RectF()
    var drawable: Drawable? = null

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

    fun updatePosition(canvasWidth: Int, canvasHeight: Int, speed: Int, resetY: Int) {
        if (y > canvasHeight) {
            x = (0..canvasWidth).random()
            y = resetY
        } else {
            y += speed
        }
    }
}