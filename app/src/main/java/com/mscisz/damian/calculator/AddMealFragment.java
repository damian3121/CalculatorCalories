package com.mscisz.damian.calculator;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.jar.Attributes;

import static android.content.ContentValues.TAG;

public class AddMealFragment extends Fragment {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listDataChild;
    private DatabaseHelper myDb = new DatabaseHelper( getActivity() );

    private List <String> breakfast = new ArrayList<String>();
    private List <String> elevenses = new ArrayList<String>();
    private List <String> dinner = new ArrayList<String>();
    private List <String> afternoonTea = new ArrayList<String>();
    private List <String> supper = new ArrayList<String>();

    private TextView inputDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        myDb = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.fragment_add_meal, container, false );
        listView = (ExpandableListView) v.findViewById( R.id.lvExp);
        inputDate = (TextView) v.findViewById(R.id.inputDate);

        showDialogOnInputClick();
        initData();

        listAdapter = new ExpandableListAdapter( getActivity(),listDataHeader, listDataChild );
        listView.setAdapter( listAdapter );

        return v;
    }

    private void initData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add( "Śniadanie" );
        listDataHeader.add( "Drugie śniadanie" );
        listDataHeader.add( "Obiad" );
        listDataHeader.add( "Podwieczorek" );
        listDataHeader.add( "Kolacja" );

        listDataChild.put(listDataHeader.get(0), breakfast);
        listDataChild.put(listDataHeader.get(1), elevenses);
        listDataChild.put(listDataHeader.get(2), dinner);
        listDataChild.put(listDataHeader.get(3), afternoonTea);
        listDataChild.put(listDataHeader.get(4), supper );
    }

    public void viewAllMealByDate(String typeMeal, String date){
        Cursor cursor = myDb.getMealByDate(typeMeal, date);
        String result_1 = "";

        while (cursor.moveToNext()) {
            result_1 = "";

            result_1 = cursor.getString( 0 );

            switch(typeMeal){
                case "sniadanie":
                    breakfast.add( result_1 );
                    break;
                case "sniadanie_2":
                    elevenses.add( result_1 );
                    break;
                case "obiad":
                    dinner.add( result_1 );
                    break;
                case "podwieczorek":
                    afternoonTea.add( result_1 );
                    break;
                case "kolacja":
                    supper.add( result_1 );
                    break;
            }
        }
    }

    private void showDialogOnInputClick(){
        inputDate.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get( Calendar.YEAR );
                int month = calendar.get( Calendar.MONTH );
                int day = calendar.get( Calendar.DAY_OF_MONTH );

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
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
                inputDate.setText( date );

                breakfast.clear();
                elevenses.clear();
                dinner.clear();
                afternoonTea.clear();
                supper.clear();

                viewAllMealByDate("sniadanie", inputDate.getText().toString());
                viewAllMealByDate("sniadanie_2", inputDate.getText().toString());
                viewAllMealByDate("obiad", inputDate.getText().toString());
                viewAllMealByDate("podwieczorek", inputDate.getText().toString());
                viewAllMealByDate("kolacja", inputDate.getText().toString());
            }
        };
    }
}
