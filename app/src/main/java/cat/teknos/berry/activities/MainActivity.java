package cat.teknos.berry.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.teknos.berry.R;

public class MainActivity extends AppCompatActivity {

    EditText etName;
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
    }

    public void play(View view) {
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