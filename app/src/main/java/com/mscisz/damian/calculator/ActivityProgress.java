package com.mscisz.damian.calculator;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;

public class ActivityProgress extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView headerProgressBar;
    private DatabaseHelper myDb;

    private int pProgressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_progress );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById( R.id.progressBar );
        headerProgressBar = (TextView) findViewById( R.id.headerProgressBar );
        myDb = new DatabaseHelper( this );

        headerProgressBar.setText( setProgressBarHeader() );
        setChartForGetCalories();
        setChartForCaloriesFromActivities();
        setChartForWeight();
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

    private String setProgressBarHeader(){
        SharedPreferences myPreferences = getSharedPreferences("basicDataPref", MODE_PRIVATE);

        final Float weight = myPreferences.getFloat("inputWeight",
                0);
        final Float weightTarget = myPreferences.getFloat("inputTargetWeight",
                0);

        new Thread( new Runnable() {
            @Override
            public void run() {
                while(pProgressStatus < (weight - myDb.getActualWeight( setActualDate() ))*100
                         /(weight - weightTarget)){
                    pProgressStatus++;
                    android.os.SystemClock.sleep( 50 );
                    handler.post( new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress( pProgressStatus );
                        }
                    } );
                }
            }
        } ).start();

        return "Jesteś coraz bliżej celu! Zrzuciłeś już " +
                String.valueOf( weight - myDb.getActualWeight( setActualDate() )) + "kg. " +
                "Do wymarzonej wagi pozostało " +
                String.valueOf((weight - weightTarget) - (weight - myDb.getActualWeight( setActualDate()) )) + " kg.";
    }

    public String setActualDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get( Calendar.YEAR );
        int month = calendar.get( Calendar.MONTH );
        int day = calendar.get( Calendar.DAY_OF_MONTH );

        return (year + "-" + (month+1) + "-" + day);
    }

    public String setDateBeforeFiveDays(int whichDayBefore){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get( Calendar.YEAR );
        int month = calendar.get( Calendar.MONTH );
        int day = calendar.get( Calendar.DAY_OF_MONTH ) - whichDayBefore;

        return (year + "-" + (month+1) + "-" + (day) );
    }

    public void setChartForGetCalories(){

        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, myDb.getCaloriesAmountPerDay( setDateBeforeFiveDays(4))),
                new DataPoint(1, myDb.getCaloriesAmountPerDay( setDateBeforeFiveDays(3))),
                new DataPoint(2, myDb.getCaloriesAmountPerDay( setDateBeforeFiveDays(2))),
                new DataPoint(3, myDb.getCaloriesAmountPerDay( setDateBeforeFiveDays(1))),
                new DataPoint(4, myDb.getCaloriesAmountPerDay( setDateBeforeFiveDays(0)))
        });
        graph.addSeries(series);

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(50);

        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
    }

    public void setChartForCaloriesFromActivities(){

        GraphView graph = (GraphView) findViewById(R.id.graph2);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, myDb.getCaloriesFromActivitiesPerDay( setDateBeforeFiveDays(4))),
                new DataPoint(1, myDb.getCaloriesFromActivitiesPerDay( setDateBeforeFiveDays(3))),
                new DataPoint(2, myDb.getCaloriesFromActivitiesPerDay( setDateBeforeFiveDays(2))),
                new DataPoint(3, myDb.getCaloriesFromActivitiesPerDay( setDateBeforeFiveDays(1))),
                new DataPoint(4, myDb.getCaloriesFromActivitiesPerDay( setDateBeforeFiveDays(0)))
        });
        graph.addSeries(series);

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(50);

        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
    }

    public void setChartForWeight(){
        GraphView graph = (GraphView) findViewById(R.id.graph3);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, myDb.getActualWeightPerDay( setDateBeforeFiveDays(4))),
                new DataPoint(1, myDb.getActualWeightPerDay( setDateBeforeFiveDays(3))),
                new DataPoint(2, myDb.getActualWeightPerDay( setDateBeforeFiveDays(2))),
                new DataPoint(3, myDb.getActualWeightPerDay( setDateBeforeFiveDays(1))),
                new DataPoint(4, myDb.getActualWeightPerDay( setDateBeforeFiveDays(0)))
        });
        graph.addSeries(series);
    }
}
