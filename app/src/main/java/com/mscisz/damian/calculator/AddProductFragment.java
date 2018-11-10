package com.mscisz.damian.calculator;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class AddProductFragment extends Fragment {
    DatabaseHelper myDb;
    EditText inputNameFood;
    EditText inputCaloriesValue;
    FloatingActionButton fab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = new DatabaseHelper(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_product, container, false);
        inputNameFood = (EditText) v.findViewById(R.id.inputNameFood);
        inputCaloriesValue = (EditText) v.findViewById(R.id.inputCaloriesValue);
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        addFood();

        return v;
    }

    public void addFood(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputNameFood.getText().length() != 0 && inputCaloriesValue.getText().length() != 0) {
                    boolean res = myDb.insertData( inputNameFood.getText().toString(),
                            Integer.parseInt( inputCaloriesValue.getText().toString() ) );

                    AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
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
