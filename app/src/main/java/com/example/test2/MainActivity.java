package com.example.test2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        File dataFile = new File(getApplicationContext().getFilesDir(), "data1.json");
        if (!dataFile.exists()) {
            // if the file does not exist, create an empty file
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //stiahne z url json data do suboru
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            new GetDataTask(getApplicationContext()).execute();
        }

        setTitle("Menu");
        Utils.init(this);
        ArrayList<ContentItem> objects = new ArrayList<>();
        ////
        objects.add(0, new ContentItem("Grafy"));
        objects.add(1, new ContentItem("Graf 1", "Recieved optical power, AvgTemp"));
        objects.add(2, new ContentItem("Graf 2", "Recieved optical power, avgPressure"));
        objects.add(3, new ContentItem("Graf 3", "Recieved optical power, avgTempDHT22"));
        objects.add(4, new ContentItem("Graf 4", "Recieved optical power, avgHumiDHT22"));

        MyAdapter adapter = new MyAdapter(this, objects);

        ListView lv = findViewById(R.id.listView1);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> av, View v, int pos, long arg3) {
        Intent i = null;
        switch (pos) {
            case 1:
                i = new Intent(this, LineChart1.class);
                break;
            case 2:
                i = new Intent(this, LineChart2.class);
                break;
            case 3:
                i = new Intent(this, LineChart3.class);
                break;
            case 4:
                i = new Intent(this, LineChart4.class);
                break;
        }
        if (i != null) startActivity(i);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
    }
}

