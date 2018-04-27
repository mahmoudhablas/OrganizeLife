package com.example.mahmoud.organizelife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class IncomeAndExpenseActivity extends AppCompatActivity {
    EditText mNote,mSubject,mDuration ;
    RadioButton rbIncome,rbExpense;
    Button btAdd;
    String type="expense";
    int id = 0;
    boolean isEdit =  false;
    DataBaseHelperMoney myDataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_and_expense);
        mNote = (EditText)findViewById(R.id.note);
        mSubject = (EditText)findViewById(R.id.transaction);
        mDuration = (EditText)findViewById(R.id.amount);
        btAdd = (Button) findViewById(R.id.add);
        myDataBase = new DataBaseHelperMoney(this);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubject();
            }
        });
        rbIncome = (RadioButton)findViewById(R.id.income);
        rbExpense = (RadioButton)findViewById(R.id.expense);
        Intent info = getIntent();
        if (info.getStringExtra("transaction") != null)
        {
            String transaction = info.getStringExtra("transaction");
            String amount = info.getStringExtra("amount");
            String note = info.getStringExtra("note");
            id = info.getIntExtra("id",0);
            String type = info.getStringExtra("type");
            isEdit = true;
            btAdd.setText("Edit");
            setTexts(transaction,amount,note,type);
        }
    }
    public void setTexts(String transaction,String amount,String note,String type)
    {
        mSubject.setText(transaction);
        mDuration.setText(amount);
        mNote.setText(note);
        if (type.equals("income"))
        {
            rbIncome.setChecked(true);
            rbExpense.setChecked(false);
        }else{
            rbIncome.setChecked(false);
            rbExpense.setChecked(true);
        }
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.income:
                if (checked)
                    type="income";
                    break;
            case R.id.expense:
                if (checked)
                    type="expense";
                    break;
        }
    }
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
    public void addSubject()
    {
        String note = mNote.getText().toString();
        String transaction = mSubject.getText().toString();
        double amount = Double.parseDouble(mDuration.getText().toString());
        if(!isEdit)
        {
            boolean result = myDataBase.addData(transaction,note,amount,type);
            if (result)
            {
                Log.d("Result ","Is OK ");
            }
            Intent i = new Intent(this,MoneyActivity.class);
            startActivity(i);
        }else{
            String t;
            if(rbIncome.isChecked())
            {
                t = "income";
            }else{
                t = "expense";
            }
            myDataBase.editData(id,transaction,amount+"",note,t);
            Intent i = new Intent(this,ContentOfMonthActivity.class);
            startActivity(i);
        }


    }
}
