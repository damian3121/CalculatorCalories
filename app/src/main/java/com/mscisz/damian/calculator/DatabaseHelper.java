package com.mscisz.damian.calculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "calories.db";
    private static final String TABLE_NAME = "food_table";
    private static final String F_NAME = "NAME";
    private static final String F_CALORIES = "CALORIES";

    private static final String CREATE_TABLE_FOOD = "CREATE TABLE "
            + TABLE_NAME + " (" + F_NAME + " VARCHAR PRIMARY KEY," + F_CALORIES
            + " INTEGER"+");";

    private static final String DROP_TABLE_FOOD = " DROP TABLE IF EXIST "+TABLE_NAME;

    public DatabaseHelper(Context context) {
        super( context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FOOD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_FOOD);
        onCreate(db);
    }

    public boolean insertData(String pName, Integer pCalories){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(F_NAME, pName);
        contentValues.put(F_CALORIES, pCalories);

        long res = db.insert(TABLE_NAME, null, contentValues);

        if(res == -1){
            return false;
        }else{
            return true;
        }
    }
}
