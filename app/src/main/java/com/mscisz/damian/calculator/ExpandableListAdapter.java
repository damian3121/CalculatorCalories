package com.mscisz.damian.calculator;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<String >> listDataChild;
    private DatabaseHelper myDb;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listHashMap) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listHashMap;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listDataChild.get( listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get( groupPosition );
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listDataChild.get( listDataHeader.get( groupPosition ) ).get( childPosition );
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup( groupPosition );

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            convertView = inflater.inflate( R.layout.list_group,null );
        }
        final TextView lblListHeader = (TextView)convertView.findViewById( R.id.lblListHeader );
        lblListHeader.setTypeface( null, Typeface.BOLD );
        lblListHeader.setText( headerTitle );

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild( groupPosition, childPosition );
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            convertView = inflater.inflate( R.layout.list_item,null );
        }
        final TextView lblListChild = (TextView) convertView.findViewById( R.id.lblListItem );
        lblListChild.setText( childText );

        lblListChild.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=  new  AlertDialog.Builder( context )
                        .setTitle("Czy na pewno usunąć posiłek?")
                        .setPositiveButton("TAK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        myDb = new DatabaseHelper( context );
                                        String[] arr = lblListChild.getText().toString().split(" ", 0);
                                        Log.d("jezdem", arr[1]);
                                        myDb.removeMeal( arr[0], arr[1] );
                                    }
                                }
                        )
                        .setNegativeButton("Anuluj",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                    }
                                }
                        );

                builder.show();
            }
        } );

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
