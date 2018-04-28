package com.example.mahmoud.organizelife;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

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
    Button mShowSubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        myDataBase = new DataBaseHelper(this);
        btAdd = (ImageButton) findViewById(R.id.add);
        mShowSubjects = (Button)findViewById(R.id.showSubjects);
        mDaychart = (PieChart) findViewById(R.id.dayPieChart);
        mWeekChart = (BarChart) findViewById(R.id.weekBarChart);
        updateCharts();
        mShowSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showSubjects();
            }
        });
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUsefuelSubject();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCharts();
    }
    public void showSubjects()
    {
        Intent i = new Intent(this , ContentOFDayActivity.class);
        startActivity(i);
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
        String[] strDays = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thusday","Friday", "Saturday" };
        ArrayList<BarEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<String>();
        ArrayList<Pair<Integer, Double>> data = myDataBase.getAllSubjectForCurrentWeek();
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
            mWeekChart.setDescriptionTextSize(16f);
            mWeekChart.setDescriptionPosition(1050,650);
            mWeekChart.getLegend().setTextSize(16f);
            mWeekChart.setData(dt);
            mWeekChart.invalidate();
            mWeekChart.setDescription("BarChart for this week ");
            mWeekChart.animateY(500);
        }

    }

    private void addDataSetPieChart() {
        Log.d("Adding", "addDataSet of PieChart started");
        ArrayList<Entry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<String>();
        double duartionOfUsfulSubject = myDataBase.getAllDurationForCurrentDay();
        usefulTime = duartionOfUsfulSubject;
        float[] yData = {(float) duartionOfUsfulSubject, (float) (24 - duartionOfUsfulSubject)};
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
        mDaychart.setDescription("PieChart for this day");
        mDaychart.setDescriptionPosition(1050,550);
        mDaychart.setDescriptionTextSize(16f);
        mDaychart.getLegend().setTextSize(16f);
        mDaychart.getLegend().setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        mDaychart.setData(pieData);
        mDaychart.setUsePercentValues(true);
        mDaychart.invalidate();
    }

}
class ListResource extends ArrayAdapter<row_transcation>{

    DataBaseHelper mDataBaseTime;
    DataBaseHelperMoney mDataBaseMoney;
    public ListResource(@NonNull Context context, ArrayList<row_transcation> resource) {
        super(context,R.layout.row_transaction ,resource);
        mDataBaseMoney=null;
        mDataBaseTime=null;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater m = LayoutInflater.from(getContext());
        View customView = m.inflate(R.layout.row_transaction,parent,false);
        final row_transcation da = getItem(position);
        TextView subject = (TextView) customView.findViewById(R.id.subject);
        TextView duration = (TextView)customView.findViewById(R.id.duration);
        Button delete = (Button) customView.findViewById(R.id.delete);
        Button edit = (Button) customView.findViewById(R.id.edit);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataBaseTime != null)
                {
                  mDataBaseTime.delete(da.getId());
                }else if(mDataBaseMoney != null)
                {
                    mDataBaseMoney.delete(da.getId());
                }
                notifyDataSetChanged();

            }
        });
        if (da.getType().equals("income"))
        {
            mDataBaseMoney = new DataBaseHelperMoney(this.getContext());
            customView.setBackgroundColor(Color.parseColor("#e70f0f"));

        }else if(da.getType().equals("expense"))
        {
            mDataBaseMoney = new DataBaseHelperMoney(this.getContext());
            customView.setBackgroundColor(Color.parseColor("#33FF42"));
        }else{
            mDataBaseTime = new DataBaseHelper(this.getContext());
        }
        subject.setText(da.getSubject());
        duration.setText(""+da.getDuration());

        return customView;
    }
}
class mListResource extends BaseAdapter{

    ArrayList<row_transcation>rows;
    Context context;
    DataBaseHelper myDataBaseTime;
    DataBaseHelperMoney myDataBaseMoney;
    Activity mActivity;
    mListResource(Context cont ,Activity a,DataBaseHelper myDataBaseTime)
    {
        this.context = cont;
        myDataBaseTime = new DataBaseHelper(this.context);
        this.mActivity = a;
        rows = myDataBaseTime.getDataForCurrentDay();
    }
    public void navigateToTimeActivity(String subject,String duration,String note,int id )
    {
        Intent i = new Intent(this.mActivity, SubjectActivity.class);
        i.putExtra("subject",subject);
        i.putExtra("duration",duration);
        i.putExtra("note",note);
        i.putExtra("id",id);
        this.mActivity.startActivity(i);
    }
    public void navigateToMoneyActivity(String transaction ,String amount ,String note,int id,String type)
    {
        Intent i = new Intent(this.mActivity, IncomeAndExpenseActivity.class);
        i.putExtra("transaction",transaction);
        i.putExtra("amount",amount);
        i.putExtra("note",note);
        i.putExtra("id",id);
        i.putExtra("type",type);
        this.mActivity.startActivity(i);
    }
    mListResource(Context cont ,Activity a,DataBaseHelperMoney myDataBaseMoney)
    {
        this.context = cont;
        this.mActivity = a;
        myDataBaseMoney = new DataBaseHelperMoney(this.context);
        rows = myDataBaseMoney.getTransactionsForCurrentMonth();
    }
    @Override
    public int getCount() {
        return rows.size();
    }

    @Override
    public row_transcation getItem(int position) {
        return rows.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rows.indexOf(getItem(position));

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_transaction, parent, false);
        final  row_transcation da = getItem(position);
        final TextView subject = (TextView) convertView.findViewById(R.id.subject);
        final TextView duration = (TextView)convertView.findViewById(R.id.duration);
        final TextView note = (TextView)convertView.findViewById(R.id.note);
        final TextView type = (TextView)convertView.findViewById(R.id.type);
        Button delete = (Button) convertView.findViewById(R.id.delete);
        Button edit = (Button) convertView.findViewById(R.id.edit);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDataBaseTime != null)
                {
                    myDataBaseTime.delete(da.getId());
                    rows = myDataBaseTime.getDataForCurrentDay();
                    notifyDataSetChanged();
                }else if(myDataBaseMoney != null)
                {
                    myDataBaseMoney.delete(da.getId());
                    rows = myDataBaseMoney.getTransactionsForCurrentMonth();
                    notifyDataSetChanged();
                }


            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDataBaseTime != null)
                {
                    row_transcation data = myDataBaseTime.getData(da.getId());
                    navigateToTimeActivity(data.getSubject(),""+data.getDuration(),data.getNote(),da.getId());
                }else if(myDataBaseMoney != null)
                {
                    row_transcation data = myDataBaseMoney.getData(da.getId());
                    navigateToMoneyActivity(data.getSubject(),""+data.getDuration(),data.getNote(),da.getId(),da.getType());
                }

            }
        });
        if (da.getType().equals("income"))
        {
            myDataBaseMoney = new DataBaseHelperMoney(this.context);
            type.setText("Income");
            duration.setText(""+da.getDuration());

        }else if(da.getType().equals("expense"))
        {
            myDataBaseMoney = new DataBaseHelperMoney(this.context);
            type.setText("expense");
            duration.setText(""+da.getDuration());
        }else{
            myDataBaseTime = new DataBaseHelper(this.context);
            type.setText("");
            duration.setText(""+da.getDuration()+"h");
        }
        subject.setText(da.getSubject());

        return convertView;
    }
}
