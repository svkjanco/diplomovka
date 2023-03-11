package com.example.test2;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.datepicker.MaterialDatePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
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


public class LineChart1 extends AppCompatActivity {

    //importnute data z data.json
    LinkedList<String> timeStampRoundedToMinute = new LinkedList<>();
    LinkedList<Float> received_optical_power = new LinkedList<>();
    LinkedList<Float> avgTemp = new LinkedList<>();

    private LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.datepicker);
        setTitle("AvgTemp");
        get_json(); //nacitanie dat z data.json
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        lineChart = findViewById(R.id.line_chart);

        //vlozenie dat1 do grafu
        ArrayList<Entry> entry1 = new ArrayList<>();
        for (int i = 0; i < timeStampRoundedToMinute.size(); i++) {
            String dateString = timeStampRoundedToMinute.get(i);
            try {
                Date date = sdf.parse(dateString);
                assert date != null;
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

        //vlozenie dat1 do grafu
        ArrayList<Entry> entry2 = new ArrayList<>();
        for (int i = 0; i < timeStampRoundedToMinute.size(); i++) {
            String dateString = timeStampRoundedToMinute.get(i);
            try {
                Date date = sdf.parse(dateString);
                assert date != null;
                float seconds = (float) date.getTime() / 1000;
                entry2.add(new Entry(seconds, received_optical_power.get(i)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        LineDataSet dataSet2 = new LineDataSet(entry2, "Recieved optical power");
        dataSet2.setColor(Color.GREEN);
        dataSet2.setLineWidth(1f);
        dataSet2.setCircleRadius(2f);
        dataSet2.setFillAlpha(65);
        dataSet2.setFillColor(ColorTemplate.getHoloBlue());
        dataSet2.setHighLightColor(Color.YELLOW);
        dataSet2.setDrawCircleHole(false);
        dataSet2.setDrawCircles(false);

        LineData lineData = new LineData(dataSet1,dataSet2);
        lineData.setValueTextSize(15f);
        lineChart.setData(lineData);

        // nastavenie xovej osy
        {
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setValueFormatter(new ValueFormatter() {
                @SuppressLint("SimpleDateFormat")
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
            CustomMarkerView mv = new CustomMarkerView(this,R.layout.custom_marker_view);
            // Set the marker to the chart
            mv.setChartView(lineChart);
            lineChart.setMarker(mv);
        }


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
            updateChartData(lineChart, entry1, entry2, startTimestamp, endTimestamp);
        });
// Show the dialog when the user clicks a button or other UI element
        Button button=findViewById(R.id.date_picker_button);
        button.setOnClickListener(view -> picker.show(getSupportFragmentManager(), "DATE_PICKER"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.actionToggleValues: {
                List<ILineDataSet> sets = lineChart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setDrawValues(!set.isDrawValuesEnabled());
                }

                lineChart.invalidate();
                break;
            }
            case R.id.actionToggleIcons: {
                List<ILineDataSet> sets = lineChart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setDrawIcons(!set.isDrawIconsEnabled());
                }

                lineChart.invalidate();
                break;
            }
            case R.id.actionToggleHighlight: {
                if(lineChart.getData() != null) {
                    lineChart.getData().setHighlightEnabled(!lineChart.getData().isHighlightEnabled());
                    lineChart.invalidate();
                }
                break;
            }
            case R.id.actionToggleFilled: {

                List<ILineDataSet> sets = lineChart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setDrawFilled(!set.isDrawFilledEnabled());
                }
                lineChart.invalidate();
                break;
            }
            case R.id.actionToggleCircles: {
                List<ILineDataSet> sets = lineChart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setDrawCircles(!set.isDrawCirclesEnabled());
                }
                lineChart.invalidate();
                break;
            }
            case R.id.actionToggleCubic: {
                List<ILineDataSet> sets = lineChart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(set.getMode() == LineDataSet.Mode.CUBIC_BEZIER
                            ? LineDataSet.Mode.LINEAR
                            :  LineDataSet.Mode.CUBIC_BEZIER);
                }
                lineChart.invalidate();
                break;
            }
            case R.id.actionToggleStepped: {
                List<ILineDataSet> sets = lineChart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(set.getMode() == LineDataSet.Mode.STEPPED
                            ? LineDataSet.Mode.LINEAR
                            :  LineDataSet.Mode.STEPPED);
                }
                lineChart.invalidate();
                break;
            }
            case R.id.actionToggleHorizontalCubic: {
                List<ILineDataSet> sets = lineChart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(set.getMode() == LineDataSet.Mode.HORIZONTAL_BEZIER
                            ? LineDataSet.Mode.LINEAR
                            :  LineDataSet.Mode.HORIZONTAL_BEZIER);
                }
                lineChart.invalidate();
                break;
            }
            case R.id.actionTogglePinch: {
                lineChart.setPinchZoom(!lineChart.isPinchZoomEnabled());

                lineChart.invalidate();
                break;
            }
            case R.id.actionToggleAutoScaleMinMax: {
                lineChart.setAutoScaleMinMaxEnabled(!lineChart.isAutoScaleMinMaxEnabled());
                lineChart.notifyDataSetChanged();
                break;
            }
            case R.id.animateX: {
                lineChart.animateX(2000);
                break;
            }
            case R.id.animateY: {
                lineChart.animateY(2000, Easing.EaseInCubic);
                break;
            }
            case R.id.animateXY: {
                lineChart.animateXY(2000, 2000);
                break;
            }
        }
        return true;
    }

    // nacitanie json dat
    public void get_json() {
        String json;
        File file = new File(getFilesDir(), "data1.json");
        if (file.exists()) {
            try {
                InputStream is = new FileInputStream(file);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();

                json = new String(buffer, StandardCharsets.UTF_8);
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    timeStampRoundedToMinute.add(obj.getString("timeStampRoundedToMinute"));
                    received_optical_power.add((float) obj.getDouble("received_optical_power"));
                    avgTemp.add((float) obj.getDouble("avgTemp"));
                    System.out.println("Data looaded successfuly 1");
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // updatnutie dat v grafe
    private void updateChartData(LineChart lineChart, List<Entry> chartData1,List<Entry> chartData2, long startTimestamp, long endTimestamp) {
        // Filter the chart data based on the selected date range
        List<Entry> filteredEntries1 = new LinkedList<>();
        for (Entry entry : chartData1) {
            long timestamp = (long) entry.getX();
            if (timestamp >= startTimestamp && timestamp <= endTimestamp) {
                filteredEntries1.add(entry);
            }
        }

        // Create a new LineDataSet and LineData object using the filtered data
        LineDataSet dataSet1 = new LineDataSet(filteredEntries1, "AvgTemp");
        dataSet1.setColor(Color.BLACK);
        dataSet1.setLineWidth(1f);
        dataSet1.setCircleRadius(2f);
        dataSet1.setFillAlpha(65);
        dataSet1.setFillColor(ColorTemplate.getHoloBlue());
        dataSet1.setHighLightColor(Color.rgb(244, 117, 117));
        dataSet1.setDrawCircleHole(false);
        dataSet1.setDrawCircles(false);

        List<Entry> filteredEntries2 = new LinkedList<>();
        for (Entry entry : chartData2) {
            long timestamp = (long) entry.getX();
            if (timestamp >= startTimestamp && timestamp <= endTimestamp) {
                filteredEntries2.add(entry);
            }
        }
        LineDataSet dataSet2 = new LineDataSet(filteredEntries2, "Recieved optical power");
        dataSet2.setColor(Color.GREEN);
        dataSet2.setLineWidth(1f);
        dataSet2.setCircleRadius(2f);
        dataSet2.setFillAlpha(65);
        dataSet2.setFillColor(ColorTemplate.getHoloBlue());
        dataSet2.setHighLightColor(Color.YELLOW);
        dataSet2.setDrawCircleHole(false);
        dataSet2.setDrawCircles(false);

        LineData lineData = new LineData(dataSet1,dataSet2);

        // Set the new LineData object on the chart
        lineChart.setData(lineData);
        // Refresh the chart
        lineChart.invalidate();
    }
}