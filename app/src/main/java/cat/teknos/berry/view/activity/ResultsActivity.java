package cat.teknos.berry.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import cat.teknos.berry.R;

public class ResultsActivity extends AppCompatActivity {

    String playerName;
    int playerScore;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Log.d("ResultsActivity", "playerName: " + playerName);
        Log.d("ResultsActivity", "playerScore: " + playerScore);
        Intent intent = getIntent();
        playerName = intent.getStringExtra("playerName");
        playerScore = intent.getIntExtra("playerScore", 0);
        if (playerName != null) {
            playerName = "PLAYER";
        }

        score = String.valueOf(playerScore).length();

        yourScore();
        scoreTable();
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void yourScore(){
        score = playerScore;
    }

    private void scoreTable(){
        TableLayout scoreTable = findViewById(R.id.scoreTable);

        TableRow row = new TableRow(this);

        TextView nameTextView = new TextView(this);
        nameTextView.setText(playerName);
        nameTextView.setPadding(10, 0, 10, 0);
        nameTextView.setGravity(Gravity.CENTER);
        row.addView(nameTextView);

        TextView scoreTextView = new TextView(this);
        scoreTextView.setText(String.valueOf(playerScore));
        scoreTextView.setPadding(10, 0, 10, 0);
        scoreTextView.setGravity(Gravity.CENTER);
        row.addView(scoreTextView);

        scoreTable.addView(row);
    }
}