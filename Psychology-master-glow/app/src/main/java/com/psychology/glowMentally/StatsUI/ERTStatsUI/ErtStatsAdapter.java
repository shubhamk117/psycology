package com.psychology.glowMentally.StatsUI.ERTStatsUI;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.FirebaseFirestore;
import com.psychology.glowMentally.R;

import java.util.ArrayList;

public class ErtStatsAdapter extends RecyclerView.Adapter<ErtStatsViewHolder> {

    Context context;
    ArrayList<ErtStatsModel> ertStatsModelArrayList;
    TextView entriesMean,meanData;

    public ErtStatsAdapter(Context context, ArrayList<ErtStatsModel> ertStatsModelArrayList, TextView entriesMean,TextView meanData) {
        this.context = context;
        this.ertStatsModelArrayList = ertStatsModelArrayList;
        this.entriesMean = entriesMean;
        this.meanData=meanData;
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    int verySadTotal = 0, sadTotal = 0, normalTotal = 0, happyTotal = 0, veryHappyTotal = 0, total = 0;

    @NonNull
    @Override
    public ErtStatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_item_ert_stats, parent, false);
        return new ErtStatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ErtStatsViewHolder holder, int position) {
        String date = ertStatsModelArrayList.get(position).getDate();
        System.out.println("FOUND:  "+date);
//        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("ERTbyDates").document(date)
//                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                        DocumentSnapshot documentSnapshot = task.getResult();
//
                        ArrayList<PieEntry> chartData = new ArrayList<>();
//                        if(documentSnapshot.exists()){

//                            System.out.println("FATES: "+documentSnapshot);
                            chartData.add(new PieEntry((int)(long)ertStatsModelArrayList.get(position).getVerySad(), "Very Sad"));
                            chartData.add(new PieEntry((int)(long)ertStatsModelArrayList.get(position).getSad(), "Sad"));
                            chartData.add(new PieEntry((int)(long)ertStatsModelArrayList.get(position).getNormal(), "Normal"));
                            chartData.add(new PieEntry((int)(long)ertStatsModelArrayList.get(position).getHappy(), "Happy"));
                            chartData.add(new PieEntry((int)(long)ertStatsModelArrayList.get(position).getVeryHappy(), "Very Happy"));

                            verySadTotal += (int)(long)ertStatsModelArrayList.get(position).getVerySad();
                            sadTotal = (int)(long)ertStatsModelArrayList.get(position).getSad();
                            normalTotal = (int)(long)ertStatsModelArrayList.get(position).getNormal();
                            happyTotal = (int)(long)ertStatsModelArrayList.get(position).getHappy();
                            veryHappyTotal = (int)(long)ertStatsModelArrayList.get(position).getVeryHappy();

                            Log.i("VeryHappyTotal: ", String.valueOf(veryHappyTotal));
                            Log.i("HappyTotal: ", String.valueOf(happyTotal));

                            PieDataSet pieDataSet = new PieDataSet(chartData,"" );
                            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                            PieData pieData = new PieData(pieDataSet);

                            holder.pieChart.setData(pieData);
                            holder.pieChart.getDescription().setEnabled(false);
                            holder.pieChart.setCenterText(ertStatsModelArrayList.get(position).getDate());
                            holder.pieChart.setCenterTextSize(10f);
                            holder.pieChart.setDrawEntryLabels(false);
                            holder.pieChart.getData().setDrawValues(false);
                            holder.pieChart.getLegend().setEnabled(false);
                            holder.pieChart.animate();
                            holder.pieChart.invalidate();
                            holder.pieChart.setVisibility(View.VISIBLE);
                            int total = verySadTotal + 2*sadTotal + 3*normalTotal + 4*happyTotal + 5*veryHappyTotal;
                            int totalNumber = veryHappyTotal + sadTotal + normalTotal + happyTotal + veryHappyTotal;
                            float average = (float)total/(float)totalNumber;

                            entriesMean.setText("Mean of all your entries - " + average);
                            String meanDataText="";
                            if(average>=1 && average<2){
                                meanDataText="Your hustle is most valued. It will be better, keep striving!";
                            }else if(average>=2 && average<3){
                                meanDataText="You are doing good. Keep up the spirit. Let’s do better!";
                            }else if(average>=3 && average<4){
                                meanDataText="You are doing very good, keep improving!";
                            }else if(average>=4 && average<5){
                                meanDataText="Very nice! Keep it up!! Cheering you!";
                            }
                            meanData.setText(meanDataText);

                            holder.pieChart.setVisibility(View.VISIBLE);
                            holder.nodata.setVisibility(View.INVISIBLE);
                            holder.noDataCard.setVisibility(View.INVISIBLE);
//                            holder.date_val.setText(ertStatsModelArrayList.get(position).getDate());
//                        }
//                        else {
//                            holder.pieChart.setVisibility(View.INVISIBLE);
//                            holder.nodata.setVisibility(View.VISIBLE);
//                            holder.noDataCard.setVisibility(View.VISIBLE);
//                            holder.date_val.setText(ertStatsModelArrayList.get(position).getDate());
//                    chartData.add(new PieEntry(0, "Very Sad"));
//                    chartData.add(new PieEntry(0, "Sad"));
//                    chartData.add(new PieEntry(0, "Normal"));
//                    chartData.add(new PieEntry(0, "Happy"));
//                    chartData.add(new PieEntry(0, "Very Happy"));
//
//                    PieDataSet pieDataSet = new PieDataSet(chartData,"" );
//                    pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//
//                    PieData pieData = new PieData(pieDataSet);
//
//                    holder.pieChart.setData(pieData);
//                    holder.pieChart.getDescription().setEnabled(false);
//                    holder.pieChart.setCenterText(ertStatsModelArrayList.get(position).getDate());
//                    holder.pieChart.setCenterTextSize(10f);
//                    holder.pieChart.setDrawEntryLabels(false);
//                    holder.pieChart.getData().setDrawValues(false);
//                    holder.pieChart.getLegend().setEnabled(false);
//                    holder.pieChart.animate();
//                    holder.pieChart.invalidate();
//                        }
//
//                    }
//                });

    }

    @Override
    public int getItemCount() {
        return ertStatsModelArrayList.size();
    }
}
