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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setTitle("Menu");
        Utils.init(this);

        ArrayList<ContentItem> objects = new ArrayList<>();

        ////
        objects.add(0, new ContentItem("Grafy"));

        objects.add(1, new ContentItem("Graf 1", "Toto je graf 1."));
        objects.add(2, new ContentItem("Graf 2", "Toto je graf 2."));


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
        }

        if (i != null) startActivity(i);

        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
    }

}



