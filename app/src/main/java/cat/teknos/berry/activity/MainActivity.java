package cat.teknos.berry.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.teknos.berry.R;

public class MainActivity extends AppCompatActivity {

    EditText etName;
    Button btnPlay;
    RadioButton rbEasy;
    RadioButton rbMedium;
    RadioButton rbHard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        rbEasy = findViewById(R.id.rbEasy);
        rbMedium = findViewById(R.id.rbMedium);
        rbHard = findViewById(R.id.rbHard);

        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(v -> play());
    }

    public void play() {
        int selectedDifficulty = 0;

        if (rbEasy.isChecked()) {
            selectedDifficulty = 1;
        } else if (rbMedium.isChecked()){
            selectedDifficulty = 2;
        } else if (rbHard.isChecked()) {
            selectedDifficulty = 3;
        }

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("difficulty", selectedDifficulty);
        startActivity(intent);
    }
}