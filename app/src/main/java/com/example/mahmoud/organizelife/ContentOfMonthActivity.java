package com.example.mahmoud.organizelife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ContentOfMonthActivity extends AppCompatActivity {
    ListView mList;
    DataBaseHelperMoney myDataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_of_month);
        mList = (ListView)findViewById(R.id.list_transcations);
        myDataBase = new DataBaseHelperMoney(this);
        ArrayList<row_transcation> rows = myDataBase.getTransactionsForCurrentMonth();
        ListAdapter mListAdabter = new mListResource(this,this,myDataBase);
        mList.setAdapter(mListAdabter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<row_transcation> rows = myDataBase.getTransactionsForCurrentMonth();
        ListAdapter mListAdabter = new mListResource(this,this,myDataBase);
        mList.setAdapter(mListAdabter);
    }
}
