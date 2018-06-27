package com.abc.example.com.tab;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ABC on 29-09-2017.
 */

public class datalist extends ArrayAdapter<String> {

    private ArrayList<String> name;
    private ArrayList<String> notification;
    private Activity context;
    int b = 0 ;

    public datalist(Activity context , String[] name , String[] notification) {
        super(context, R.layout.activity_datalist, name );
        this.name=new ArrayList<String>();
        this.notification = new ArrayList<String>();
        this.context = context;
        this.name.addAll(Arrays.asList(name));
        this.notification.addAll(Arrays.asList(notification));
        Log.e("notification" , String.valueOf(this.name));
    }

    public void addItem(String name , String notification)
    {
        this.name.add(name);
        this.notification.add(notification);

    }

    @Override
    public int getCount() {return name.size();}

    @Override
    public View getView(int position , View convertView , ViewGroup parent)
    {

        LayoutInflater inflater=context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_datalist,null,true);
        TextView textView = (TextView)listViewItem.findViewById(R.id.t2);
        TextView textViewContact = (TextView)listViewItem.findViewById(R.id.t1);
        textView.setText(notification.get(position).substring(5 , 10));
        textViewContact.setText(name.get(position));
        notifyDataSetChanged();
        return listViewItem;

    }


}
