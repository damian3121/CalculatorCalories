package com.mscisz.damian.calculator;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ActivityShowMealByDate extends AppCompatActivity {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listDataChild;
    private DatabaseHelper myDb = new DatabaseHelper( this );

    private List <String> breakfast = new ArrayList<String>();
    private List <String> elevenses = new ArrayList<String>();
    private List <String> dinner = new ArrayList<String>();
    private List <String> afternoonTea = new ArrayList<String>();
    private List <String> supper = new ArrayList<String>();

    int day;
    int month;
    int year;

    private TextView inputDate;
    private FloatingActionButton fabAddMeal;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_show_meal_by_date );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myDb = new DatabaseHelper(this);

        listView = (ExpandableListView) findViewById( R.id.lvExp);
        inputDate = (TextView) findViewById(R.id.inputDate);
        fabAddMeal = (FloatingActionButton) findViewById( R.id.fabAddMeal );

        initData();
        setActualDate();
        showDialogOnInputClick();
        ShowPopup();

        listAdapter = new ExpandableListAdapter( this, listDataHeader, listDataChild );
        listView.setAdapter( listAdapter );

        viewAllMealByDate("sniadanie", inputDate.getText().toString());
        viewAllMealByDate("sniadanie_2", inputDate.getText().toString());
        viewAllMealByDate("obiad", inputDate.getText().toString());
        viewAllMealByDate("podwieczorek", inputDate.getText().toString());
        viewAllMealByDate("kolacja", inputDate.getText().toString());
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }


    private void setActualDate(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get( Calendar.YEAR );
        month = calendar.get( Calendar.MONTH );
        day = calendar.get( Calendar.DAY_OF_MONTH );

        String date = year + "-" + (month+1) + "-" + day;
        inputDate.setText( date );
    }

    private void initData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add( "Śniadanie" );
        listDataHeader.add( "Drugie śniadanie" );
        listDataHeader.add( "Obiad" );
        listDataHeader.add( "Podwieczorek" );
        listDataHeader.add( "Kolacja" );

        listDataChild.put(listDataHeader.get(0), breakfast);
        listDataChild.put(listDataHeader.get(1), elevenses);
        listDataChild.put(listDataHeader.get(2), dinner);
        listDataChild.put(listDataHeader.get(3), afternoonTea);
        listDataChild.put(listDataHeader.get(4), supper );
    }

    public void viewAllMealByDate(String typeMeal, String date){
        Cursor cursor = myDb.getMealByDate(typeMeal, date);
        String result_name = "";
        int result_calories = 0;

        while (cursor.moveToNext()) {
            result_name = "";

            result_name = cursor.getString( 0 );
            result_calories = cursor.getInt( 1 );

            switch(typeMeal){
                case "sniadanie":
                    breakfast.add( result_name + " " + result_calories + " kalorii" );
                    break;
                case "sniadanie_2":
                    elevenses.add( result_name + " " + result_calories + " kalorii" );
                    break;
                case "obiad":
                    dinner.add( result_name + " " + result_calories + " kalorii" );
                    break;
                case "podwieczorek":
                    afternoonTea.add( result_name + " " + result_calories + " kalorii" );
                    break;
                case "kolacja":
                    supper.add( result_name + " " + result_calories + " kalorii" );
                    break;
            }
        }
    }

    private void showDialogOnInputClick(){
        inputDate.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(
                        ActivityShowMealByDate.this,
                        android.R.style.Theme_Material_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.show();
            }
        } );

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int pYear, int pMonth, int pDayOfMonth) {
                pMonth = pMonth + 1;
                String date = pYear + "-" + pMonth + "-" + pDayOfMonth;
                inputDate.setText( date );

                breakfast.clear();
                elevenses.clear();
                dinner.clear();
                afternoonTea.clear();
                supper.clear();
            }
        };
    }

    public void ShowPopup(){
        fabAddMeal.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddMeal = new Intent(getApplicationContext(), ActivityAddMeal.class);
                startActivity(intentAddMeal);
            }
        } );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void removeMeal(){
//        listDataChild.
    }
}
