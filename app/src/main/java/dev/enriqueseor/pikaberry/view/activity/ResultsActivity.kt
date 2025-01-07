package dev.enriqueseor.pikaberry.view.activity

import android.content.ContentValues
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.enriqueseor.pikaberry.R
import dev.enriqueseor.pikaberry.database.PlayerDatabaseHelper
import dev.enriqueseor.pikaberry.view.adapter.ScoreAdapter

class ResultsActivity : AppCompatActivity() {
    private var playerName: String? = null
    private var playerScore = 0
    private lateinit var resultTextView: TextView
    private lateinit var dbHelper: PlayerDatabaseHelper
    private var isScoreSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        dbHelper = PlayerDatabaseHelper(this)

        resultTextView = findViewById(R.id.resultTextView)
        val scoreRecyclerView: RecyclerView = findViewById(R.id.scoreRecyclerView)
        scoreRecyclerView.layoutManager = LinearLayoutManager(this)

        val levelName = intent.getStringExtra("levelName") ?: "medium"
        playerName = intent.getStringExtra("playerName") ?: "PLAYER"
        playerScore = intent.getIntExtra("playerScore", 0)

        if (savedInstanceState != null) {
            isScoreSaved = savedInstanceState.getBoolean("isScoreSaved", false)
        }

        yourScore(levelName)

        val scores = getScoresByLevel(levelName)
        val adapter = ScoreAdapter(scores)
        scoreRecyclerView.adapter = adapter
    }

    private fun yourScore(levelName: String) {
        val resultText = "PLAYER: $playerName\nSCORE: $playerScore\nLEVEL: $levelName"
        resultTextView.text = resultText
        if (!isScoreSaved) {
            savePlayerScore(playerName!!, playerScore, levelName)
            isScoreSaved = true
        }
    }

    private fun savePlayerScore(name: String, score: Int, level: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(PlayerDatabaseHelper.COLUMN_NAME, name)
            put(PlayerDatabaseHelper.COLUMN_SCORE, score)
            put(PlayerDatabaseHelper.COLUMN_LEVEL, level)
        }
        db.insert(PlayerDatabaseHelper.TABLE_NAME, null, values)
        db.close()
    }

    private fun getScoresByLevel(level: String): List<Pair<String, Int>> {
        val db = dbHelper.readableDatabase
        val scores = mutableListOf<Pair<String, Int>>()

        val cursor = db.query(
            PlayerDatabaseHelper.TABLE_NAME,
            arrayOf(PlayerDatabaseHelper.COLUMN_NAME, PlayerDatabaseHelper.COLUMN_SCORE),
            "${PlayerDatabaseHelper.COLUMN_LEVEL} = ?",
            arrayOf(level),
            null, null,
            "${PlayerDatabaseHelper.COLUMN_SCORE} DESC",
            "10"
        )

        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(PlayerDatabaseHelper.COLUMN_NAME))
                val score = getInt(getColumnIndexOrThrow(PlayerDatabaseHelper.COLUMN_SCORE))
                scores.add(name to score)
            }
        }
        cursor.close()
        db.close()

        return scores
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isScoreSaved", isScoreSaved)
    }
}