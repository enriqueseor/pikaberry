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
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void yourScore(){
        score = playerScore;
    }
}