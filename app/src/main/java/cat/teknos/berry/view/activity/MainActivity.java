package cat.teknos.berry.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import cat.teknos.berry.R;

public class MainActivity extends AppCompatActivity {

    private EditText etName;
    private RadioButton rbEasy, rbHard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        rbEasy = findViewById(R.id.rbEasy);
        rbHard = findViewById(R.id.rbHard);

        Button btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(v -> play());
    }

    public void play() {
        int level = 2;
        if (rbEasy.isChecked()) {
            level = 1;
        } else if (rbHard.isChecked()) {
            level = 3;
        }

        String playerName = etName.getText().toString();
        if (playerName.isEmpty()) {
            playerName = "PLAYER";
        }

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("level", level);
        intent.putExtra("playerName", playerName);
        startActivity(intent);
    }
}