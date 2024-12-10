package cat.teknos.berry.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cat.teknos.berry.R

class ResultsActivity : AppCompatActivity() {
    private var playerName: String? = null
    private var playerScore = 0
    private var resultTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val intent = intent
        playerName = intent.getStringExtra("playerName")
        playerScore = intent.getIntExtra("playerScore", 0)
        if (playerName == null) {
            playerName = "PLAYER"
        }
        resultTextView = findViewById(R.id.resultTextView)

        yourScore()
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun yourScore() {
        val resultText = "PLAYER: $playerName\nSCORE: $playerScore"
        resultTextView!!.text = resultText
    }
}