package com.example.test2;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

public class JsonParser extends AsyncTask<Void, Void, Void> {

    private String url;
    private LinkedList<String> timeStampRoundedToMinute = new LinkedList<>();
    private LinkedList<Float> received_optical_power = new LinkedList<>();
    private LinkedList<Float> avgTemp = new LinkedList<>();

    public JsonParser(String url) {
        this.url = url;
    }

    public LinkedList<String> getTimeStampRoundedToMinute() {
        return timeStampRoundedToMinute;
    }

    public LinkedList<Float> getReceived_optical_power() {
        return received_optical_power;
    }

    public LinkedList<Float> getAvgTemp() {
        return avgTemp;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String jsonString = "";

        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    jsonString += line;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                timeStampRoundedToMinute.add(obj.getString("timeStampRoundedToMinute"));
                received_optical_power.add((float) obj.getDouble("received_optical_power"));
                avgTemp.add((float) obj.getDouble("avgTemp"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


}

