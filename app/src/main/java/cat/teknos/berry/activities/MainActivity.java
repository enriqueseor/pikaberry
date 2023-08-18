package cat.teknos.berry.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.teknos.apulidofruites.R;

import cat.teknos.berry.singleton.Singleton;

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
        Singleton singleton = Singleton.getInstance();
        singleton.setPlayer(etName.getText().toString());
        if (rbEasy.isChecked()) {
            singleton.setDifficulty(1);
        } else if (rbMedium.isChecked()){
            singleton.setDifficulty(2);
        } else if (rbHard.isChecked()) {
            singleton.setDifficulty(3);
        }

        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }
}