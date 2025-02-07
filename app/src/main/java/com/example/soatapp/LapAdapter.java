package com.example.soatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class LapAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> laps;

    public LapAdapter(Context context, List<String> laps) {
        super(context, 0, laps);
        this.context = context;
        this.laps = laps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView lapText = convertView.findViewById(android.R.id.text1);
        lapText.setText(laps.get(position));

        return convertView;
    }
}
