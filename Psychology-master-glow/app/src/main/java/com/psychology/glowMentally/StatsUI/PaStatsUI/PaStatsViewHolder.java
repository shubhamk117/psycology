package com.psychology.glowMentally.StatsUI.PaStatsUI;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psychology.glowMentally.R;

public class PaStatsViewHolder extends RecyclerView.ViewHolder {

    TextView pa, date;
    ImageButton edit;

    public PaStatsViewHolder(@NonNull View itemView) {
        super(itemView);

        pa = itemView.findViewById(R.id.pa);
        date = itemView.findViewById(R.id.pa_date);
        edit = itemView.findViewById(R.id.pa_edit);
    }
}
