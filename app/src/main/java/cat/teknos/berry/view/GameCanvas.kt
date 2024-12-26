package cat.teknos.berry.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import cat.teknos.berry.R
import cat.teknos.berry.model.Berry
import cat.teknos.berry.model.Heart
import cat.teknos.berry.model.Pikachu
import cat.teknos.berry.model.Rock
import cat.teknos.berry.view.util.GameEventListener
import java.util.Random

class GameCanvas(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var canvasWidth: Int = 0
    private var canvasHeight: Int = 0
    private var radius: Int = 100
    private var level: Int = 2
    private var score: Int = 0
    private var lives: Int = 3

    private lateinit var pikachu: Pikachu
    private lateinit var heart: Heart
    private val berries = mutableListOf<Berry>()
    private val rocks = mutableListOf<Rock>()
    private var pikachuDrawable: Drawable? = null
    private var rockDrawable: Drawable? = null
    private var heartDrawable: Drawable? = null
    private var berryDrawable: Drawable? = null
    private var berriesDrawable: Array<Drawable?>
    private var gameEventListener: GameEventListener? = null
    private val random = Random()
    private val berryPointsArray = intArrayOf(1, 2, 3, 5, 10)
    private val scoreboard = RectF()
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
    }
    private val paintWhite = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    init {
        pikachuDrawable = ResourcesCompat.getDrawable(resources, R.drawable.pikachu, null)
        berriesDrawable = arrayOfNulls(5)
        berriesDrawable[0] = ResourcesCompat.getDrawable(resources, R.drawable.razz_berry, null)
        berriesDrawable[1] = ResourcesCompat.getDrawable(resources, R.drawable.pinap_berry, null)
        berriesDrawable[2] = ResourcesCompat.getDrawable(resources, R.drawable.nanap_berry, null)
        berriesDrawable[3] = ResourcesCompat.getDrawable(resources, R.drawable.pinap_berry_silver, null)
        berriesDrawable[4] = ResourcesCompat.getDrawable(resources, R.drawable.razz_berry_golden, null)
        rockDrawable = ResourcesCompat.getDrawable(resources, R.drawable.golem, null)
        heartDrawable = ResourcesCompat.getDrawable(resources, R.drawable.heart, null)
        berryDrawable = ResourcesCompat.getDrawable(resources, R.drawable.berry, null)
    }

    private fun initObjects() {
        val pikachuRadius = 100
        pikachu = Pikachu(canvasWidth / 2, canvasHeight - 100, pikachuRadius)

        val berryHeights = generateUniqueNegativeHeights(3)
        for (i in 0 until 3) {
            val berry = Berry(random.nextInt(canvasWidth), berryHeights[i], customRandomBerryType())
            berry.drawable = berriesDrawable[berry.type]
            berries.add(berry)
        }

        val rockHeights = generateUniqueNegativeHeights(3)
        for (i in 0 until 3) {
            val rock = Rock(random.nextInt(canvasWidth), rockHeights[i])
            rock.drawable = rockDrawable
            rocks.add(rock)
        }

        heart = Heart(
            x = random.nextInt(canvasWidth),
            y = (-15000..-12000).random() * level,
            radius = radius,
            context = context
        )
    }

    private fun generateUniqueNegativeHeights(count: Int): List<Int> {
        val uniqueHeights = mutableSetOf<Int>()
        while (uniqueHeights.size < count) {
            val randomHeight = -random.nextInt(1500)
            uniqueHeights.add(randomHeight)
        }
        return uniqueHeights.toList()
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)

        canvasWidth = w
        canvasHeight = h

        initObjects()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_MOVE || event.action == MotionEvent.ACTION_UP) {
            pikachu.updatePosition(event.x.toInt(), canvasWidth)
            this.invalidate()
        }
        return true
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        /******************************************************
        *                       PIKACHU                       *
        *******************************************************/
        pikachu.setBounds()
        pikachuDrawable!!.setBounds(
            pikachu.rect.left.toInt(),
            pikachu.rect.top.toInt(),
            pikachu.rect.right.toInt(),
            pikachu.rect.bottom.toInt()
        )
        pikachuDrawable!!.draw(canvas)

        /******************************************************
         *                       BERRIES                      *
         ******************************************************/
        for (berry in berries) {
            berry.draw(canvas, radius)
            berry.updatePosition(canvasWidth, canvasHeight, 10 * level, 0)
            updateBerry(berry)
        }

        /*****************************************************
         *                        ROCKS                      *
         *****************************************************/
        for (rock in rocks) {
            rock.draw(canvas, radius)
            rock.updatePosition(canvasWidth, canvasHeight, 10 * level, 0)
            updateRock(rock)
        }

        /*****************************************************
         *                        HEART                      *
         *****************************************************/
        heart.setBounds()
        heart.draw(canvas)
        updateHeart()

        /*****************************************************
         *                     SCOREBOARD                    *
         *****************************************************/

        val canvasWidth = width
        val canvasHeight = height

        // SCOREBOARD BACKGROUND
        scoreboard.set(
            0f,
            0f,
            canvasWidth.toFloat(),
            canvasHeight * 0.1f
        )
        canvas.drawRect(scoreboard, paintWhite)

        // BERRY ICON
        val berryX = canvasWidth * 0.16f / 2
        val berryY = canvasHeight * 0.1f / 2
        berryDrawable?.setBounds(
            (berryX - canvasHeight * 0.1f / 2).toInt(),
            (berryY - canvasHeight * 0.1f / 2).toInt(),
            (berryX + canvasHeight * 0.1f / 2).toInt(),
            (berryY + canvasHeight * 0.1f / 2).toInt()
        )
        berryDrawable?.draw(canvas)

        // SCORE
        val textX = canvasWidth * 0.30f
        val textY = canvasHeight * 0.08f
        textPaint.textSize = canvasHeight * 0.08f
        canvas.drawText(
            "$score",
            textX,
            textY,
            textPaint
        )

        // HEARTS ICONS
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

    private fun updateBerry(berry: Berry) {
        if (RectF.intersects(pikachu.rect, berry.rect)) {
            val berryPoints = berryPointsArray[berry.type]
            score += berryPoints
            gameEventListener?.onBerryCollected()
            gameEventListener?.onScoreUpdated(score)
            berry.x = random.nextInt(canvasWidth)
            berry.y = 0
            berry.type = customRandomBerryType()
        }
    }

    private fun updateRock(rock: Rock) {
        if (RectF.intersects(pikachu.rect, rock.rect)) {
            rock.x = random.nextInt(canvasWidth)
            rock.y = 0
            gameEventListener?.onRockCollision()
            if (lives > 0) {
                lives -= 1
            }
        }
    }

    private fun updateHeart() {
        val speed = 10 * level
        if (heart.y > canvasHeight) {
            heart.y = (-15000..-12000).random() * level
            heart.updatePosition(random.nextInt(canvasWidth), canvasWidth)
        } else {
            heart.y += speed
        }
        heart.setBounds()

        if (RectF.intersects(pikachu.rect, heart.rect)) {
            heart.y = (-17500..-12500).random() * level
            heart.updatePosition(random.nextInt(canvasWidth), canvasWidth)
            gameEventListener?.onHeartCollected()
            if (lives < 4) {
                lives += 1
            }
        }
    }

    fun setGameEventListener(listener: GameEventListener?) {
        this.gameEventListener = listener
    }

    fun setDifficultyLevel(level: Int) {
        this.level = level
    }

    private fun customRandomBerryType(): Int {
        val probabilities = doubleArrayOf(0.60, 0.20, 0.10, 0.050, 0.025)
        val rand = random.nextDouble()
        var cumulativeProbability = 0.0
        for (i in probabilities.indices) {
            cumulativeProbability += probabilities[i]
            if (rand <= cumulativeProbability) {
                return i
            }
        }
        return 0
    }
}