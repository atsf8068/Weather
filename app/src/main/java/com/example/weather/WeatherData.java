package com.example.weather;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

class WeatherData {

    String cityName;
    String description;
    String main;
    String temp;
    String minTemp;
    String maxTemp;
    String sunriseTime;
    String sunsetTime;
    int imageResource;

    WeatherData(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        cityName = getName(jsonObject);
        Log.i("name", cityName);
        HashMap<String, String> mainDesc = getMainDesc(jsonObject);
        main = mainDesc.get("main");
        description = mainDesc.get("description");
        getMainTemp(jsonObject);
        getSunriseSunset(jsonObject);
        getImageResource(Objects.requireNonNull(mainDesc.get("icon")));
    }

    private void getImageResource(String icon) {
        switch (icon) {
            case "01d":
                imageResource = R.drawable.day_clear;
                break;
            case "01n":
                imageResource = R.drawable.night_full_moon_clear;
                break;
            case "02d":
                imageResource = R.drawable.day_partial_cloud;
                break;
            case "02n":
                imageResource = R.drawable.night_full_moon_partial_cloud;
                break;
            case "03d":
            case "03n":
                imageResource = R.drawable.cloudy;
                break;
            case "04d":
            case "04n":
                imageResource = R.drawable.overcast;
                break;
            case "09d":
            case "09n":
                imageResource = R.drawable.rain;
                break;
            case "10d":
                imageResource = R.drawable.day_rain;
                break;
            case "10n":
                imageResource = R.drawable.night_full_moon_rain;
                break;
            case "11d":
            case "11n":
                imageResource = R.drawable.thunder;
                break;
            case "13d":
            case "13n":
                imageResource = R.drawable.snow;
                break;
            case "50d":
            case "50n":
                imageResource = R.drawable.mist;
                break;
            default:
                break;
        }
    }

    private void getSunriseSunset(JSONObject jsonObject) throws JSONException {
        JSONObject sys = jsonObject.getJSONObject("sys");
        Log.i("sys", sys.toString());
        long timezone = jsonObject.getLong("timezone") * 1000;
        long sunrise = sys.getLong("sunrise") * 1000;
        long sunset = sys.getLong("sunset") * 1000;
        SimpleDateFormat sunriseFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        sunriseFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        sunriseTime = sunriseFormat.format(new Date(sunrise + timezone));
        SimpleDateFormat sunsetFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        sunsetFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        sunsetTime = sunsetFormat.format(new Date(sunset + timezone));
    }

    private void getMainTemp(JSONObject jsonObject) throws JSONException {
        JSONObject mainTemp = jsonObject.getJSONObject("main");
        Log.i("mainTemp", String.valueOf(mainTemp));
        temp = mainTemp.getString("temp");
        minTemp = mainTemp.getString("temp_min");
        maxTemp = mainTemp.getString("temp_max");
    }

    private JSONArray getWeatherArray(JSONObject jsonObject) throws JSONException {
        String weatherData = jsonObject.getString("weather");
        return new JSONArray(weatherData);
    }

    private String getName(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("name");
    }

    private HashMap<String, String> getMainDesc(JSONObject jsonObject) throws JSONException {
        JSONArray weatherArray = getWeatherArray(jsonObject);
        String main = "";
        String description = "";
        String icon = "";
        Log.i("weather", weatherArray.toString());
        for (int i = 0; i < weatherArray.length(); i++) {
            JSONObject weatherPart = weatherArray.getJSONObject(i);
            main = weatherPart.getString("main");
            description = weatherPart.getString("description");
            icon = weatherPart.getString("icon");
        }
        HashMap<String, String> mainDesc = new HashMap<>();
        mainDesc.put("main", main);
        mainDesc.put("description", description);
        mainDesc.put("icon", icon);
        return mainDesc;
    }

}
