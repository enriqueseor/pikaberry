package cat.teknos.berry.view.activity

import android.content.Intent
import android.media.MediaPlayer
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
import cat.teknos.berry.view.util.OnBerryCollectedListener
import java.util.Random
import java.util.Timer
import java.util.TimerTask

class GameActivity : AppCompatActivity(), GameEventListener, OnBerryCollectedListener {
    private var level = 0
    private var soundResource = 0
    private var score = 0
    private var numLives = 3
    private val maxLives = 3
    private var game: GameCanvas? = null
    private val handler = Handler(Looper.getMainLooper())
    private var playlistManager: PlaylistManager? = null
    private var live1: ImageView? = null
    private var live2: ImageView? = null
    private var live3: ImageView? = null
    private var playerName: String? = null
    private var heartTimer: Timer? = null
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        game = findViewById(R.id.Screen)
        game?.setGameEventListener(this)
        game?.setOnBerryCollectedListener(this)

        val intent = intent
        level = intent.getIntExtra("level", 2)
        game?.setDifficultyLevel(level)
        playerName = getIntent().getStringExtra("playerName")

        live1 = findViewById(R.id.live1)
        live2 = findViewById(R.id.live2)
        live3 = findViewById(R.id.live3)

        mediaPlayer = MediaPlayer.create(this, R.raw.berry_collected)
        soundResource = R.raw.berry_collected

        playList()
        obs()
        timer()
        delayedHeartTimer()
    }

    private fun playSound(soundResource: Int) {
        if (this.soundResource != soundResource) {
            mediaPlayer!!.release()
            mediaPlayer = MediaPlayer.create(this, soundResource)
            this.soundResource = soundResource
        }
        mediaPlayer!!.start()
    }


    override fun onBerryCollected(berryType: Int) {
        runOnUiThread {
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
            playSound(R.raw.berry_collected)
        }
    }

    override fun onRockCollision() {
        if (numLives > 0) {
            numLives--
            updateLifeIconsVisibility()
            playSound(R.raw.rock_collision)
        }
        if (numLives == 0) {
            onGameFinished()
        }
    }

    override fun onNewHeartGenerated() {
        stopHeartTimer()
    }

    override fun onHeartCollected() {
        runOnUiThread {
            if (numLives < maxLives) {
                numLives++
                updateLifeIconsVisibility()
            }
        }
        playSound(R.raw.heart_collected)
        stopHeartTimer()
    }

    private fun stopHeartTimer() {
        if (heartTimer != null) {
            heartTimer!!.cancel()
            heartTimer = null
        }
    }

    private fun updateLifeIconsVisibility() {
        live1!!.visibility =
            if (numLives >= 1) View.VISIBLE else View.INVISIBLE
        live2!!.visibility =
            if (numLives >= 2) View.VISIBLE else View.INVISIBLE
        live3!!.visibility =
            if (numLives == 3) View.VISIBLE else View.INVISIBLE
    }

    override fun onPause() {
        super.onPause()
        if (playlistManager != null) {
            playlistManager!!.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (playlistManager != null) {
            playlistManager!!.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (playlistManager != null) {
            playlistManager!!.release()
        }
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
        }
    }

    private fun onGameFinished() {
        val intent = Intent(this, ResultsActivity::class.java)
        intent.putExtra("playerScore", score)
        intent.putExtra("playerName", playerName)
        startActivity(intent)
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
        playlistManager = PlaylistManager(this, songResources)
        playlistManager!!.start()
    }

    private fun obs() {
        val obs = game!!.viewTreeObserver
        obs.addOnGlobalLayoutListener {}
    }

    private fun timer() {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post {
                    game!!.posBerryY += level * 10
                    game!!.posRockY += level * 10
                    game!!.invalidate()
                }
            }
        }, 0, 20)
    }

    private fun heartTimer() {
        if (heartTimer != null) {
            heartTimer!!.cancel()
        }
        heartTimer = Timer()
        heartTimer!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    game!!.posHeartY += level * 10
                    game!!.invalidate()
                }
            }
        }, 0, 20)
    }

    private fun delayedHeartTimer() {
        val handler = Handler(Looper.getMainLooper())
        val random = Random()
        val randomDelay = random.nextInt(15000) + 30000
        handler.postDelayed({
            heartTimer()
            delayedHeartTimer()
        }, randomDelay.toLong())
    }
}