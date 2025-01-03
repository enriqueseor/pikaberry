package cat.teknos.pikaberry.view.activity

import android.content.Intent
import android.content.res.Configuration
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import cat.teknos.pikaberry.R
import cat.teknos.pikaberry.media.PlaylistManager
import cat.teknos.pikaberry.view.GameCanvas
import cat.teknos.pikaberry.view.util.GameEventListener

class GameActivity : AppCompatActivity(), GameEventListener {
    private var levelNumber = 2
    private var score = 0
    private var numLives = 3
    private var isGameFinished = false

    private lateinit var game: GameCanvas
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var playerName: String
    private lateinit var soundPool: SoundPool
    private lateinit var soundMap: Map<Int, Int>

    private var playlistManager: PlaylistManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        levelNumber = intent.getIntExtra("levelNumber", 2)
        playerName = intent.getStringExtra("playerName") ?: "Unknown"

        game = findViewById(R.id.Screen)
        game.setGameEventListener(this)
        game.setDifficultyLevel(levelNumber)

        initializeSoundPool()
        playList()
        observer()
        gameTimer()
    }

    private fun initializeSoundPool() {
        soundPool = SoundPool.Builder().setMaxStreams(5).build()
        soundMap = mapOf(
            R.raw.geodude to soundPool.load(this, R.raw.geodude, 2),
            R.raw.heart to soundPool.load(this, R.raw.heart, 3),
            R.raw.berry to soundPool.load(this, R.raw.berry, 1)
        )
    }

    private fun playSound(soundResource: Int) {
        soundMap[soundResource]?.let { soundId ->
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
        }
    }

    override fun onBerryCollected() {
        playSound(R.raw.berry)
    }

    override fun onRockCollision() {
        playSound(R.raw.geodude)
        if (numLives == 1) {
            onGameFinished()
        } else {
            numLives -= 1
        }
    }

    override fun onHeartCollected() {
        playSound(R.raw.heart)
        if (numLives < 3) {
            numLives ++
        }
    }

    override fun onScoreUpdated(newScore: Int) {
        score = newScore
    }

    override fun onPause() {
        super.onPause()
        playlistManager?.pause()
    }

    override fun onResume() {
        super.onResume()
        playlistManager?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        playlistManager?.release()
        soundPool.release()
    }

    private fun onGameFinished() {
        if (isGameFinished) return
        isGameFinished = true
        game.setGameEventListener(null)
        Intent(this, ResultsActivity::class.java).apply {
            putExtra("levelNumber", levelNumber)
            putExtra("levelName", intent.getStringExtra("levelName") ?: "MEDIUM")
            putExtra("playerName", playerName)
            putExtra("playerScore", score)
            startActivity(this)
        }
        finish()
    }

    private fun playList() {
        val songsAndPortraitBackgrounds = listOf(
            Pair(R.raw.route_101, R.drawable.route_101_portrait),
            Pair(R.raw.route_104, R.drawable.route_104_portrait),
            Pair(R.raw.route_110, R.drawable.route_110_portrait),
            Pair(R.raw.route_119, R.drawable.route_119_portrait),
            Pair(R.raw.route_120, R.drawable.route_120_portrait)
        )
        val songsAndLandscapeBackgrounds = listOf(
            Pair(R.raw.route_101, R.drawable.route_102_landscape),
            Pair(R.raw.route_104, R.drawable.route_116_landscape),
            Pair(R.raw.route_110, R.drawable.route_117_landscape),
            Pair(R.raw.route_113, R.drawable.route_113_landscape),
            Pair(R.raw.route_119, R.drawable.route_123_landscape),
            Pair(R.raw.route_120, R.drawable.route_121_landscape)
        )
        val orientation = resources.configuration.orientation

        val (shuffledSongs, shuffledBackgrounds) = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val shuffledList = songsAndLandscapeBackgrounds.shuffled()
            shuffledList.map { it.first }.toIntArray() to shuffledList.map { it.second }.toIntArray()
        } else {
            val shuffledList = songsAndPortraitBackgrounds.shuffled()
            shuffledList.map { it.first }.toIntArray() to shuffledList.map { it.second }.toIntArray()
        }

        playlistManager = PlaylistManager(this, shuffledSongs).apply { start() }

        val backgroundImageView = findViewById<ImageView>(R.id.backgroundImageView)
        backgroundImageView.setImageResource(shuffledBackgrounds.getOrNull(0) ?: 0)
        backgroundImageView.scaleType = ImageView.ScaleType.CENTER_CROP

        playlistManager?.setOnSongChangeListener { index ->
            shuffledBackgrounds.getOrNull(index)?.let {
                backgroundImageView.setImageResource(it)
                backgroundImageView.scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }
    }

    private fun observer() {
        val observer = game.viewTreeObserver
        observer.addOnGlobalLayoutListener {}
    }

    private fun gameTimer() {
        handler.post(object : Runnable {
            override fun run() {
                game.invalidate()
                handler.postDelayed(this, 16)
            }
        })
    }
}