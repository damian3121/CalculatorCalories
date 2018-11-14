package com.mscisz.damian.calculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // column of the product table
    private static final String DATABASE_NAME = "calories.db";
    private static final String TABLE_F_NAME = "food_table";
    private static final String F_NAME = "NAME";
    private static final String F_CALORIES = "CALORIES";

    // column of the meal table
    private static final String TABLE_MEAL_NAME = "meal_table";
    private static final String MEAL_ID = "ID";
    private static final String MEAL_DATE = "DATE";
    private static final String MEAL_TYPE = "MEAL_TYPE";
    private static final String FOOD_NAME= F_NAME;

    private static final String CREATE_TABLE_FOOD = "CREATE TABLE "
            + TABLE_F_NAME + " (" + F_NAME + " VARCHAR PRIMARY KEY," + F_CALORIES
            + " INTEGER"+");";

    private static final String CREATE_TABLE_MEAL= "CREATE TABLE "
            + TABLE_MEAL_NAME + " (" + MEAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + MEAL_DATE
            + " DATE," + MEAL_TYPE + " VARCHAR," + FOOD_NAME + " VARCHAR," + " FOREIGN KEY(" + FOOD_NAME + ") REFERENCES "
            + TABLE_F_NAME + "(" + F_NAME + ")" +");";

    private static final String DROP_TABLE_FOOD = " DROP TABLE IF EXIST "+ TABLE_F_NAME;
    private static final String DROP_TABLE_MEAL = " DROP TABLE IF EXIST "+ TABLE_MEAL_NAME;

    public DatabaseHelper(Context context) {
        super( context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FOOD);
        db.execSQL(CREATE_TABLE_MEAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_FOOD);
        db.execSQL(DROP_TABLE_MEAL);
        onCreate(db);
    }

    public boolean insertData(String pName, Integer pCalories){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(F_NAME, pName);
        contentValues.put(F_CALORIES, pCalories);

        long res = db.insert( TABLE_F_NAME, null, contentValues);

        if(res == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getMealByDate(String typeMeal, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( " select name from " + TABLE_MEAL_NAME
                + " where date='" + date + "' and meal_type=" + "'" + typeMeal + "' ", null );

        return cursor;
    }
}
