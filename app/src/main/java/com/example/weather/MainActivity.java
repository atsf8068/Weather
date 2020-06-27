package com.example.weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String cityNameString = "cityName";
    public static final String spinnerSelString = "spinnerSelString";

    private View.OnClickListener buttonClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            gotoResult();
        }
    };

    private void gotoResult() {
        EditText editText = findViewById(R.id.editText);
        final Spinner spinner = findViewById(R.id.selectUnit);
        String spinnerSel = spinner.getSelectedItem().toString();
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        String cityName = editText.getText().toString();
        intent.putExtra(cityNameString, cityName);
        intent.putExtra(spinnerSelString,spinnerSel);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(buttonClick);
    }

}
