package com.example.mahmoud.organizelife;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SubjectActivity extends AppCompatActivity {

    EditText mNote,mSubject,mDuration ;
    Button btAdd;
    DataBaseHelper myDataBase;
    int id = 0;
    boolean isEdit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        mNote = (EditText)findViewById(R.id.note);
        mSubject = (EditText)findViewById(R.id.subject);
        mDuration = (EditText)findViewById(R.id.duration);
        btAdd = (Button) findViewById(R.id.add);
        myDataBase = new DataBaseHelper(this);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubject();
            }
        });
        isEdit = false;
        Intent info = getIntent();
        if (info.getStringExtra("subject") != null)
        {
            String subject = info.getStringExtra("subject");
            String duration = info.getStringExtra("duration");
            String note = info.getStringExtra("note");
            id = info.getIntExtra("id",0);
            isEdit = true;
            btAdd.setText("Edit");
            setTexts(subject,duration,note);
        }
    }
    public void setTexts(String subject,String duration, String note)
    {
        mSubject.setText(subject);
        mDuration.setText(duration);
        mNote.setText(note);
    }
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
    public void addSubject()
    {
        String note = mNote.getText().toString();
        String subject = mSubject.getText().toString();
        double duration = Double.parseDouble(mDuration.getText().toString());
        double currentDuration = myDataBase.getAllDurationForCurrentDay() + duration;
        if(currentDuration > 24)
        {
            toastMessage("Duration mustn't be more than 24");
            return;
        }
        if (subject.isEmpty() || duration == 0)
        {
            toastMessage("Subject and Duration Fields are required");
            return;
        }
        if(!isEdit)
        {
            boolean result = myDataBase.addData(subject,note,duration);
            if (result)
            {
                Log.d("Result ","Is OK ");
            }
            Intent i = new Intent(this,TimeActivity.class);
            startActivity(i);
        }else{
            myDataBase.editData(id,subject,duration+"",note);

            Intent i = new Intent(this,ContentOFDayActivity.class);
            startActivity(i);
        }

    }

}
