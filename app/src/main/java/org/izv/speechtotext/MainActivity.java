package org.izv.speechtotext;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative

    private ActivityResultLauncher<Intent> sttLauncher;
    private Intent sttIntent;
    private TextView tvStt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private Intent getSttIntent() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, new Locale("spa", "ES"));
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speak));
        return intent;
    }

    private ActivityResultLauncher<Intent> getSttLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    String text = "";
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        List<String> r = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        text = r.get(0);
                    } else if(result.getResultCode() == Activity.RESULT_CANCELED) {
                        text = getString(R.string.error);
                    }
                    showResult(text);
                }
        );
    }

    private void initialize() {
        Button btStt = findViewById(R.id.btStt);
        tvStt = findViewById(R.id.tvStt);

        sttLauncher = getSttLauncher();
        sttIntent = getSttIntent();

        btStt.setOnClickListener(view -> {
            sttLauncher.launch(sttIntent);
        });
    }

    private void showResult(String result) {
        tvStt.setText(result);
    }

}