package com.mscisz.damian.calculator;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("preferences",MODE_PRIVATE);
        boolean firstStart = preferences.getBoolean("firstStart", true);

        if(firstStart){
            Intent i=new Intent(getApplicationContext(),EnterBasicDataActivity.class);
            startActivity(i);
            enterBasicData();
        }
        else{
            setContentView(R.layout.activity_main);
            input = findViewById(R.id.input);

            SharedPreferences basicDataPref = getSharedPreferences("basicDataPref", Context.MODE_PRIVATE);
            String txt = basicDataPref.getString("inputDateBirth","unkown");
            input.setText(txt);
        }
    }

    private void enterBasicData() {
        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }
}
