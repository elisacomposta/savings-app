package com.example.savings;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public final static String RECORD_TABLE = "RECORD_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_AMOUNT = "AMOUNT";
    public static final String COLUMN_SUBJECT = "SUBJECT";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_IS_EARNED = "IS_EARNED";
    public static final String COLUMN_DATE = "DATE";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "record.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String str = "CREATE TABLE " +RECORD_TABLE+ "(" +COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_AMOUNT + " INTEGER, " +
                COLUMN_SUBJECT + " STRING, " +
                COLUMN_DESCRIPTION + " STRING, " +
                COLUMN_IS_EARNED + " INTEGER, " +
                COLUMN_DATE + " STRING);";

        db.execSQL(str);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean addOne(Record record){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_AMOUNT, record.getAmount());
        cv.put(COLUMN_SUBJECT, record.getSubject());
        cv.put(COLUMN_DESCRIPTION, record.getDescription());
        cv.put(COLUMN_IS_EARNED, record.getEarned());
        cv.put(COLUMN_DATE, record.getDate());
        return (db.insert(RECORD_TABLE, null, cv)!=-1)? true : false;
    }

    public List<Record> getAll(){
        List<Record> all = new ArrayList<>();
        String query = "SELECT * FROM " +RECORD_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            Record rec;
            do{
                int id = cursor.getInt(0);
                int amount = cursor.getInt(1);
                String subject = cursor.getString(2);
                String description = cursor.getString(3);
                int intIsEarned = cursor.getInt(4);
                String data = cursor.getString(5);

                boolean isEarned = (intIsEarned==1)? true: false;
                rec = new Record(id, amount, subject, description, isEarned, data);
                all.add(rec);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return all;
    }

    public boolean deleteOne(Record record){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + RECORD_TABLE + " WHERE " +
                COLUMN_ID + " = " + record.getId();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            return true;
        } else {
            return false;
        }
    }

    public Record searchRecord(int id){
        Record record;
        String query = "SELECT * FROM " + RECORD_TABLE + " WHERE " + COLUMN_ID + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst()){
            int amount= c.getInt(1);
            String subject = c.getString(2);
            String description = c.getString(3);
            boolean isEarned = c.getInt(4)==1? true : false;
            String date = c.getString(5);

            record = new Record(id, amount, subject, description, isEarned, date);
        } else {
            record = null;
        }
        c.close();
        db.close();
        return record;
    }


}
