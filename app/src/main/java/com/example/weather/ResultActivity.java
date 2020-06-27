package com.example.weather;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ResultActivity extends AppCompatActivity {

    private final static  String base_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    private final static String APPID = "1e5d018bb2db56badb2dcc37bdbc9961";
    private final static  String unitImperial = "imperial";
    private final static String unitMetric = "metric";
    private final static String degreeF = "\u2109";
    private final static String degreeC = "\u2103";

    private static class GetWeather extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... location) {
            try {
                URL address = new URL(base_URL + location[0] + "&units=" + location[1] + "&APPID=" + APPID);
                HttpURLConnection connection = (HttpURLConnection) address.openConnection();
                connection.connect();

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int data = isr.read();
                StringBuilder content = new StringBuilder();
                char ch;
                while (data != -1) {
                    ch = (char) data;
                    content.append(ch);
                    data = isr.read();
                }
                return content.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        String cityName = intent.getStringExtra(MainActivity.cityNameString);
        String spinnerSel = intent.getStringExtra(MainActivity.spinnerSelString);
        String unit = unitImperial;
        String unitSym = degreeF;
        assert spinnerSel != null;
        if (spinnerSel.equals("C")) {
            unit = unitMetric;
            unitSym = degreeC;
        }
        TextView city = findViewById(R.id.city);
        TextView weatherDesc = findViewById(R.id.weatherDesc);
        TextView temp = findViewById(R.id.Temp);
        TextView maxTemp = findViewById(R.id.maxTemp);
        TextView minTemp = findViewById(R.id.minTemp);
        TextView sunrise = findViewById(R.id.sunrise);
        TextView sunset = findViewById(R.id.sunset);
        ImageView icon = findViewById(R.id.imageView);

        String data;
        GetWeather weather = new GetWeather();
        try {
            data = weather.execute(cityName, unit).get();
            Log.i("weatherData", data);
            WeatherData weatherData = new WeatherData(data);
            icon.setImageResource(weatherData.imageResource);
            city.setText(weatherData.cityName);
            weatherDesc.setText(getString(R.string.weatherDesc, weatherData.main, weatherData.description));
            temp.setText(getString(R.string.tempOutput, weatherData.temp, unitSym));
            maxTemp.setText(getString(R.string.maxTempOutput, weatherData.maxTemp, unitSym));
            minTemp.setText(getString(R.string.minTempOutput, weatherData.minTemp, unitSym));
            sunrise.setText(weatherData.sunriseTime);
            sunset.setText(weatherData.sunsetTime);
        } catch (Exception e) {
            e.printStackTrace();
            city.setText(e.toString());
            new AlertDialog.Builder(this).setTitle("An Exception Has Occurred")
                    .setMessage("Did not find city").setNeutralButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }).setIcon(android.R.drawable.ic_dialog_alert).show();
        }
    }
}
