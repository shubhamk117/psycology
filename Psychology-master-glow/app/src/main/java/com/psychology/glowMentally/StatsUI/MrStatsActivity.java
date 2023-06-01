package com.psychology.glowMentally.StatsUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.psychology.glowMentally.EntriesUI.MREntryActivity;
import com.psychology.glowMentally.R;
import com.psychology.glowMentally.StatsUI.MrStatsUI.MrStatsAdapter;
import com.psychology.glowMentally.StatsUI.MrStatsUI.MrStatsModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class MrStatsActivity extends AppCompatActivity {

    RecyclerView MrStatsRecycler;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<MrStatsModel> mrStatsModelArrayList = new ArrayList<>();

    EditText startDate, endDate;

    int selectedStartDay, selectedStartMonth, selectedStartYear;

    Date startDateD, endDateD, dateD;

    TextView statsNumber;
    LinearLayout btns;
    Button addMore, skip;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mr_stats);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.orangeMR));
//                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        startDate = findViewById(R.id.start_date_edit);
        endDate = findViewById(R.id.end_date_edit);
        btns=findViewById(R.id.btns);
        statsNumber = findViewById(R.id.mr_stats_number);

        MrStatsRecycler = findViewById(R.id.mr_stats_recycler);
        MrStatsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        MrStatsRecycler.setHasFixedSize(true);
        MrStatsRecycler.setNestedScrollingEnabled(false);

        addMore = findViewById(R.id.addMore);
        skip = findViewById(R.id.skip);

        backBtn=findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MREntryActivity.class);
                startActivity(intent);
                finish();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(startDate.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Please select a start date first", Toast.LENGTH_SHORT).show();
                }
                else{
                   handleDateButton1();
                }
            }
        });
    }

    private void getEntries(){

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            startDateD = format.parse(startDate.getText().toString().trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            endDateD = format.parse(endDate.getText().toString().trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("MR").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                mrStatsModelArrayList.clear();

                for(DocumentSnapshot documentSnapshot: task.getResult()){

                    MrStatsModel mrStatsModel = new MrStatsModel();
                    mrStatsModel.setDate(documentSnapshot.getString("eventDate"));
                    mrStatsModel.setTime(documentSnapshot.getString("eventTime"));
                    mrStatsModel.setEvent(documentSnapshot.getString("event"));

                    Log.i("date", documentSnapshot.getString("eventDate"));

                    try {
                        dateD = format.parse(documentSnapshot.getString("eventDate"));
                        mrStatsModel.setDateD(dateD);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    assert dateD != null;
                    if(dateD.compareTo(startDateD) >= 0 && dateD.compareTo(endDateD) <= 0){
                        mrStatsModelArrayList.add(mrStatsModel);
                    }

                }

                Collections.sort(mrStatsModelArrayList);
                Collections.reverse(mrStatsModelArrayList);

                int number = mrStatsModelArrayList.size();

                statsNumber.setText("You have " + String.valueOf(number) + " Mindfulness Reminders in the selected range of time period");
                MrStatsAdapter mrStatsAdapter = new MrStatsAdapter(getApplicationContext(), mrStatsModelArrayList);
                MrStatsRecycler.setAdapter(mrStatsAdapter);
                btns.setVisibility(View.VISIBLE);
            }
        });
    }

    private void handleDateButton(){

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

//        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.ERTTimePickerTheme, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//                selectedStartDay = dayOfMonth;
//                selectedStartMonth = month;
//                selectedStartYear = year;
//
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(year,month,dayOfMonth);
//                startDate.setText(DateFormat.format("dd/MM/yyyy", calendar));
//            }
//        },year,month,date);
//
////        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
////                "Cancel",
////                new DialogInterface.OnClickListener() {
////                    public void onClick(DialogInterface dialog,
////                                        int which) {
////                        if (which == DialogInterface.BUTTON_NEGATIVE) {
////                            dialog.cancel();
//////                            isOkayClicked = false;
////                        }
////                    }
////                });
//
//        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE,
//                "OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,
//                                        int which) {
//                        if (which == DialogInterface.BUTTON_POSITIVE) {
////                            isOkayClicked = true;
//                            DatePicker datePicker = datePickerDialog
//                                    .getDatePicker();
////                            datePickerListener.onDateSet(datePicker,
////                                    datePicker.getYear(),
////                                    datePicker.getMonth(),
////                                    datePicker.getDayOfMonth());
//                        }
//                    }
//                });
//        datePickerDialog.setCancelable(false);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.MRTimePickerTheme,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                selectedStartDay = dayOfMonth;
                selectedStartMonth = month;
                selectedStartYear = year;

                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,dayOfMonth);
                startDate.setText(DateFormat.format("dd/MM/yyyy", calendar));
            }
        }, year, month, date);
//        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.orangeMR));
//        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.orangeMR));

        datePickerDialog.show();
    }

    private void handleDateButton1(){

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.MRTimePickerTheme,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,dayOfMonth);
                endDate.setText(DateFormat.format("dd/MM/yyyy", calendar));
                getEntries();
            }
        }, year, month, date);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(selectedStartYear, selectedStartMonth, selectedStartDay  + 1,
                0, 0, 0);

        datePickerDialog.getDatePicker().setMinDate(calendar1.getTimeInMillis());
//        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        datePickerDialog.show();
    }
}