package cat.teknos.berry.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import cat.teknos.berry.model.Berry
import cat.teknos.berry.model.Heart
import cat.teknos.berry.model.Pikachu
import cat.teknos.berry.model.Rock
import cat.teknos.berry.model.Scoreboard
import cat.teknos.berry.view.util.GameEventListener

class GameCanvas(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var canvasWidth: Int = 0
    private var canvasHeight: Int = 0
    private var radius: Int = 100
    private var baseSpeed: Int = 10
    private var level: Int = 2
    private var score: Int = 0
    private var lives: Int = 3

    private lateinit var pikachu: Pikachu
    private val berries = mutableListOf<Berry>()
    private val rocks = mutableListOf<Rock>()
    private lateinit var heart: Heart
    private lateinit var scoreboard: Scoreboard

    private var gameEventListener: GameEventListener? = null

    private fun initObjects() {
        val pikachuRadius = 100
        pikachu = Pikachu(canvasWidth / 2, canvasHeight - 100, pikachuRadius, context)

        val berryHeights = generateUniqueNegativeHeights(3)
        for (i in 0 until 3) {
            val berry = Berry((0..canvasWidth).random(), berryHeights[i], resources)
            berries.add(berry)
        }

        val rockHeights = generateUniqueNegativeHeights(3)
        for (i in 0 until 3) {
            val rock = Rock((0..canvasWidth).random(), rockHeights[i], context)
            rocks.add(rock)
        }

        heart = Heart(
            x = (0..canvasWidth).random(),
            y = (-15000..-12000).random() * level,
            context = context
        )

        scoreboard = Scoreboard(canvasWidth, canvasHeight, score, lives, context)
    }

    private fun generateUniqueNegativeHeights(count: Int): List<Int> {
        val uniqueHeights = mutableSetOf<Int>()
        while (uniqueHeights.size < count) {
            val randomHeight = -(0 until 1500).random()
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
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                pikachu.updatePosition(event.x.toInt(), canvasWidth)
                this.invalidate()
            }
            MotionEvent.ACTION_UP -> {
                pikachu.updatePosition(event.x.toInt(), canvasWidth)
                this.invalidate()
                performClick()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        /******************************************************
        *                       PIKACHU                       *
        *******************************************************/
        pikachu.setBounds()
        pikachu.draw(canvas)

        /******************************************************
         *                       BERRIES                      *
         ******************************************************/
        for (berry in berries) {
            berry.draw(canvas, radius)
            berry.updatePosition(canvasWidth, canvasHeight, baseSpeed, level)
            onBerryCollision(berry)
        }

        /*****************************************************
         *                        ROCKS                      *
         *****************************************************/
        for (rock in rocks) {
            rock.draw(canvas, radius)
            rock.updatePosition(canvasWidth, canvasHeight, baseSpeed, level)
            onRockCollision(rock)
        }

        /*****************************************************
         *                        HEART                      *
         *****************************************************/
        heart.draw(canvas, radius)
        heart.updatePosition(canvasWidth, canvasHeight, baseSpeed, level)
        onHeartCollision()

        /*****************************************************
         *                     SCOREBOARD                    *
         *****************************************************/
        scoreboard.draw(canvas)
        scoreboard.updateScore(score, lives)
    }

    private fun onBerryCollision(berry: Berry) {
        val berryPointsArray = intArrayOf(1, 2, 3, 5, 10)
        if (RectF.intersects(pikachu.rect, berry.rect)) {
            val berryPoints = berryPointsArray[berry.type]
            score += berryPoints
            gameEventListener?.onBerryCollected()
            gameEventListener?.onScoreUpdated(score)
            berry.x = (0..canvasWidth).random()
            berry.y = 0
            berry.type = berry.customRandomBerryType()
            berry.setDrawable()
        }
    }

    private fun onRockCollision(rock: Rock) {
        if (RectF.intersects(pikachu.rect, rock.rect)) {
            rock.x = (0..canvasWidth).random()
            rock.y = 0
            gameEventListener?.onRockCollision()
            if (lives > 0) {
                lives -= 1
            }
        }
    }

    private fun onHeartCollision() {
        if (RectF.intersects(pikachu.rect, heart.rect)) {
            heart.x = (0..canvasWidth).random()
            heart.y = (-17500..-12500).random() * level
            gameEventListener?.onHeartCollected()
            if (lives < 3) {
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
}