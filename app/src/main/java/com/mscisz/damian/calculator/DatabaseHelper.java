package com.mscisz.damian.calculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    // column of the product table
    private static final String DATABASE_NAME = "calories_2.db";
    private static final String TABLE_F_NAME = "food_table";
    private static final String F_NAME = "NAME";
    private static final String F_CALORIES = "CALORIES";

    // column of the meal table
    private static final String TABLE_MEAL_NAME = "meal_table";
    private static final String MEAL_ID = "ID";
    private static final String MEAL_DATE = "DATE";
    private static final String MEAL_TYPE = "MEAL_TYPE";
    private static final String MEAL_AMOUNT = "ALL_CALORIES_AMOUNT";
    private static final String FOOD_NAME= F_NAME;

    // column of the activity table
    private static final String TABLE_ACTIVITIES_NAME = "activities_table";
    private static final String ALL_AMOUNT_ACTIVITIES = "ALL_CALORIES_AMOUNT";
    private static final String ACTIVITY_NAME= "ACTIVITY_NAME";

    // column of the sports table
    private static final String TABLE_DAILY_SPORTS_NAME = "daily_sports_table";
    private static final String DAILY_SPORTS_ID = "ID";
    private static final String DAILY_SPORTS_DATE = "DAILY_SPORTS_DATE";
    private static final String SPORTS_CALORIES_AMOUNT = "ALL_CALORIES_AMOUNT";
    private static final String SPORTS_NAME = ACTIVITY_NAME;

    private static final String CREATE_TABLE_FOOD = "CREATE TABLE "
            + TABLE_F_NAME + " (" + F_NAME + " VARCHAR PRIMARY KEY," + F_CALORIES
            + " INTEGER"+");";

    private static final String CREATE_TABLE_MEAL= "CREATE TABLE "
            + TABLE_MEAL_NAME + " (" + MEAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + MEAL_DATE
            + " DATE," + MEAL_TYPE + " VARCHAR," + FOOD_NAME + " VARCHAR," + MEAL_AMOUNT + " INTEGER, " + " FOREIGN KEY(" + FOOD_NAME + ") REFERENCES "
            + TABLE_F_NAME + "(" + F_NAME + ")" +");";

    private static final String CREATE_TABLE_ACTIVITIES = "CREATE TABLE "
            + TABLE_ACTIVITIES_NAME + " (" + ACTIVITY_NAME + " VARCHAR PRIMARY KEY,"
            + ALL_AMOUNT_ACTIVITIES + " INTEGER" +");";

    private static final String CREATE_TABLE_DAILY_SPORTS = "CREATE TABLE "
            + TABLE_DAILY_SPORTS_NAME + " (" + DAILY_SPORTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + DAILY_SPORTS_DATE
            + " DATE," + SPORTS_NAME + " VARCHAR," + SPORTS_CALORIES_AMOUNT + " INTEGER, " + " FOREIGN KEY(" + SPORTS_NAME + ") REFERENCES "
            + TABLE_ACTIVITIES_NAME + "(" + ACTIVITY_NAME + ")" +");";

    private static final String DROP_TABLE_FOOD = " DROP TABLE IF EXIST "+ TABLE_F_NAME;
    private static final String DROP_TABLE_MEAL = " DROP TABLE IF EXIST "+ TABLE_MEAL_NAME;
    private static final String DROP_TABLE_ACTIVITIES = " DROP TABLE IF EXIST "+ TABLE_ACTIVITIES_NAME;
    private static final String DROP_TABLE_DAILY_SPORTS = " DROP TABLE IF EXIST "+ TABLE_DAILY_SPORTS_NAME;

    public DatabaseHelper(Context context) {
        super( context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FOOD);
        db.execSQL(CREATE_TABLE_MEAL);
        db.execSQL( CREATE_TABLE_ACTIVITIES );
        db.execSQL( CREATE_TABLE_DAILY_SPORTS );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_FOOD);
        db.execSQL(DROP_TABLE_MEAL);
        db.execSQL( DROP_TABLE_ACTIVITIES );
        db.execSQL( DROP_TABLE_DAILY_SPORTS );
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

    public boolean insertDataToMealTable(String pDate, String pMealType, String pProductName, Integer pAmount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEAL_DATE, pDate);
        contentValues.put(MEAL_AMOUNT, pAmount);
        contentValues.put(FOOD_NAME, pProductName);

        switch(pMealType){
            case "Śniadanie":
                contentValues.put(MEAL_TYPE, "sniadanie");
                break;
            case "Drugie śniadanie":
                contentValues.put(MEAL_TYPE, "sniadanie_2");
                break;
            default:
                contentValues.put(MEAL_TYPE, pMealType.toLowerCase());
                break;
        }

        long res = db.insert( TABLE_MEAL_NAME, null, contentValues);

        if(res == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean insertDataToActivityTable( String pName, Integer pCalories){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACTIVITY_NAME, pName);
        contentValues.put(ALL_AMOUNT_ACTIVITIES, pCalories);

        long res = db.insert( TABLE_ACTIVITIES_NAME, null, contentValues);

        if(res == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean insertDataToDailySportActivity( String pDate, String pName, Integer pCalories){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DAILY_SPORTS_DATE, pDate);
        contentValues.put(SPORTS_NAME, pName);
        contentValues.put(SPORTS_CALORIES_AMOUNT, pCalories);

        long res = db.insert( TABLE_DAILY_SPORTS_NAME, null, contentValues);

        if(res == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getMealByDate(String typeMeal, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( " select name, all_calories_amount from " + TABLE_MEAL_NAME
                + " where date='" + date + "' and meal_type=" + "'" + typeMeal + "' ", null );

        return cursor;
    }

    public Cursor getAllProducts(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( " select name from " + TABLE_F_NAME , null );

        return cursor;
    }

    public Cursor getAllSportsActivities(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( " select activity_name from " + TABLE_ACTIVITIES_NAME , null );

        return cursor;
    }

    public int getCaloriesForProductByName(String mealName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( " select calories from " + TABLE_F_NAME +
                " where name = '" + mealName + "' ", null );
        cursor.moveToNext();

        return cursor.getInt( 0 );
    }

    public int getCaloriesForSportsActivity(String sportName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( " select all_calories_amount from " + TABLE_ACTIVITIES_NAME +
                " where activity_name = '" + sportName + "' ", null );
        cursor.moveToNext();

        return cursor.getInt( 0 );
    }

    public void removeMeal(String name, String calories){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = " DELETE FROM " + TABLE_MEAL_NAME +
                " WHERE name='" + name + "' AND all_calories_amount='" + calories + "' ";
        db.execSQL( query );
    }

    public int getSubstractCaloriesFromProductByDate(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( "select sum(all_calories_amount) from " + TABLE_MEAL_NAME +
                " where date='" + date + "'", null );

        cursor.moveToNext();
        return cursor.getInt( 0 );
    }
}
