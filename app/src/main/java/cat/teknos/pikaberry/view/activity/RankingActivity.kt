package cat.teknos.pikaberry.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.teknos.pikaberry.R
import cat.teknos.pikaberry.database.PlayerDatabaseHelper
import cat.teknos.pikaberry.view.adapter.ScoreAdapter

class RankingActivity : AppCompatActivity() {
    private lateinit var dbHelper: PlayerDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        dbHelper = PlayerDatabaseHelper(this)

        val easyRecyclerView: RecyclerView = findViewById(R.id.easyRecyclerView)
        val mediumRecyclerView: RecyclerView = findViewById(R.id.mediumRecyclerView)
        val hardRecyclerView: RecyclerView = findViewById(R.id.hardRecyclerView)

        easyRecyclerView.layoutManager = LinearLayoutManager(this)
        mediumRecyclerView.layoutManager = LinearLayoutManager(this)
        hardRecyclerView.layoutManager = LinearLayoutManager(this)

        val easyScores = getScoresByLevel("EASY")
        val mediumScores = getScoresByLevel("MEDIUM")
        val hardScores = getScoresByLevel("HARD")

        easyRecyclerView.adapter = ScoreAdapter(easyScores)
        mediumRecyclerView.adapter = ScoreAdapter(mediumScores)
        hardRecyclerView.adapter = ScoreAdapter(hardScores)
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
}