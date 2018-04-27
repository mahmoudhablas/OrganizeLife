package com.example.mahmoud.organizelife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by Mahmoud on 4/27/2018.
 */

public class DataBaseHelperMoney extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "test3";
    private static final String COL1 = "ID";
    private static final String COL2 = "transactionCategory";
    private static final String COL3 = "note";
    private static final String COL4 = "date";
    private static final String COL5 = "amount";
    private static final String COL6 = "type";


    public DataBaseHelperMoney(Context context) {
        super(context, TABLE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 +" TEXT, " + COL3 +" TEXT, " + COL4 + " datetime DEFAULT CURRENT_DATE, " + COL5 +" REAL, "+COL6 +" TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void delete(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME +" WHERE "+COL1+" = "+id;
        db.execSQL(query);
    }
    public void editData(int id,String transaction,String amount, String note,String type)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE "+TABLE_NAME+" SET "+COL2 +"='"+transaction+"', "+COL5+"='"+amount+"',"+COL3+"='"+note+"',"+COL6+"='"+type+"' WHERE "+COL1+"= "+ id;
        db.execSQL(query);
    }
    public boolean addData(String transactionCategory, String note, double amount,String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, transactionCategory);
        contentValues.put(COL3, note);
        contentValues.put(COL5,amount );
        contentValues.put(COL6,type);

        Log.d(TAG, "addData: Adding " + transactionCategory +", " + note +  ", " + amount  + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public row_transcation getData(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+COL1 +", "+COL2+", "+COL3+", "+COL5+"," +COL6+" FROM " + TABLE_NAME + " WHERE "+COL1+ " = "+id;
        Cursor data = db.rawQuery(query, null);
        row_transcation r = new row_transcation("",0,0);
        if(data.moveToNext())
        {
            int ID = data.getInt(0);
            String transaction = data.getString(1);
            String note = data.getString(2);
            double amount = data.getDouble(3);
            String type = data.getString(4);
            r = new row_transcation(ID,transaction,amount,note,type);
        }
        return r;

    }
    public ArrayList<row_transcation> getTransactionsForCurrentMonth()
     {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+COL2+", " + COL5 +", "+COL6+", "+COL1+" FROM " + TABLE_NAME +" WHERE STRFTIME('%Y %m', "+COL4+") = STRFTIME('%Y %m', 'now')";
        Cursor data = db.rawQuery(query, null);
        ArrayList<row_transcation> da = new ArrayList<row_transcation>();
        while(data.moveToNext())
        {
            String transaction = data.getString(0);
            double amount = data.getDouble(1);
            String type = data.getString(2);
            int id = data.getInt(3);
            row_transcation r = new row_transcation(transaction,amount,type,id);
            da.add(r);
        }
        return da;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteAllRows()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME;
        db.execSQL(query);
    }

    public void deleteAllRowsForCurrent()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME+" WHERE STRFTIME('%Y %M %D', "+COL4+") = STRFTIME('%Y %M %D', 'now')";
        db.execSQL(query);
    }

    public Pair<Double,Double> getAllExpensesAndIncomeForMonth()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT SUM("+ COL5 + ") FROM " + TABLE_NAME +" WHERE "+COL6+" = 'income' GROUP BY "+COL4+" HAVING STRFTIME('%Y %m', "+COL4+") = STRFTIME('%Y %m', 'now')";
        Cursor data = db.rawQuery(query, null);
        double income = 0;
        if(data.moveToNext())
        {
            income  = data.getDouble(0);
        }
        String query1 = "SELECT SUM("+ COL5 + ") FROM " + TABLE_NAME +" WHERE "+COL6+" = 'expense' GROUP BY "+COL4+" HAVING STRFTIME('%Y %m', "+COL4+") = STRFTIME('%Y %m', 'now')";
        Cursor data1 = db.rawQuery(query1, null);
        double expense = 0;
        if(data1.moveToNext())
        {
            expense  = data1.getDouble(0);
        }
        return new Pair(new Double(income),new Double(expense));
    }

    public ArrayList<Pair<Integer,Double>> getAllTransactionForCurrentWeek()
    {
        ArrayList< Pair<Integer,Double> > durationsOfThisWeek = new ArrayList< Pair<Integer,Double> >();;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT STRFTIME('%w',"+ COL4 + "), SUM("+COL5+") FROM " + TABLE_NAME + " WHERE "+COL6+" = 'income' GROUP BY "+COL4+" HAVING STRFTIME('%W', "+COL4+") = STRFTIME('%W', 'now') AND STRFTIME('%Y', "+COL4+") = STRFTIME('%Y', 'now')";
        Cursor data = db.rawQuery(query, null);
        while(data.moveToNext()){
            int day = data.getInt(0);
            double d = data.getDouble(1);
            durationsOfThisWeek.add(new Pair(new Integer(day), d));
            Log.d("Duration of " + day+ " day = ", "" + d);
        }
        return durationsOfThisWeek;
    }
}

