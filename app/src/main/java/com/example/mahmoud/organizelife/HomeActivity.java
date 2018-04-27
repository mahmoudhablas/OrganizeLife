package com.example.mahmoud.organizelife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {
    ImageButton saveTime,saveMoney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        saveTime = (ImageButton) findViewById(R.id.saveTime);
        saveMoney = (ImageButton)findViewById(R.id.saveMoney);
        saveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveTime();
            }
        });
        saveMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMoney();
            }
        });

    }

    public void SaveMoney()
    {
        Intent i = new Intent(this,MoneyActivity.class);
        startActivity(i);
    }
    public void SaveTime()
    {
        Intent i = new Intent(this,TimeActivity.class);
        startActivity(i);
    }
}
