package com.psychology.glowMentally.MainUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.psychology.glowMentally.EntriesUI.ERTEntryActivity;
import com.psychology.glowMentally.EntriesUI.MREntryActivity;
import com.psychology.glowMentally.EntriesUI.PAEntryActivity;
import com.psychology.glowMentally.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HomeFragment extends Fragment {

    CardView ERTEntry, MREntry, PAEntry;

    TextView ERTEntries, MREntries, PAEntries;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    SharedPreferences prefs;

    List<String> daysOfWeek;
    List<String> daysOfWeek2;
    int ertEntryCount=0;
    int mrEntryCount=0;
    int paEntryCount=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        daysOfWeek = new ArrayList<>();
        daysOfWeek2 = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal =Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR,-7);

        for(int i=0;i<7;i++){
            cal.add(Calendar.DAY_OF_YEAR,1);
            System.out.println("DATE: "+sdf.format(cal.getTime()));
            daysOfWeek.add(sdf.format(cal.getTime()).toString());
            daysOfWeek2.add(sdf2.format(cal.getTime()).toString());
        }


        prefs = this.getActivity().getSharedPreferences("Psychology", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = prefs.edit();

        ERTEntry = view.findViewById(R.id.ERTButton);
        MREntry = view.findViewById(R.id.MRButton);
        PAEntry = view.findViewById(R.id.PAButton);

        ERTEntries = view.findViewById(R.id.ERTEntriesNumber);
        MREntries = view.findViewById(R.id.MREntriesNumber);
        PAEntries = view.findViewById(R.id.PAEntriesNumber);



        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                myEdit.putString("name",value.getString("name"));
                myEdit.commit();
             }
        });

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("ERT").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                if(value.getDocuments().isEmpty()){
                    ERTEntries.setText("0");
                }
                else{
                    for(int i=0;i<value.getDocuments().size();i++){
                        for(int j=0;j<daysOfWeek.size();j++){
                            if(value.getDocuments().get(i).get("date").toString().equals(daysOfWeek.get(j))){
                                ertEntryCount+=1;
                            }
                        }
                    }
                    ERTEntries.setText(String.valueOf(ertEntryCount));
                }
            }
        });

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("MR").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                assert value != null;
                if(value.getDocuments().isEmpty()){
                    MREntries.setText("0");
                }
                else{
                    for(int i=0;i<value.getDocuments().size();i++){
                        for(int j=0;j<daysOfWeek2.size();j++){
                            if(value.getDocuments().get(i).get("eventDate").toString().equals(daysOfWeek2.get(j))){
                                mrEntryCount+=1;
                            }
                        }
                    }

                    MREntries.setText(String.valueOf(mrEntryCount));
                }
            }
        });

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("PA").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                assert value != null;
                if(value.getDocuments().isEmpty()){
                    PAEntries.setText("0");
                }
                else{
                    for(int i=0;i<value.getDocuments().size();i++){
                        for(int j=0;j<daysOfWeek2.size();j++){
                            if(value.getDocuments().get(i).get("affirmationDate").toString().equals(daysOfWeek2.get(j))){
                                paEntryCount+=1;
                            }
                        }
                    }
                    PAEntries.setText(String.valueOf(paEntryCount));
                }
            }
        });

        ERTEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ERTEntryActivity.class);
                startActivity(intent);
            }
        });

        MREntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MREntryActivity.class);
                startActivity(intent);
            }
        });

        PAEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PAEntryActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}