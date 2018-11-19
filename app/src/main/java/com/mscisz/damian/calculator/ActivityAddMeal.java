package com.mscisz.damian.calculator;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
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

import fr.ganfra.materialspinner.MaterialSpinner;

public class ActivityAddMeal extends AppCompatActivity {

    private MaterialSpinner spinnerTypeOfMeal;
    private TextView inputDateAddMeal;
    private TextView confirmMeal;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private AutoCompleteTextView autoCompleteSearchProduct;
    private EditText amountOfMeal;
    DatabaseHelper myDb;
    private List<String> products = new ArrayList<>();

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
        confirmMeal = (TextView) findViewById( R.id.confirmMeal );
        myDb = new DatabaseHelper(this);

        showCalendarDialogAndSetDate();
        setActualDate();
        autoCompleteInputProduct();
        addMeal();
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

        int i = 0;
        while (cursor.moveToNext()) {
            products.add( cursor.getString( 0 ) );
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, products);
        autoCompleteSearchProduct.setAdapter( adapter );
    }

    private void addMeal(){

        confirmMeal.setOnClickListener( new View.OnClickListener() {
            boolean result = false;
            @Override
            public void onClick(View v) {
                // alert
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAddMeal.this);
                builder.setCancelable( true );
                if (!products.contains(autoCompleteSearchProduct.getText().toString())) {
                    builder.setTitle( "Uwaga" );
                    builder.setMessage( "Najpierw wprowadź produkt do bazy" );
                    builder.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    } );
                    builder.show();
                } else {

                    if(amountOfMeal.getText().length() != 0 &&
                            autoCompleteSearchProduct.getTextSize() != 0){
                        result = myDb.insertDataToMealTable( inputDateAddMeal.getText().toString(),
                                spinnerTypeOfMeal.getSelectedItem().toString(),
                                autoCompleteSearchProduct.getText().toString(),
                                Integer.parseInt( amountOfMeal.getText().toString() ) );

                        autoCompleteSearchProduct.setText( "" );
                        amountOfMeal.setText( "" );
                    }

                    builder.setTitle( "Uwaga" );
                    builder.setMessage( "Posiłek został dodany" );
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
