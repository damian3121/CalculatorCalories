package com.mscisz.damian.calculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddMealFragment extends Fragment {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listDataChild;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.fragment_add_meal, container, false );
        listView = (ExpandableListView) v.findViewById( R.id.lvExp);
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

        List <String> breakfast = new ArrayList<String>();
        breakfast.add( "sn 1.1" );
        breakfast.add( "sn 1.2" );

        List <String> elevenses = new ArrayList<String>();
        elevenses.add( "elevenses 2.1" );
        elevenses.add( "elevenses 2.2" );
        elevenses.add( "elevenses 2.2" );

        List <String> dinner = new ArrayList<String>();
        dinner.add( "dinner 3.1" );

        List <String> afternoonTea = new ArrayList<String>();
        afternoonTea.add( "podwie 4.1" );
        afternoonTea.add( "podwie 4.2" );

        List <String> supper = new ArrayList<String>();
        supper.add( "kolacja 5.1" );
        supper.add( "kolacja 5.2" );

        listDataChild.put(listDataHeader.get(0), breakfast);
        listDataChild.put(listDataHeader.get(1), elevenses);
        listDataChild.put(listDataHeader.get(2), dinner);
        listDataChild.put(listDataHeader.get(3), afternoonTea);
        listDataChild.put(listDataHeader.get(4), supper );
    }
}
