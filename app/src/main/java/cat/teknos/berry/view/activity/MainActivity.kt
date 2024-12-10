package cat.teknos.berry.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import cat.teknos.berry.R

class MainActivity : AppCompatActivity() {
    private var etName: EditText? = null
    private var rbEasy: RadioButton? = null
    private var rbHard: RadioButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etName = findViewById(R.id.etName)
        rbEasy = findViewById(R.id.rbEasy)
        rbHard = findViewById(R.id.rbHard)

        val btnPlay = findViewById<Button>(R.id.btnPlay)
        btnPlay.setOnClickListener { _: View? -> play() }
    }

    private fun play() {
        var level = 2
        if (rbEasy!!.isChecked) {
            level = 1
        } else if (rbHard!!.isChecked) {
            level = 3
        }

        var playerName = etName!!.text.toString()
        if (playerName.isEmpty()) {
            playerName = "PLAYER"
        }

        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("level", level)
        intent.putExtra("playerName", playerName)
        startActivity(intent)
    }
}