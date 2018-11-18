package com.mscisz.damian.calculator;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import fr.ganfra.materialspinner.MaterialSpinner;

public class ActivityAddMeal extends AppCompatActivity {

    private MaterialSpinner spinnerTypeOfMeal;
    private TextView inputDateAddMeal;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private AutoCompleteTextView autoCompleteSearchProduct;
    private EditText amountOfMeal;
    DatabaseHelper myDb;

    int day;
    int month;
    int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_meal );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinnerTypeOfMeal = (MaterialSpinner) findViewById( R.id.spinnerTypeOfMeal );
        inputDateAddMeal = (TextView) findViewById( R.id.inputDateAddMeal );
        autoCompleteSearchProduct = (AutoCompleteTextView) findViewById( R.id.autoCompleteSearchProduct );
        amountOfMeal = (EditText) findViewById( R.id.amountOfMeal );
        myDb = new DatabaseHelper(this);

        showCalendarDialogAndSetDate();
        setActualDate();
        autoCompleteInputProduct();
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
        inputDateAddMeal.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(
                        ActivityAddMeal.this,
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
                inputDateAddMeal.setText( date );
            }
        };
    }

    private void setActualDate(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get( Calendar.YEAR );
        month = calendar.get( Calendar.MONTH );
        day = calendar.get( Calendar.DAY_OF_MONTH );

        String date = year + "-" + (month+1) + "-" + day;
        inputDateAddMeal.setText( date );
    }

    private void autoCompleteInputProduct(){
        Cursor cursor = myDb.getAllProducts();
        String[] products = new String[cursor.getCount()];

        int i = 0;
        while (cursor.moveToNext()) {
            products[i++] = cursor.getString( 0 );
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, products);
        autoCompleteSearchProduct.setAdapter( adapter );
    }
}
