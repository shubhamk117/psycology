package com.psychology.glowMentally.EntriesUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.psychology.glowMentally.R;

import java.util.ArrayList;
import java.util.List;

public class PAEntryAdapter extends ArrayAdapter<String> {
    private ArrayList<String> fullList;
    public PAEntryAdapter(@NonNull Context context, int resource, List<String> objects) {
        super(context, resource);
        fullList = (ArrayList<String>) objects;
    }

    @Override
    public int getCount() {
        return fullList.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return fullList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.autocomplete_text, parent,
                false);
        TextView nameView = (TextView) rowView.findViewById(R.id.text);
//        Map<String, String> contactMap = fullList.get(position);
        nameView.setText(fullList.get(position));
//        phoneView.setText(contactMap.get("phone"));
//        typeView.setText(contactMap.get("type"));
        return rowView;
    }
}
