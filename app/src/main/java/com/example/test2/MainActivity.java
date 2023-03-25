package com.example.test2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;


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
        Button myButton = findViewById(R.id.my_button);
        myButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, GraphBar.class);
            startActivity(intent);
        });
    }


    @Override
    public void onItemClick(AdapterView<?> av, View v, int pos, long arg3) {
        Intent i = null;
        switch (pos) {
            case 1:
                i = new Intent(this, GraphBar.class);
                break;

        }
        if (i != null) startActivity(i);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
    }
}

