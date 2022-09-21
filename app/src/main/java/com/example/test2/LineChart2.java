package com.example.test2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LineChart2 extends AppCompatActivity {

    //importnute data z data.json
    ArrayList<String> timeStampRoundedToMinute = new ArrayList<>();
    ArrayList<Float> avgTempDHT22 = new ArrayList<>();
    ArrayList<Float>  avgHumiDHT22 = new ArrayList<>();

    private LineChart lineChart;
    private List<Entry> entryAvgTempDHT22 = new ArrayList<>();
    private List<Entry> entryAvgHumiDHT22 = new ArrayList<>();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_line1);
        setTitle("AvgTempDHT22, AvgHumiDHT22");
        get_json(); //nacitanie dat z data.json

        // enable scaling and dragging
        {
            lineChart = findViewById(R.id.chart1);
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

        // nastavenie xovej osy
        {
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(timeStampRoundedToMinute));/*for x axis values*/
            xAxis.setLabelCount(timeStampRoundedToMinute.size());
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawLabels(true);    //to hide all xaxis values
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(true);
            xAxis.setLabelCount(3, true);
        }

        //add data to chart
        for(int i=0;i<timeStampRoundedToMinute.size();i++) {
            entryAvgTempDHT22.add(new Entry( i+1, avgTempDHT22.get(i)));
            entryAvgHumiDHT22.add(new Entry( i+1, avgHumiDHT22.get(i)));
        }
        LineDataSet set1, set2;
        // avgTempDHT22
        set1 = new LineDataSet(entryAvgTempDHT22, "AvgTempDHT22");
        set1.setColor(Color.BLACK);
        set1.setLineWidth(2f);
        set1.setCircleRadius(3f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);

        // avgHumiDHT22
        set2 = new LineDataSet(entryAvgHumiDHT22, "AvgHumiDHT22");
        set2.setColor(Color.RED);
        set2.setLineWidth(2f);
        set2.setCircleRadius(3f);
        set2.setFillAlpha(65);
        set2.setFillColor(Color.RED);
        set2.setDrawCircleHole(false);
        set2.setHighLightColor(Color.rgb(244, 117, 117));



        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets
        dataSets.add(set2);

        // create a data object with the datasets
        LineData data = new LineData(dataSets);
        data.setValueTextColor(Color.BLUE);
        data.setValueTextSize(15f);

        // set data
        lineChart.setData(data);

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
        /*    case R.id.actionSave: {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    saveToGallery();
                } else {
                    requestStoragePermission(lineChart);
                }
                break;
            } */
        }
        return true;
    }

    // nacitanie json dat
    public void get_json(){
        String json;
        try {
            InputStream is = getAssets().open("data.json");
            int size=is.available();
            byte[] buffer=new byte[size];
            is.read(buffer);
            is.close();

            json=new String(buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray= new JSONArray(json);

            for(int i=0; i<jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                timeStampRoundedToMinute.add(obj.getString("timeStampRoundedToMinute"));
               /* received_optical_power.add((float) obj.getDouble("received_optical_power"));
                avgTemp.add((float) obj.getDouble("avgTemp"));
                avgPressure.add((float) obj.getDouble("avgPressure")); */
               avgTempDHT22.add((float) obj.getDouble("avgTempDHT22"));
                avgHumiDHT22.add((float) obj.getDouble("avgHumiDHT22"));
              /* avgTempT_ds18b20.add((float) obj.getDouble("avgTempT_ds18b20"));
               avgWindSpeedWU_anemometer.add((float) obj.getDouble("avgWindSpeedWU_anemometer"));
              avgWindVoltageWU_anemometer.add((float) obj.getDouble("avgWindVoltageWU_anemometer"));
               avgGM3G_gp2y1010au0f.add((float) obj.getDouble("avgGM3G_gp2y1010au0f"));
                avgVISIBAAV_miniOFS.add((float) obj.getDouble("avgVISIBAAV_miniOFS"));   */
                System.out.println("Data looaded successfuly");
            }
        }
        catch (IOException | JSONException e){
            e.printStackTrace();
        }
    }
}
