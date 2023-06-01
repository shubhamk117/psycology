package com.psychology.glowMentally.StatsUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
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
import com.psychology.glowMentally.EntriesUI.ERTEntryActivity;
import com.psychology.glowMentally.R;
import com.psychology.glowMentally.StatsUI.ERTStatsUI.ErtStatsAdapter;
import com.psychology.glowMentally.StatsUI.ERTStatsUI.ErtStatsModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ErtStatsActivity extends AppCompatActivity {

    RecyclerView ertStatsRecyclerView;
    ArrayList<ErtStatsModel> ertStatsModelArrayList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText startDate, endDate;
    int selectedStartDay, selectedStartMonth, selectedStartYear;
    Date startDateD, endDateD, dateD;

    TextView meanOfEntries,mrstatsno,meanData;
    ImageView backBtn;

    Button addMore, skip;
    int total=0;
    ErtStatsAdapter ertStatsAdapter;
LinearLayout btns;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ert_stats);

        ertStatsRecyclerView = findViewById(R.id.ert_stats_recycler);
        ertStatsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        ertStatsRecyclerView.setHasFixedSize(true);
        ertStatsRecyclerView.setNestedScrollingEnabled(false);

        startDate = findViewById(R.id.start_date_edit);
        mrstatsno=findViewById(R.id.mr_stats_number);
        endDate = findViewById(R.id.end_date_edit);

        meanData = findViewById(R.id.meanData);
        meanOfEntries = findViewById(R.id.meanEntries);

        addMore = findViewById(R.id.addMore);
        skip = findViewById(R.id.skip);
        btns=findViewById(R.id.btns);
        backBtn=findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.violetERT));
//                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ERTEntryActivity.class);
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

//    private void retrieveNum(){
//        total=0;
//        for(int i=0;i<ertStatsModelArrayList.size();i++){
//            String date = ertStatsModelArrayList.get(i).getDate();
//
//            db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("ERTbyDates").document(date)
//                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                            DocumentSnapshot documentSnapshot = task.getResult();
////                            Log.i("DATECHECK: ",task.getResult()+"");
//                            ArrayList<String> chartData = new ArrayList<>();
//
//                            if(documentSnapshot.exists()){
//                                Log.i("DATACHECK: ",task.getResult()+"");
//                                chartData.add((documentSnapshot.get("verySad"), "Very Sad"));
//                                chartData.add((documentSnapshot.get("sad"), "Sad"));
//                                chartData.add((documentSnapshot.get("normal"), "Normal"));
//                                chartData.add((documentSnapshot.get("happy"), "Happy"));
//                                chartData.add(new PieEntry(documentSnapshot.get("veryHappy"), "Very Happy"));
//                                total+=1;
//                            }
//                        }
//                    });
//
//        }

//        Log.i("VALUE OF I:",total+""+ertStatsModelArrayList.size());
//     }

    private void handleDateButton(){

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

//        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.ERTTimePickerTheme, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                selectedStartDay = dayOfMonth;
//                selectedStartMonth = month;
//                selectedStartYear = year;
//
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(year,month,dayOfMonth);
//                startDate.setText(DateFormat.format("dd-MM-yyyy", calendar));
//            }
//        },year,month,date);


//        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.violetERT));
//        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.violetERT));
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.ERTTimePickerTheme,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                selectedStartDay = dayOfMonth;
                selectedStartMonth = month;
                selectedStartYear = year;

                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,dayOfMonth);
                startDate.setText(DateFormat.format("dd-MM-yyyy", calendar));
            }
        }, year, month, date);

        datePickerDialog.show();
    }

    private void handleDateButton1(){

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.ERTTimePickerTheme,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,dayOfMonth);
                endDate.setText(DateFormat.format("dd-MM-yyyy", calendar));
//                getDates(startDate.getText().toString(), endDate.getText().toString());


                fetchValuesFromDb(getDates(startDate.getText().toString(), endDate.getText().toString()));
                meanOfEntries.setVisibility(View.VISIBLE);
                meanData.setVisibility(View.VISIBLE);
                mrstatsno.setVisibility(View.VISIBLE);
                btns.setVisibility(View.VISIBLE);

//                ErtStatsAdapter ertStatsAdapter = new ErtStatsAdapter(getApplicationContext(), ertStatsModelArrayList, meanOfEntries);
//                ertStatsRecyclerView.setAdapter(ertStatsAdapter);

                for(int i=0;i<ertStatsModelArrayList.size();i++){
                    System.out.println("ERTARRAY: "+ertStatsModelArrayList.get(i).getDate());
                }
            }
        }, year, month, date);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(selectedStartYear, selectedStartMonth, selectedStartDay  + 1,
                0, 0, 0);

        datePickerDialog.getDatePicker().setMinDate(calendar1.getTimeInMillis());

        datePickerDialog.show();
    }

    private ArrayList<String> getDates(String startingDate, String endingDate){

        List<Date> dates = new ArrayList<Date>();
        ArrayList<String> datesList = new ArrayList<>();

        String str_date = startingDate;
        String end_date = endingDate;

        SimpleDateFormat formatter ;

        formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate = null;
        try {
            startDate = (Date)formatter.parse(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date endDate = null;
        try {
            endDate = (Date)formatter.parse(end_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long interval = 24*1000 * 60 * 60; // 1 hour in millis
        long endTime =endDate.getTime() ; // create your endtime here, possibly using Calendar or Date
        long curTime = startDate.getTime();
        while (curTime <= endTime) {
            dates.add(new Date(curTime));
            curTime += interval;
        }

        ertStatsModelArrayList.clear();

        for(int i=0;i<dates.size();i++) {
            Date lDate = (Date) dates.get(i);
            String ds = formatter.format(lDate);
            System.out.println(" Date is ..." + ds);
            datesList.add(ds);

            ErtStatsModel ertStatsModel = new ErtStatsModel();


//            ertStatsModelArrayList.add(ertStatsModel);

//            String ds = "28-01-2023";
//            String ds1 = "29-01-2023";
//            String ds2 = "31-01-2023";
//            ErtStatsModel ertStatsModel = new ErtStatsModel();
//            ertStatsModel.setDate(ds);
//            ErtStatsModel ertStatsModel1 = new ErtStatsModel();
//            ertStatsModel1.setDate(ds1);
//            ErtStatsModel ertStatsModel2 = new ErtStatsModel();
//            ertStatsModel2.setDate(ds2);
//
//            ertStatsModelArrayList.add(ertStatsModel);
//            ertStatsModelArrayList.add(ertStatsModel1);
//            ertStatsModelArrayList.add(ertStatsModel2);

        }
//            db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("ERTbyDates").document(ds)
//                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            DocumentSnapshot documentSnapshot = task.getResult();
//                            if (documentSnapshot.exists()) {
//                                ertStatsModel.setDate(ds);
//                                ertStatsModelArrayList.add(ertStatsModel);
//                                System.out.println("FOUND: "+ds);
//                            }
//                        }
//                    });
//        }
//        System.out.println("ERTSIZE: "+ertStatsModelArrayList.size());
//        retrieveNum();

        return datesList;
    }

    private void fetchValuesFromDb(ArrayList<String> datesList){
            ertStatsModelArrayList.clear();
            for(int i=0;i<datesList.size();i++){
                db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("ERTbyDates").document(datesList.get(i))
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()) {
                                    ertStatsModelArrayList.add(new ErtStatsModel(documentSnapshot.getId(),
                                            Integer.parseInt(documentSnapshot.get("verySad").toString()),
                                            Integer.parseInt(documentSnapshot.get("sad").toString()),
                                            Integer.parseInt(documentSnapshot.get("normal").toString()),
                                            Integer.parseInt(documentSnapshot.get("happy").toString()),
                                            Integer.parseInt(documentSnapshot.get("veryHappy").toString())));
//                                    ertStatsModelArrayList.add(ertStatsModel);
                                    System.out.println("FOUND: "+documentSnapshot.getId());
                                }
                                ertStatsAdapter.notifyDataSetChanged();
                            }
                        });
            }

//        System.out.println("FOUNDNOW: "+ertStatsModelArrayList.size());
        ertStatsAdapter = new ErtStatsAdapter(getApplicationContext(), ertStatsModelArrayList, meanOfEntries,meanData);
        ertStatsRecyclerView.setAdapter(ertStatsAdapter);
    }


}