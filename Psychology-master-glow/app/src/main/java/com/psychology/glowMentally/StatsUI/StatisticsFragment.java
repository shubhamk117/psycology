package com.psychology.glowMentally.StatsUI;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.psychology.glowMentally.R;


public class StatisticsFragment extends Fragment {

    CardView ErtStats, MrStats, PaStats;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        ErtStats = view.findViewById(R.id.ERTButton);
        MrStats = view.findViewById(R.id.MRButton);
        PaStats = view.findViewById(R.id.PAButton);

        ErtStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ErtStatsActivity.class);
                startActivity(intent);
            }
        });

        MrStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MrStatsActivity.class);
                startActivity(intent);
            }
        });

        PaStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PaStatsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}