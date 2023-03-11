package com.example.test2;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class GetDataTask extends AsyncTask<Void, Void, String> {
    private Context mContext;

    public GetDataTask(Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        String urlString = "https://svkjanco.github.io/data.json";
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            connection.disconnect();

            return stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                saveDataToFile(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveDataToFile(JSONArray jsonArray) {
        try {
            FileWriter fileWriter = new FileWriter(new File(mContext.getFilesDir(), "data1.json"));
            fileWriter.write(jsonArray.toString());
            fileWriter.flush();
            fileWriter.close();
            System.out.println("data ulozene");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("data neulozene");
        }
    }
}