package com.psychology.glowMentally.StatsUI.PaStatsUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psychology.glowMentally.R;

import java.util.ArrayList;

public class PaStatsAdapter extends RecyclerView.Adapter<PaStatsViewHolder> {

    Context context;
    ArrayList<PaStatsModel> paStatsModelArrayList;

    public PaStatsAdapter(Context context, ArrayList<PaStatsModel> paStatsModelArrayList) {
        this.context = context;
        this.paStatsModelArrayList = paStatsModelArrayList;
    }

    @NonNull
    @Override
    public PaStatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_item_pa_stats, parent, false);
        return new PaStatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaStatsViewHolder holder, int position) {
        holder.pa.setText(paStatsModelArrayList.get(position).getAffirmation());
        holder.date.setText(paStatsModelArrayList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return paStatsModelArrayList.size();
    }
}
