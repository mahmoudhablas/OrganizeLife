package com.example.mahmoud.organizelife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mahmoud on 4/16/2018.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "test2";
    private static final String COL1 = "ID";
    private static final String COL2 = "subjectCategory";
    private static final String COL3 = "note";
    private static final String COL4 = "date";
    private static final String COL5 = "duration";


    public DataBaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 +" TEXT, " + COL3 +" TEXT, " + COL4 + " datetime DEFAULT CURRENT_DATE, " + COL5 +" REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean addData(String subjectCategory, String note, double duration) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, subjectCategory);
        contentValues.put(COL3, note);
        contentValues.put(COL5,duration );

        Log.d(TAG, "addData: Adding " + subjectCategory +", " + note +  ", " + duration  + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public ArrayList<row_transcation> getDataForCurrentDay()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        String currentDate = sdf.format(new Date());
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+COL2+", " + COL5 +" FROM " + TABLE_NAME +" WHERE "+COL4+" = '"+currentDate+"'";
        Cursor data = db.rawQuery(query, null);
        ArrayList<row_transcation> da = new ArrayList<row_transcation>();
        while(data.moveToNext())
        {
            row_transcation r = new row_transcation(data.getString(0),data.getDouble(1));
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
    public Cursor getAllDurationsOfADay(String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT SUM("+ COL5 + ") FROM " + TABLE_NAME +" GROUP BY "+COL4+" HAVING "+COL4+" ='"+date+"'";
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
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        String currentDate = sdf.format(new Date());
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME+" WHERE "+COL4 + " = '"+currentDate+"'";
        db.execSQL(query);
    }
    public double getAllDurationForCurrentDay()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        String currentDate = sdf.format(new Date());
        Cursor data = getAllDurationsOfADay(currentDate);
        double durationOfhisDay = 0;
        if(data.moveToNext())
        {
            durationOfhisDay  = data.getDouble(0);
        }
        return durationOfhisDay;
    }
    public ArrayList< Pair<String,Double> > getAllSubjectForCurrentWeek()
    {
        ArrayList< Pair<String,Double> > durationsOfThisWeek = new ArrayList< Pair<String,Double> >();;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+ COL4 + ", SUM("+COL5+") FROM " + TABLE_NAME + " WHERE STRFTIME('%W', "+COL4+") = STRFTIME('%W', 'now') AND STRFTIME('%Y', "+COL4+") = STRFTIME('%Y', 'now')";
        Cursor data = db.rawQuery(query, null);
        while(data.moveToNext()){
            String date = data.getString(0);
            double d = data.getDouble(1);
            durationsOfThisWeek.add(new Pair(date, d));
            Log.d("Duration of " + date + " day = ", "" + d);
            }
        return durationsOfThisWeek;
    }
//    public Cursor getItemID(String name){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
//                " WHERE " + COL2 + " = '" + name + "'";
//        Cursor data = db.rawQuery(query, null);
//        return data;
//    }




}
