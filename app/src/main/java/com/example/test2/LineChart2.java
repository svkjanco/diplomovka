package com.example.test2;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
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
    ArrayList<Double> received_optical_power = new ArrayList<>();
    ArrayList<Integer> avgTemp = new ArrayList<>();
    ArrayList<Integer> avgPressure = new ArrayList<>();
    ArrayList<Double> avgTempDHT22 = new ArrayList<>();
    ArrayList<Double>  avgHumiDHT22 = new ArrayList<>();
    ArrayList<Double>  avgTempT_ds18b20 = new ArrayList<>();
    ArrayList<Double>  avgWindSpeedWU_anemometer = new ArrayList<>();
    ArrayList<Double>  avgWindVoltageWU_anemometer = new ArrayList<>();
    ArrayList<Double>  avgGM3G_gp2y1010au0f = new ArrayList<>();
    ArrayList<Double>  avgVISIBAAV_miniOFS = new ArrayList<>();
    //arraylist string to date
    // ArrayList<Date> newtimeStampRoundedToMinute = new ArrayList<>(timeStampRoundedToMinute.size());

    LineChart lineChart;
    LineData lineData;
    List<Entry> entryList = new ArrayList<>();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_line1);

        get_json(); //nacitanie dat z data.json

        setTitle("LineChartActivity");

       /*  //string time to date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try{
        for (String timeStampRoundedToMinute : timeStampRoundedToMinute) {
            // sdf.parse(dateString) - convert the String into a Date accoring the pattern
            // dates.add(...) - add the Date to the list
            newtimeStampRoundedToMinute.add(sdf.parse(timeStampRoundedToMinute));
        }}
        catch (Exception e){
            e.printStackTrace();
        }
            //console vypis ci ukazuje datum
        for(int i=0;i<5;i++){
            Log.i("info", String.valueOf(newtimeStampRoundedToMinute.get(i)));
        } */

        lineChart = findViewById(R.id.chart1);
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
        }

        XAxis xAxis=lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(timeStampRoundedToMinute));/*for x axis values*/
        xAxis.setLabelCount(timeStampRoundedToMinute.size());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);    //to hide all xaxis values
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setLabelCount(2,true);

        //add data to chart
        for(int i=0;i<avgTemp.size();i++) {
            entryList.add(new Entry( i+1,avgTemp.get(i)));
        }
        LineDataSet lineDataSet = new LineDataSet(entryList,"data");
        lineDataSet.setColors(ColorTemplate.getHoloBlue());
        lineDataSet.setLineWidth(2);
        //lineDataSet.setFillAlpha(110);
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.setVisibleXRangeMaximum(100);
        lineChart.invalidate();
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
                received_optical_power.add(obj.getDouble("received_optical_power"));
                avgTemp.add(obj.getInt("avgTemp"));
                avgPressure.add(obj.getInt("avgPressure"));
                avgTempDHT22.add(obj.getDouble("avgTempDHT22"));
                avgHumiDHT22.add(obj.getDouble("avgHumiDHT22"));
                avgTempT_ds18b20.add(obj.getDouble("avgTempT_ds18b20"));
                avgWindSpeedWU_anemometer.add(obj.getDouble("avgWindSpeedWU_anemometer"));
                avgWindVoltageWU_anemometer.add(obj.getDouble("avgWindVoltageWU_anemometer"));
                avgGM3G_gp2y1010au0f.add(obj.getDouble("avgGM3G_gp2y1010au0f"));
                avgVISIBAAV_miniOFS.add(obj.getDouble("avgVISIBAAV_miniOFS"));
                System.out.println("Data looaded successfuly");
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

}
