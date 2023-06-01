package com.psychology.glowMentally.StatsUI.MrStatsUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psychology.glowMentally.R;

import java.util.ArrayList;

public class MrStatsAdapter extends RecyclerView.Adapter<MrStatsViewHolder> {

    Context context;
    ArrayList<MrStatsModel> mrStatsModelArrayList;

    public MrStatsAdapter(Context context, ArrayList<MrStatsModel> mrStatsModelArrayList) {
        this.context = context;
        this.mrStatsModelArrayList = mrStatsModelArrayList;
    }

    @NonNull
    @Override
    public MrStatsViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_item_mr_statistics, parent, false);
        return new MrStatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MrStatsViewHolder holder, int position) {
        holder.date.setText(mrStatsModelArrayList.get(position).getDate());
        holder.time.setText(mrStatsModelArrayList.get(position).getTime());
        holder.event.setText(mrStatsModelArrayList.get(position).getEvent());

        holder.edit.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_edit_24));

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mrStatsModelArrayList.size();
    }
}
