package com.example.imatch.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.imatch.R;

public class MyAdapter extends ArrayAdapter<String>
{
    private final Activity context;
    private final String[] interestlist;

    @SuppressLint("ResourceType")
    public MyAdapter(@NonNull Context context, String[] interestlist) {
        super(context, R.layout.listdetails_layout, interestlist);
        this.context = (Activity) context;
        this.interestlist = interestlist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"InflateParams", "ViewHolder"}) View rowview = inflater.inflate(R.layout.listdetails_layout,null,true);
        TextView itemname = rowview.findViewById(R.id.listitem);

        itemname.setText(interestlist[position]);


        return rowview;
    }



}
