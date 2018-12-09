package com.mscisz.damian.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class Repeating_activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate( savedInstanceState );
        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }
}
