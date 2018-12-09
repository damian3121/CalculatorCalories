package com.mscisz.damian.calculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    private TextView viewBilansCalories;
    private TextView textP;
    private DatabaseHelper myDb;
    private TextView substractCaloriesFromProduct;
    private TextView addCaloriesFromActivity;
    private TextView resultCaloriesPerDay;
    private RingProgressBar ringProgressBar;
    private RingProgressBar ringProgressBar2;
    private EditText inputActualWeight;
    private TextView confirmWeight;
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("preferences",MODE_PRIVATE);
        boolean firstStart = preferences.getBoolean("firstStart", true);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        viewBilansCalories = (TextView) findViewById( R.id.viewBilansCalories );
        textP = (TextView) findViewById( R.id.textP );
        substractCaloriesFromProduct = (TextView) findViewById( R.id.substractCaloriesFromProduct );
        addCaloriesFromActivity = (TextView) findViewById( R.id.addCaloriesFromActivity );
        resultCaloriesPerDay = (TextView) findViewById( R.id.resultCaloriesPerDay );
        ringProgressBar = (RingProgressBar) findViewById( R.id.progress_bar );
        ringProgressBar2 = (RingProgressBar) findViewById( R.id.progress_bar2 );
        inputActualWeight = (EditText) findViewById( R.id.inputActualWeight );
        confirmWeight = (TextView) findViewById( R.id.confirmWeight );
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        myDb = new DatabaseHelper( this );

        if(firstStart){
            Intent i=new Intent(getApplicationContext(),EnterBasicDataActivity.class);
            startActivity(i);
            enterBasicData();
        }

        setMainWelcomeText();
        setSubstractCaloriesFromProductByDate();
        setCaloriesFromSportActivityByDate();
        viewBilansCalories.setText( String.valueOf(  setBilansCalories() ));
        setActualResultCaloriesPerDay();
        addDailyStatistics();

        new Thread( new Runnable() {
            @Override
            public void run() {
                progress = 0;
                for(int i = 0; i < 100; i++){
                    try{
                        Thread.sleep( 200 );
                        handler.sendEmptyMessage( 0 );
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        } ).start();
    }

    private Handler handler = new Handler(  ){
        @Override
        public void handleMessage(Message message){
            if(Integer.parseInt( viewBilansCalories.getText().toString()) != 0) {
                if (message.what == 0) {
                    if (progress < 100 - (Integer.parseInt( resultCaloriesPerDay.getText().toString() ) * 100) /
                            Integer.parseInt( viewBilansCalories.getText().toString() )) {
                        progress += 1;
                        ringProgressBar.setProgress( progress );
                        ringProgressBar2.setProgress( progress );
                    }
                }
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
        setSubstractCaloriesFromProductByDate();
        setCaloriesFromSportActivityByDate();
        viewBilansCalories.setText( String.valueOf(  setBilansCalories() ));
        setActualResultCaloriesPerDay();
        addDailyStatistics();

        new Thread( new Runnable() {
            @Override
            public void run() {
                progress = 0;
                for(int i = 0; i < 100; i++){
                    try{
                        Thread.sleep( 130 );
                        handler.sendEmptyMessage( 0 );
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        } ).start();
    }

    private void enterBasicData() {
        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainPage:
                Intent intentMainPage = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentMainPage);
                break;
            case R.id.addProduct:
                Intent intentAddProduct = new Intent(getApplicationContext(), ActivityAddProduct.class);
                startActivity(intentAddProduct);
                break;
            case R.id.addMeals:
                Intent intentShowMeals = new Intent(getApplicationContext(), ActivityShowMealByDate.class);
                startActivity(intentShowMeals);
                break;
            case R.id.addActivity:
                Intent intentAddActivity = new Intent(getApplicationContext(), ActivityAddSports.class);
                startActivity(intentAddActivity);
                break;
            case R.id.progress:
                Intent intentAddStatisticsProgress = new Intent(getApplicationContext(), ActivityProgress.class);
                startActivity(intentAddStatisticsProgress);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public int setBilansCalories(){
        double ppm = 0;
        double cpm = 0;

        SharedPreferences myPreferences = getSharedPreferences("basicDataPref", MODE_PRIVATE);
        String sex = myPreferences.getString("sex", "unknown");

        Float weight = myPreferences.getFloat("inputWeight",
                0);
        Integer height = myPreferences.getInt("height",
                0);
        Integer age = myPreferences.getInt("age",
                0);
        String activityLevel = myPreferences.getString("activityLevel",
                "unknown");

        if( sex.equals( "men" )){
            ppm = 665.09 + (9.56*weight) + (1.85*height) - (4.68*age);
            cpm = ppm * setActivityLevel(activityLevel);
        }else{
            ppm = 66.47 + (13.75*weight) + (5*height) - (6.75*age);
            cpm = ppm * setActivityLevel(activityLevel);
        }

        return (int)cpm;
    }

    public double setActivityLevel(String level){
        switch (level){
            case "Siedzący tryb życia":
                return 1.4;
            case "Niska aktywność fizyczna":
                return 1.55;
            case "Umiarkowana aktywność fizyczna":
                return 1.8;
            case "Wysoka aktywność fizyczna":
                return 1.95;
            case "Ekstremalnie wysoka aktywność fizyczna":
                return 2.0;

                default:
                    return 0;
        }
    }

    public void setMainWelcomeText(){
        SharedPreferences xpreferences = getSharedPreferences("basicDataPref",MODE_PRIVATE);
        String date = setActualDate();

        String setText = "Witaj " + xpreferences.getString("name", "unknown")
                + "! " + "Dziś jest " + date + "\nDo zrzucenia pozostało Ci " + (xpreferences.getFloat("inputWeight", 0) -
                xpreferences.getFloat("inputTargetWeight", 0) + " kg");

        textP.setText(setText);
    }

    public void setSubstractCaloriesFromProductByDate(){
        myDb.getSubstractCaloriesFromProductByDate(setActualDate());
        substractCaloriesFromProduct.setText( String.valueOf( myDb.getSubstractCaloriesFromProductByDate(setActualDate())) );

    }

    public void setCaloriesFromSportActivityByDate(){
        myDb.getCaloriesFromSportActivityByDate(setActualDate());
        addCaloriesFromActivity.setText( String.valueOf( myDb.getCaloriesFromSportActivityByDate(setActualDate())) );

    }

    public String setActualDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get( Calendar.YEAR );
        int month = calendar.get( Calendar.MONTH );
        int day = calendar.get( Calendar.DAY_OF_MONTH );

        return (year + "-" + (month+1) + "-" + day);
    }

    public void setActualResultCaloriesPerDay(){
        int result = Integer.parseInt( viewBilansCalories.getText().toString() ) -
                Integer.parseInt( substractCaloriesFromProduct.getText().toString() ) +
                Integer.parseInt( addCaloriesFromActivity.getText().toString());

        resultCaloriesPerDay.setText( String.valueOf( result ) );
    }

    public void addDailyStatistics(){

        confirmWeight.setOnClickListener( new View.OnClickListener() {
            boolean result = false;
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable( true );
                if (inputActualWeight.getText().length() == 0) {
                    builder.setTitle( "Uwaga" );
                    builder.setMessage( "Najpierw wprowadź wagę" );
                    builder.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    } );
                    builder.show();
                } else {
                    result = myDb.insertDataToStatisticsTable ( setActualDate(),
                            Integer.parseInt( substractCaloriesFromProduct.getText().toString()),
                            Integer.parseInt( addCaloriesFromActivity.getText().toString()),
                            Float.parseFloat( inputActualWeight.getText().toString()) );

                    if(!result){
                        result = myDb.updateDataToStatisticsTable ( setActualDate(),
                                Integer.parseInt( substractCaloriesFromProduct.getText().toString()),
                                Integer.parseInt( addCaloriesFromActivity.getText().toString()),
                                Float.parseFloat( inputActualWeight.getText().toString()));
                    }
                    inputActualWeight.setText( "" );

                    builder.setTitle( "Uwaga" );
                    builder.setMessage( "Waga i statystyki zostały zaktualizowane" );
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
