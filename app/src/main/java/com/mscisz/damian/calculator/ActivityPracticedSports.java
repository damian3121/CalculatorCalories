package com.mscisz.damian.calculator;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityPracticedSports extends AppCompatActivity {

    private EditText inputDateAddDailySport;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    DatabaseHelper myDb;
    private AutoCompleteTextView autoCompleteSearchActivities;
    private List<String> allSports = new ArrayList<>();
    private TextView confirmAddDailySport;
    private EditText amountMinutesOfSports;

    int day;
    int month;
    int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_practiced_sports );

        inputDateAddDailySport = (EditText) findViewById( R.id.inputDateAddDailySport );
        myDb = new DatabaseHelper(this);
        autoCompleteSearchActivities = (AutoCompleteTextView) findViewById( R.id.autoCompleteSearchActivities );
        confirmAddDailySport = (TextView) findViewById( R.id.confirmAddDailySport );
        amountMinutesOfSports = (EditText) findViewById( R.id.amountMinutesOfSports );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showCalendarDialogAndSetDate();
        setActualDate();
        autoCompleteInputDailyActivities();
        addDailySports();
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

    private void showCalendarDialogAndSetDate(){
        inputDateAddDailySport.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(
                        ActivityPracticedSports.this,
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
                inputDateAddDailySport.setText( date );
            }
        };
    }

    private void setActualDate(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get( Calendar.YEAR );
        month = calendar.get( Calendar.MONTH );
        day = calendar.get( Calendar.DAY_OF_MONTH );

        String date = year + "-" + (month+1) + "-" + day;
        inputDateAddDailySport.setText( date );
    }

    private void autoCompleteInputDailyActivities(){
        Cursor cursor = myDb.getAllSportsActivities();

        int i = 0;
        while (cursor.moveToNext()) {
            allSports.add( cursor.getString( 0 ) );
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allSports );
        autoCompleteSearchActivities.setAdapter( adapter );
    }

    private void addDailySports(){

        confirmAddDailySport.setOnClickListener( new View.OnClickListener() {
            boolean result = false;
            @Override
            public void onClick(View v) {
                // alert
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityPracticedSports.this);
                builder.setCancelable( true );
                if (!allSports.contains(autoCompleteSearchActivities.getText().toString())) {
                    builder.setTitle( "Uwaga" );
                    builder.setMessage( "Najpierw wprowadź aktywność sportową" );
                    builder.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    } );
                    builder.show();
                } else {

                    if(amountMinutesOfSports.getText().length() != 0 &&
                            autoCompleteSearchActivities.getTextSize() != 0){
                        int caloriesValue = Integer.parseInt( amountMinutesOfSports.getText().toString() )
                                * myDb.getCaloriesForSportsActivity(
                                autoCompleteSearchActivities.getText().toString() );

                        result = myDb.insertDataToDailySportActivity ( inputDateAddDailySport.getText().toString(),
                                autoCompleteSearchActivities.getText().toString(),
                                caloriesValue/10 );

                        autoCompleteSearchActivities.setText( "" );
                        amountMinutesOfSports.setText( "" );
                    }

                    builder.setTitle( "Uwaga" );
                    builder.setMessage( "Aktywność została dodana" );
                    builder.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    } );
                    builder.show();
                }
            }
        } );
    }
}
