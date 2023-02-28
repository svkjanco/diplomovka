package com.example.test2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LineChart3 extends AppCompatActivity {

    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.datepicker);
        setTitle("DatePickerTest");

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1623945600, 5)); // Entry(x-value, y-value)
        entries.add(new Entry(1624032000, 7));
        entries.add(new Entry(1624118400, 3));

        LineDataSet dataSet = new LineDataSet(entries, "Custom Date Range"); // Replace "Custom Date Range" with your desired label
        dataSet.setColor(Color.BLUE); // Customize the color of the line
        dataSet.setDrawCircles(false); // Remove circles on data points

        LineData lineData = new LineData(dataSet);

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

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select Date Range");

// Set the initial date range to the last 7 days
        Calendar calendar = Calendar.getInstance();
        long end = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        long start = calendar.getTimeInMillis();
        builder.setSelection(new Pair<>(start, end));

        MaterialDatePicker<Pair<Long, Long>> picker = builder.build();

// Set a listener to update the chart data based on the selected date range
        picker.addOnPositiveButtonClickListener(selection -> {
            long startTimestamp = selection.first / 1000;
            long endTimestamp = selection.second / 1000;
            updateChartData(startTimestamp, endTimestamp);
        });

// Show the dialog when the user clicks a button or other UI element
        View button = null;
        button.setOnClickListener(view -> picker.show(getSupportFragmentManager(), "DATE_PICKER"));

// Create a new LineDataSet and LineData object using the filtered data

// Set the new LineData object on the chart
        lineChart.setData(lineData);

// Refresh the chart
        lineChart.invalidate();


    }

    private void updateChartData(long startTimestamp, long endTimestamp) {
        // Filter the data points based on the selected date range
        ArrayList<Entry> filteredEntries = new ArrayList<>();
        Entry[] entries = new Entry[0];
        for (Entry entry : entries) {
            long timestamp = (long) entry.getX();
            if (timestamp >= startTimestamp && timestamp <= endTimestamp) {
                filteredEntries.add(entry);
            }
        }
    }
}
