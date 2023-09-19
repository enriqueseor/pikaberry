package cat.teknos.berry.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import cat.teknos.berry.R;

public class ResultsActivity extends AppCompatActivity {

    private String playerName;
    private int playerScore;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        playerName = intent.getStringExtra("playerName");
        playerScore = intent.getIntExtra("playerScore", 0);
        if (playerName == null) {
            playerName = "PLAYER";
        }
        resultTextView = findViewById(R.id.resultTextView);

        yourScore();
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void yourScore() {
        String resultText = "PLAYER: " + playerName + "\nSCORE: " + playerScore;
        resultTextView.setText(resultText);
    }
}