package com.tz.downloadjson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private String targetUrl = "https://api.openweathermap.org/data/2.5/weather?lat=55.7504461&lon=37.6174943&appid=9ad44b0c121776b58879fb126eea524a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadJSONTask task = new DownloadJSONTask();
        task.execute(targetUrl);
    }

    //Download Content
    private static class DownloadJSONTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();
            URL url = null;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = bufferedReader.readLine();
                while (line != null) {
                    result.append(line);
                    line = bufferedReader.readLine();
                }
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                //get "name" of the city
                String name = jsonObject.getString("name");
                Log.i("Result", name);

                //get the json object "main"
                JSONObject main = jsonObject.getJSONObject("main");
                //get "temp" and "pressure" from object "main"
                String temp = main.getString("temp");
                String pressure = main.getString("pressure");
                Log.i("Result", "Temperature: " + temp + " | " + "Pressure: " + pressure);

                //get the data from array "weather"
                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                //get the first element of array "array"
                JSONObject weather = jsonArray.getJSONObject(0);
                //get string "main" and "description" from the first element of array
                String mainStr = weather.getString("main");
                String descriptionStr = weather.getString("description");
                Log.i("Result", "Main: " + mainStr + " | " + "Description: " + descriptionStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}