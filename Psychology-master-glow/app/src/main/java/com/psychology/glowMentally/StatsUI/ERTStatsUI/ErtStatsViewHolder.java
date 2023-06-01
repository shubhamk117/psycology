package com.psychology.glowMentally.StatsUI.ERTStatsUI;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.psychology.glowMentally.R;

public class ErtStatsViewHolder extends RecyclerView.ViewHolder {

    PieChart pieChart;
    TextView date_val;
    LinearLayout nodata;
    CardView noDataCard;

    public ErtStatsViewHolder(@NonNull View itemView) {
        super(itemView);

        pieChart = itemView.findViewById(R.id.statsChart);
        date_val=itemView.findViewById(R.id.date_val);
        nodata=itemView.findViewById(R.id.nodata);
        noDataCard = itemView.findViewById(R.id.noDataCard);
    }
}
