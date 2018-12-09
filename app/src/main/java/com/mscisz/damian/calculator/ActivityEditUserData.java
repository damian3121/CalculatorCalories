package com.mscisz.damian.calculator;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.w3c.dom.Text;

public class ActivityEditUserData extends AppCompatActivity {

    private TextInputLayout inputEditDailyCalories;
    private FloatingActionButton fabAddEditingData;
    private TextInputEditText inputEditName;
    private  TextInputEditText inputEditTargetWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit_user_data );

        inputEditDailyCalories = (TextInputLayout) findViewById( R.id.inputEditDailyCalories );
        fabAddEditingData = (FloatingActionButton) findViewById( R.id.fabAddEditingData );
        inputEditName = (TextInputEditText) findViewById( R.id.inputEditName );
        inputEditTargetWeight = (TextInputEditText) findViewById( R.id.inputEditTargetWeight );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setSharedPrefEditingData();
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

    public void setSharedPrefEditingData(){

        fabAddEditingData.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityEditUserData.this);
                builder.setCancelable( true );

                if(validateInputEditDailyCalories() && validateInputEditName() &&
                        validateInputEditTargetWeight()){
                    SharedPreferences editDataPref = getSharedPreferences("editDataPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = editDataPref.edit();

                    SharedPreferences myPreferences = getSharedPreferences("basicDataPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor_ = myPreferences.edit();

                    editor.putInt("dailyCalories" , Integer.parseInt( inputEditDailyCalories.getEditText().getText().toString()));

                    editor.apply();

                    editor_.putString( "name",  inputEditName.getText().toString());
                    editor_.putFloat( "inputWeight", Float.valueOf( inputEditTargetWeight.getText().toString()) );
                    editor_.apply();

                    builder.setTitle( "Uwaga" );
                    builder.setMessage( "Dane zostały wprowadzone poprawnie" );
                    builder.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    } );
                    builder.show();
                }
                else{
                    builder.setTitle( "Uwaga" );
                    builder.setMessage( "Dane zostały błędnie wporawadzone" );
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

    private boolean validateInputEditDailyCalories(){
        String pInputEditDailyCalories = inputEditDailyCalories.getEditText().getText().toString().trim();

        if(!pInputEditDailyCalories.isEmpty()){
            return true;
        }else{
            inputEditDailyCalories.setError("Wpisz nową wartość");
            return false;
        }
    }

    private boolean validateInputEditName(){
        String pInputEditName = inputEditName.getText().toString().trim();

        if(!pInputEditName.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    private boolean validateInputEditTargetWeight(){
        String pInputEditTargetWeight = inputEditTargetWeight.getText().toString().trim();

        if(!pInputEditTargetWeight.isEmpty()){
            return true;
        }else{
            return false;
        }
    }
}
