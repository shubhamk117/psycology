package com.psychology.glowMentally.StatsUI.MrStatsUI;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psychology.glowMentally.R;

public class MrStatsViewHolder extends RecyclerView.ViewHolder {

    TextView date, time, event;
    ImageButton edit;

    public MrStatsViewHolder(@NonNull View itemView) {
        super(itemView);

        date = itemView.findViewById(R.id.mr_date);
        time = itemView.findViewById(R.id.mr_time);
        event = itemView.findViewById(R.id.mr_event);
        edit = itemView.findViewById(R.id.mr_edit);

        edit.setImageResource(R.drawable.ic_baseline_edit_24);
    }
}
