package com.example.test2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.datepicker.MaterialDatePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class LineChart4 extends AppCompatActivity {

    //importnute data z data.json
    LinkedList<String> timeStampRoundedToMinute = new LinkedList<>();
    LinkedList<Float> avgTemp = new LinkedList<>();


    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.datepicker);
        setTitle("AvgTemp");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        lineChart = findViewById(R.id.line_chart);

        //vlozenie dat1 do grafu
        ArrayList<Entry> entry1 = new ArrayList<>();
        for (int i = 0; i < timeStampRoundedToMinute.size(); i++) {
            String dateString = timeStampRoundedToMinute.get(i);
            try {
                Date date = sdf.parse(dateString);
                float seconds = (float) date.getTime() / 1000;
                entry1.add(new Entry(seconds, avgTemp.get(i)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        LineDataSet dataSet1 = new LineDataSet(entry1, "AvgTemp");
        dataSet1.setColor(Color.BLACK);
        dataSet1.setLineWidth(1f);
        dataSet1.setCircleRadius(2f);
        dataSet1.setFillAlpha(65);
        dataSet1.setFillColor(ColorTemplate.getHoloBlue());
        dataSet1.setHighLightColor(Color.rgb(244, 117, 117));
        dataSet1.setDrawCircleHole(false);
        dataSet1.setDrawCircles(false);


        LineData lineData = new LineData(dataSet1);
        lineData.setValueTextSize(15f);
        lineChart.setData(lineData);

        // nastavenie xovej osy
        {
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setValueFormatter(new ValueFormatter() {
                private final SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy");

                @Override
                public String getFormattedValue(float value) {
                    // Convert Unix timestamp to date string
                    long timestamp = (long) value;
                    return mFormat.format(new Date(timestamp * 1000));
                }
            });
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set X-axis position
            xAxis.setLabelRotationAngle(45f); // Rotate X-axis labels
        }

        // enable scaling and dragging
        {
            lineChart.setTouchEnabled(true);
            lineChart.setDragDecelerationFrictionCoef(0.9f);
            lineChart.setDragEnabled(true);
            lineChart.setScaleEnabled(true);
            lineChart.setDrawGridBackground(false);
            lineChart.setHighlightPerDragEnabled(true);
            // background color
            lineChart.setBackgroundColor(Color.WHITE);
            lineChart.setPinchZoom(true);

            // create marker to display box when values are selected
            CustomMarkerView mv = new CustomMarkerView(this, R.layout.custom_marker_view);
            // Set the marker to the chart
            mv.setChartView(lineChart);
            lineChart.setMarker(mv);
        }


    }

}