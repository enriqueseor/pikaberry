package cat.teknos.berry.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import cat.teknos.berry.R;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        String playerName = intent.getStringExtra("playerName");
        int playerScore = intent.getIntExtra("playerScore", 0);

        TableLayout scoreTable = findViewById(R.id.scoreTable);

        TableRow row = new TableRow(this);

        TextView nameTextView = new TextView(this);
        nameTextView.setText(playerName);
        row.addView(nameTextView);

        TextView scoreTextView = new TextView(this);
        scoreTextView.setText(String.valueOf(playerScore));
        row.addView(scoreTextView);

        scoreTable.addView(row);
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}