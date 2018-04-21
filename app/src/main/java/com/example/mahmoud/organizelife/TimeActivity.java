package com.example.mahmoud.organizelife;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TimeActivity extends AppCompatActivity {
    final String TAG1 = "Usuful Time";
    final String TAG2 = "Wasting Time";
    private double[] yData;
    private double usefulTime;
    private String[] xData = {TAG1, TAG2};
    ImageButton btAdd;
    DataBaseHelper myDataBase;
    PieChart mDaychart;
    BarChart mWeekChart;
    Button btDelete;
    TextView mInfoPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        myDataBase = new DataBaseHelper(this);
        btAdd = (ImageButton) findViewById(R.id.add);
        btDelete = (Button) findViewById(R.id.delete);
        mDaychart = (PieChart) findViewById(R.id.dayPieChart);
        mWeekChart = (BarChart) findViewById(R.id.weekBarChart);
        mInfoPieChart = (TextView)findViewById(R.id.infoPieChart);
        updateCharts();
        updateTextView();
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUsefuelSubject();
            }
        });
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCharts();
        updateTextView();
    }

    public void delete() {
        myDataBase.deleteAllRows();
    }
    public void updateTextView()
    {
        mInfoPieChart.setText("Usefull Time is: "+usefulTime+"\n unusefull Time is: "+(24-usefulTime));
    }
    int dayofweek(int d, int m, int y) {
        int t[] = {0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4};
        y -= m < 3 ? m : 0;
        return (y + y / 4 - y / 100 + y / 400 + t[m - 1] + d) % 7;
    }

    public void updateCharts() {
        addDataSetPieChart();
        addDateSetBarChart();
    }

    public void addUsefuelSubject() {
        Intent i = new Intent(this, SubjectActivity.class);
        startActivity(i);
    }

    private void addDateSetBarChart() {
        Log.d("Adding", "addDataSet of BarChart started");
        String[] strDays = new String[]{"Friday", "Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thusday"};
        ArrayList<BarEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();
        ArrayList<Pair<String, Double>> data = myDataBase.getAllSubjectForCurrentWeek();
        for (int k = 0;k<7;k++)
        {
            yEntrys.add(new BarEntry(k, 0));
        }
        if (data.size() != 0) {
            for (int i = 0; i < data.size(); i++) {
                String h = data.get(i).first;
                String[] dates = h.split("-");
                int y = Integer.parseInt(dates[0]);
                int m = Integer.parseInt(dates[1]);
                int d = Integer.parseInt(dates[2]);
                int dayOfWeek = dayofweek(y, m, d);
                float x = (float) data.get(i).second.doubleValue();
                yEntrys.set(dayOfWeek,new BarEntry(dayOfWeek,x));
//                if (dayOfWeek != i + 1) {
//                    float x = (float) data.get(i).second.doubleValue();
//                    yEntrys.add(new BarEntry(i, x));
//                } else {
//                    yEntrys.add(new BarEntry(i, 0));
//                }
            }
            for (int j = 0; j< 7;j++)
            {
                if(yEntrys.get(j) == null)
                {
                    yEntrys.set(j,new BarEntry(j,0));
                }
            }
            BarDataSet barDataSet = new BarDataSet(yEntrys, "Subjects Schedule");
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            barDataSet.setDrawValues(true);
            BarData dt = new BarData(barDataSet);
            mWeekChart.setData(dt);
            mWeekChart.invalidate();
            mWeekChart.animateY(500);
        }

    }

    private void addDataSetPieChart() {
        Log.d("Adding", "addDataSet of PieChart started");
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<String>();
        double duartionOfUsfulSubject = myDataBase.getAllDurationForCurrentDay();
        usefulTime = duartionOfUsfulSubject;
        float[] yData = {(float) duartionOfUsfulSubject, (float) (24 - duartionOfUsfulSubject)};
        for (int i = 0; i < yData.length; i++) {
            yEntrys.add(new PieEntry(yData[i], i));
        }

        for (int i = 1; i < xData.length; i++) {
            xEntrys.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Subjects Schedule");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(8);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = mDaychart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        legend.setEnabled(true);
        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        mDaychart.setData(pieData);
        mDaychart.invalidate();
    }

}
