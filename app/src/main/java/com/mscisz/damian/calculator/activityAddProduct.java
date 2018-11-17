package com.mscisz.damian.calculator;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class activityAddProduct extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText inputNameFood;
    EditText inputCaloriesValue;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_product );

        inputNameFood = (EditText) findViewById(R.id.inputNameFood);
        inputCaloriesValue = (EditText) findViewById(R.id.inputCaloriesValue);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        myDb = new DatabaseHelper(this);

        addFood();
    }

    public void addFood(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputNameFood.getText().length() != 0 && inputCaloriesValue.getText().length() != 0) {
                    boolean res = myDb.insertData( inputNameFood.getText().toString(),
                            Integer.parseInt( inputCaloriesValue.getText().toString() ) );

                    AlertDialog.Builder builder = new AlertDialog.Builder(activityAddProduct.this);
                    builder.setCancelable( true );
                    if (!res) {
                        builder.setTitle( "Uwaga" );
                        builder.setMessage( "Produkt jest już wprowadzony do bazy" );
                        builder.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        } );
                        builder.show();
                    } else {
                        builder.setTitle( "Uwaga" );
                        builder.setMessage( "Produkt został dodany" );
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
