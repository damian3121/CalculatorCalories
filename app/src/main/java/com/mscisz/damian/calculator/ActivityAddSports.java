package com.mscisz.damian.calculator;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class ActivityAddSports extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText inputNameActivity;
    EditText inputCaloriesValue;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_sports );

        inputNameActivity = (EditText) findViewById(R.id.inputNameActivity);
        inputCaloriesValue = (EditText) findViewById(R.id.inputCaloriesValue);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        myDb = new DatabaseHelper(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addSportActivity();
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

    public void addSportActivity(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputNameActivity.getText().length() != 0 && inputCaloriesValue.getText().length() != 0) {
                    boolean res = myDb.insertData( inputNameActivity.getText().toString(),
                            Integer.parseInt( inputCaloriesValue.getText().toString() ) );

                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAddSports.this);
                    builder.setCancelable( true );
                    if (!res) {
                        builder.setTitle( "Uwaga" );
                        builder.setMessage( "Aktywność sportowa jest już wprowadzona do bazy" );
                        builder.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        } );
                        builder.show();
                    } else {
                        builder.setTitle( "Uwaga" );
                        builder.setMessage( "Aktywność została dodany" );
                        builder.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        } );
                        builder.show();
                    }
                }
            }
        });
    }
}
