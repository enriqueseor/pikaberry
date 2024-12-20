package cat.teknos.berry.view.activity

import android.content.Intent
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cat.teknos.berry.R
import cat.teknos.berry.model.PlaylistManager
import cat.teknos.berry.view.GameCanvas
import cat.teknos.berry.view.util.GameEventListener

class GameActivity : AppCompatActivity(), GameEventListener {
    private var levelNumber = 0
    private var score = 0
    private var numLives = 3
    private val maxLives = 3

    private lateinit var game: GameCanvas
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var liveIcons: List<ImageView>
    private lateinit var playerName: String
    private lateinit var soundPool: SoundPool
    private lateinit var soundMap: Map<Int, Int>

    private var playlistManager: PlaylistManager? = null
    private var currentPlayingSound: Int? = null
    private var currentPriority: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        game = findViewById(R.id.Screen)
        game.setGameEventListener(this)

        // Initialize variables
        levelNumber = intent.getIntExtra("levelNumber", 2)
        game.setDifficultyLevel(levelNumber)
        playerName = intent.getStringExtra("playerName") ?: "Unknown"

        liveIcons = listOf(
            findViewById(R.id.live1),
            findViewById(R.id.live2),
            findViewById(R.id.live3)
        )

        soundPool = SoundPool.Builder().setMaxStreams(5).build()
        soundMap = mapOf(
            R.raw.geodude to soundPool.load(this, R.raw.geodude, 2),
            R.raw.heart to soundPool.load(this, R.raw.heart, 3),
            R.raw.berry to soundPool.load(this, R.raw.berry, 1)
        )

        playList()
        observer()
        timer()
    }

    private fun playSound(soundResource: Int, priority: Int) {
        if (currentPlayingSound != null) {
            if (currentPriority > priority) {
                return
            }
        }
        soundMap[soundResource]?.let { soundId ->
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
            currentPlayingSound = soundResource
        }
    }

    override fun onBerryCollected(berryType: Int) {
        val textView = findViewById<TextView>(R.id.points)
        var berryPoints = 0
        when (berryType) {
            0 -> berryPoints = 1
            1 -> berryPoints = 2
            2 -> berryPoints = 3
            3 -> berryPoints = 5
            4 -> berryPoints = 10
        }
        val points = textView.text.toString().toInt()
        score = points + berryPoints
        textView.text = String.format(score.toString())
        playSound(R.raw.berry, 1)
    }

    override fun onRockCollision() {
        if (numLives > 0) {
            numLives--
            updateLifeIconsVisibility()
            playSound(R.raw.geodude, 2)
        }
        if (numLives == 0) {
            onGameFinished()
        }
    }

    override fun onHeartCollected() {
        if (numLives < maxLives) {
            numLives++
            updateLifeIconsVisibility()
        }
        playSound(R.raw.heart, 2)
    }

    private fun updateLifeIconsVisibility() {
        liveIcons.forEachIndexed { index, icon ->
            icon.visibility = if (index < numLives) View.VISIBLE else View.INVISIBLE
        }
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
        val songResources = intArrayOf(
            R.raw.route_101,
            R.raw.route_104,
            R.raw.route_110,
            R.raw.route_113,
            R.raw.route_119,
            R.raw.route_120,
        )
        val shuffledSongs = songResources.toMutableList().apply {
            val randomIndex = indices.random()
            add(0, removeAt(randomIndex))
        }
        playlistManager = PlaylistManager(this, shuffledSongs.toIntArray()).apply { start() }
    }

    private fun observer() {
        val observer = game.viewTreeObserver
        observer.addOnGlobalLayoutListener {}
    }

    private fun timer() {
        handler.post(object : Runnable {
            override fun run() {
                game.invalidate()
                handler.postDelayed(this, 16)
            }
        })
    }
}