package com.mscisz.damian.calculator;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.Attributes;

public class AddMealFragment extends Fragment {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listDataChild;
    private DatabaseHelper myDb = new DatabaseHelper( getActivity() );

    private List <String> breakfast = new ArrayList<String>();
    List <String> elevenses = new ArrayList<String>();
    List <String> dinner = new ArrayList<String>();
    List <String> afternoonTea = new ArrayList<String>();
    List <String> supper = new ArrayList<String>();

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
        initData();

        viewAllMealByDate("sniadanie", "2018-11-11");
        viewAllMealByDate("sniadanie_2", "2018-11-10");
        viewAllMealByDate("obiad", "2018-11-13");
        viewAllMealByDate("podwieczorek", "2018-11-10");
        viewAllMealByDate("kolacja", "2018-11-10");

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
}
