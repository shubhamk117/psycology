package com.psychology.glowMentally.EntriesUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.psychology.glowMentally.MainActivity;
import com.psychology.glowMentally.Notification.AlarmReceiver;
import com.psychology.glowMentally.R;
import com.psychology.glowMentally.VideoPlayerActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class PAEntryActivity extends AppCompatActivity {

    AutoCompleteTextView affirmation;

    Button selectDate, submit;
    TextView skip;

    String selectedAffirmationDate = "";
    ProgressBar progressBar;

    ArrayList<String> Affirmations = new ArrayList<>();
    int selectedDay, selectedMonth, selectedYear;
    ImageView backBtn;
    CardView dateCard;

    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;

    Button learnMoreBtn;
    private Calendar calendarNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paentry);

        createNotificationChannel();

        SharedPreferences sharedPreferences = getSharedPreferences("Psychology",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        calendarNotification=Calendar.getInstance();
        learnMoreBtn = findViewById(R.id.learnMore);
        progressBar=findViewById(R.id.progressBar);
        dateCard=findViewById(R.id.dateCard);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.bluePA));
        }

        Affirmations.add("I have limitless potential");
        Affirmations.add("I’m exactly where I need to be. I see myself in my dream job");
        Affirmations.add("I have all the best skills and knowledge to deliver for this job");
        Affirmations.add("I have exceptional talents and capabilities and I will nail it");
        Affirmations.add("I am courageous enough to face and conquer my fears");
        Affirmations.add("I am confident in my self-worth");
        Affirmations.add("I am the best at what I d and I’m going to create exceptional results for my organization");
        Affirmations.add("This is the opportunity I need to show the world that I’m the best");
        Affirmations.add("I am living my life to the fullest");
        Affirmations.add("I am more effective when I focus and take care of myself");
        Affirmations.add("This is my game and I am here to show them how to win");
        Affirmations.add("I do my work for a purpose. I am going to shine at it");
        affirmation = findViewById(R.id.affirmation_edit);
        submit = findViewById(R.id.submit);
        skip = findViewById(R.id.skip);
        selectDate = findViewById(R.id.affirmationDate);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                this,
//                R.layout.dropdown_menu_popup_item,
//                Affirmations
//        );
        PAEntryAdapter adapter = new PAEntryAdapter(getApplicationContext(),R.layout.autocomplete_text,Affirmations);
        affirmation.setAdapter(adapter);

        backBtn=findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        learnMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
                intent.putExtra("headingText","Positive Affirmation");
                intent.putExtra("videoID","cMxKG_ZKUpY");
                startActivity(intent);
            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });

        dateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDateButton();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredAffirmation = affirmation.getText().toString().trim();
                if(TextUtils.isEmpty(enteredAffirmation)){
                    Toast.makeText(getApplicationContext(), "Enter an affirmation to continue", Toast.LENGTH_SHORT).show();
                }
                else if(selectedAffirmationDate.equals("")){
                    Toast.makeText(getApplicationContext(), "Select an affirmation date to continue", Toast.LENGTH_SHORT).show();
                }
                else{
                    skip.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.INVISIBLE);
//                    createNotificationChannel();
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("affirmation", enteredAffirmation);
                    data.put("affirmationDate", selectedAffirmationDate);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("PA").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
//                            scheduleNotification();
                            setAlarm(getApplicationContext(),2);
                            skip.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            submit.setVisibility(View.VISIBLE);

                            myEdit.putString("paevent",enteredAffirmation);
//                            myEdit.putString("paeventTime",selectedEventTime);
                            myEdit.putString("paeventDate",selectedAffirmationDate);
                            myEdit.commit();

                            Toast.makeText(getApplicationContext(), "PA Entry added successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void handleDateButton(){

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

//        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.PATimePickerTheme, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(year,month,dayOfMonth);
//                selectedYear=year;
//                selectedMonth=month;
//                selectedDay=dayOfMonth;
//                selectDate.setText("Affimation Date - " + DateFormat.format("dd/MM/yyyy", calendar));
//
//                selectedAffirmationDate = String.valueOf(DateFormat.format("dd/MM/yyyy", calendar));
//
//            }
//        },year,month,date);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.PATimePickerTheme,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,dayOfMonth);
                selectedYear=year;
                selectedMonth=month;
                selectedDay=dayOfMonth;
                calendarNotification.set(Calendar.YEAR,year);
                calendarNotification.set(Calendar.MONTH,month);
                calendarNotification.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                calendarNotification.set(Calendar.HOUR_OF_DAY,11);
                calendarNotification.set(Calendar.MINUTE,10);
                selectDate.setText("Affimation Date - " + DateFormat.format("dd/MM/yyyy", calendar));
                selectedAffirmationDate = String.valueOf(DateFormat.format("dd/MM/yyyy", calendar));
            }
        }, year, month, date);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DATE, 0);

        datePickerDialog.getDatePicker().setMinDate(calendar1.getTimeInMillis());

        datePickerDialog.show();
    }

//    private void createNotificationChannel(){
//        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
//            CharSequence name = "reminderChannel";
//            String desc = "Channel for reminder";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("notifyPA",name,importance);
//            channel.setDescription(desc);
//
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }

//    private void scheduleNotification(){
////        createNotificationChannel();
//        Calendar calendar = Calendar.getInstance(Locale.getDefault());
////                    calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.YEAR,selectedYear);
//        calendar.set(Calendar.MONTH,selectedMonth);
//        calendar.set(Calendar.DAY_OF_MONTH,selectedDay);
//        calendar.set(Calendar.HOUR_OF_DAY, 9);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND,0);
//        calendar.set(Calendar.MILLISECOND,0);
////                    Log.i("CALENDAR: ",selectedYear+" "+selectedMonth+" "+selectedDay+" "+selectedHour+" "+selectedMinute);
//        Log.i("CHECKTEST: ",calendar.getTimeInMillis()+"");
//        Toast.makeText(getApplicationContext(),"Done!",Toast.LENGTH_SHORT).show();
////
//        Intent intent = new Intent(PAEntryActivity.this, PABroadcast.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(PAEntryActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.setExact(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // your code
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MyAndroidChannelForGlow";
            String description = "Channel for Anroid Manager Glow";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel chanel = new NotificationChannel("MyGlowApp", name, importance);
            chanel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(chanel);
        }
    }
    private void setAlarm(Context context, int notificationId) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("description","Hurray!! You have a positive affirmation for today: \n"+affirmation.getText().toString().trim());
        intent.putExtra("notificationId",notificationId);
        pendingIntent = PendingIntent.getBroadcast(context,(int) System.currentTimeMillis(),intent,0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendarNotification.getTimeInMillis(),
                    pendingIntent);
        }
//        Toast.makeText(context,"Alarm Success",Toast.LENGTH_SHORT).show();
    }

}