package cat.teknos.berry.view.activity

import android.content.ContentValues
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.teknos.berry.R
import cat.teknos.berry.database.PlayerDatabaseHelper
import cat.teknos.berry.view.adapter.ScoreAdapter

class ResultsActivity : AppCompatActivity() {
    private var playerName: String? = null
    private var playerScore = 0
    private lateinit var resultTextView: TextView
    private lateinit var dbHelper: PlayerDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        dbHelper = PlayerDatabaseHelper(this)

        resultTextView = findViewById(R.id.resultTextView)
        val scoreRecyclerView: RecyclerView = findViewById(R.id.scoreRecyclerView)
        scoreRecyclerView.layoutManager = LinearLayoutManager(this)

        val intent = intent
        playerName = intent.getStringExtra("playerName") ?: "PLAYER"
        playerScore = intent.getIntExtra("playerScore", 0)

        yourScore()

        val scores = getAllScores()
        val adapter = ScoreAdapter(scores)
        scoreRecyclerView.adapter = adapter
    }

    private fun yourScore() {
        val resultText = "PLAYER: $playerName\nSCORE: $playerScore"
        resultTextView.text = resultText
        savePlayerScore(playerName!!, playerScore)
    }

    private fun savePlayerScore(name: String, score: Int) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(PlayerDatabaseHelper.COLUMN_NAME, name)
            put(PlayerDatabaseHelper.COLUMN_SCORE, score)
        }
        db.insert(PlayerDatabaseHelper.TABLE_NAME, null, values)
        db.close()
    }

    private fun getAllScores(): List<Pair<String, Int>> {
        val db = dbHelper.readableDatabase
        val scores = mutableListOf<Pair<String, Int>>()

        val cursor = db.query(
            PlayerDatabaseHelper.TABLE_NAME,
            arrayOf(PlayerDatabaseHelper.COLUMN_NAME, PlayerDatabaseHelper.COLUMN_SCORE),
            null, null, null, null,
            "${PlayerDatabaseHelper.COLUMN_SCORE} DESC"
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
}