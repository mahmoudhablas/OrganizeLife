package com.example.mahmoud.organizelife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    Button saveTime,saveMoney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        saveTime = findViewById(R.id.saveTime);
        saveMoney = findViewById(R.id.saveMoney);
        saveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveTime();
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
