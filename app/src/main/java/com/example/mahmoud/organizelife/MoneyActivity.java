package com.example.mahmoud.organizelife;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

public class MoneyActivity extends AppCompatActivity {
    final String TAG1 = "Income";
    final String TAG2 = "Expense";
    private double[] yData;
    private double income;
    private String[] xData = {TAG1, TAG2};
    ImageButton btAdd;
    DataBaseHelperMoney myDataBase;
    PieChart mDaychart;
    BarChart mWeekChart;
    Button btDelete,mShowTransactions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        myDataBase = new DataBaseHelperMoney(this);
        btAdd = (ImageButton) findViewById(R.id.add);
        btDelete = (Button) findViewById(R.id.delete);
        mShowTransactions = (Button)findViewById(R.id.showSubjects);
        mDaychart = (PieChart) findViewById(R.id.dayPieChart);
        mWeekChart = (BarChart) findViewById(R.id.weekBarChart);
        updateCharts();
        mShowTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTransactions();
            }
        });
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExpenseOrIncome();
            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();
        updateCharts();
    }
    public void showTransactions()
    {
        Intent i = new Intent(this , ContentOfMonthActivity.class);
        startActivity(i);
    }
    public void addExpenseOrIncome() {
        Intent i = new Intent(this, IncomeAndExpenseActivity.class);
        startActivity(i);
    }
    public void updateCharts() {
        addDataSetPieChart();
        addDateSetBarChart();
    }
    private void addDateSetBarChart() {
        Log.d("Adding", "addDataSet of BarChart started");
        String[] strDays = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thusday","Friday", "Saturday" };
        ArrayList<BarEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<String>();
        ArrayList<Pair<Integer, Double>> data = myDataBase.getAllTransactionForCurrentWeek();
        for (int i = 0; i < strDays.length; i++) {
            xEntrys.add(strDays[i]);
        }
        for (int k = 0;k<7;k++)
        {
            yEntrys.add(new BarEntry(0,k));
        }
        if (data.size() != 0) {
            for (int i = 0; i < data.size(); i++) {
                int dayNum = data.get(i).first;
                float x = (float) data.get(i).second.doubleValue();
                yEntrys.set(dayNum,new BarEntry(x,dayNum));
            }
            BarDataSet barDataSet = new BarDataSet(yEntrys, "Subjects Schedule");
            barDataSet.setDrawValues(true);
            BarData dt = new BarData(xEntrys,barDataSet);
            mWeekChart.setData(dt);
            mWeekChart.invalidate();
            mWeekChart.animateY(500);
        }

    }
    private void addDataSetPieChart() {
        Log.d("Adding", "addDataSet of PieChart started");
        ArrayList<Entry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<String>();
        Pair<Double,Double> incomeAndExpense = myDataBase.getAllExpensesAndIncomeForMonth();
        float[] yData = {(float)incomeAndExpense.first.doubleValue(), (float) incomeAndExpense.second.doubleValue()};
        for (int i = 0; i < yData.length; i++) {
            yEntrys.add(new Entry(yData[i], i));
        }

        for (int i = 0; i < xData.length; i++) {
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
        PieData pieData = new PieData(xEntrys,pieDataSet);
        mDaychart.setData(pieData);
        mDaychart.setUsePercentValues(true);
        mDaychart.invalidate();
    }

}


