package com.example.test2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;


public class GraphBar extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);
        setTitle("Menu grafov");

        Utils.init(this);
        ArrayList<ContentItem> objects = new ArrayList<>();
        ////
        objects.add(0, new ContentItem("Grafy"));
        objects.add(1, new ContentItem("priemerná Teplota", "Teplota k RSSI"));
        objects.add(2, new ContentItem("Tlak", "Tlak k RSSI"));
        objects.add(3, new ContentItem("teplota zo senzora", "Teplota zo senzora DHT22 k RSSI"));
        objects.add(4, new ContentItem("Vlhkosť", "Vlhkosť k RSSI"));
        objects.add(5, new ContentItem("teplota zo senzora", "Teplota zo senzora avgTempT_ds18b20 k RSSI"));
        objects.add(6, new ContentItem("Rýchlosť vetra", "Rýchlosť vetra k RSSI"));
        objects.add(7, new ContentItem("Koncetrácia částic", "Koncentrácia částic k RSSI"));
        objects.add(8, new ContentItem("Viditeľnosť", "Viditeľnosť k RSSI"));

        MyAdapter adapter = new MyAdapter(this, objects);

        ListView lv = findViewById(R.id.listView);
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
            case 5:
                i = new Intent(this, LineChart5.class);
                break;
            case 6:
                i = new Intent(this, LineChart6.class);
                break;
            case 7:
                i = new Intent(this, LineChart7.class);
                break;
            case 8:
                i = new Intent(this, LineChart8.class);
                break;
        }
        if (i != null) startActivity(i);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
    }
}
