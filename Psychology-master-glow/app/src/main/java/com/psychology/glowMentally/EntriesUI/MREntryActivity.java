package com.psychology.glowMentally.EntriesUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.psychology.glowMentally.MainActivity;
import com.psychology.glowMentally.Notification.AlarmReceiver;
import com.psychology.glowMentally.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class MREntryActivity extends AppCompatActivity {

    Button eventDate, eventTime;
    ImageView backBtn;
    int tHour, tMinute;
    ProgressBar progressBar;
    String selectedEventTime = "", selectedEventDate = "";
    CardView timeBtn, calendarBtn;
    EditText event;

    Button submit;
    TextView skip;
    SharedPreferences prefs;
    int selectedDay, selectedMonth, selectedYear, selectedHour, selectedMinute;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Calendar calendarNotification;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mrentry);
        progressBar=findViewById(R.id.progressBar);
        timeBtn = findViewById(R.id.timeBtn);
        calendarBtn=findViewById(R.id.calendarBtn);

        calendarNotification=Calendar.getInstance();
        createNotificationChannel();

        SharedPreferences sharedPreferences = getSharedPreferences("Psychology",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.orangeMR));
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        eventDate = findViewById(R.id.eventDate);
        eventTime = findViewById(R.id.eventTime);
        event = findViewById(R.id.event_edit);
        submit = findViewById(R.id.submit);
        skip = findViewById(R.id.skip);

        backBtn=findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDateButton();
            }
        });

        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTimeButton();
            }
        });

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });



        eventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimeButton();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredEvent = event.getText().toString().trim();

                if(TextUtils.isEmpty(enteredEvent)){
                    Toast.makeText(getApplicationContext(), "Please enter an event to continue", Toast.LENGTH_SHORT).show();
                }
                else if(selectedEventDate.equals("")){
                    Toast.makeText(getApplicationContext(), "Please select an event date to continue", Toast.LENGTH_SHORT).show();
                }
                else if(selectedEventTime.equals("")){
                    Toast.makeText(getApplicationContext(), "Please select an event time to continue", Toast.LENGTH_SHORT).show();
                }
                else{
                    skip.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.INVISIBLE);
                    myEdit.putString("mrevent",enteredEvent);
                    myEdit.putString("mreventTime",selectedEventTime);
                    myEdit.putString("mreventDate",selectedEventDate);
                    myEdit.commit();
                    Calendar calendar = Calendar.getInstance(Locale.getDefault());
//                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.YEAR,selectedYear);
                    calendar.set(Calendar.MONTH,selectedMonth);
                    calendar.set(Calendar.DAY_OF_MONTH,selectedDay);
                    calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                    calendar.set(Calendar.MINUTE, selectedMinute);
                    calendar.set(Calendar.SECOND,0);
                    calendar.set(Calendar.MILLISECOND,0);
                    Log.i("CALENDAR: ",selectedYear+" "+selectedMonth+" "+selectedDay+" "+selectedHour+" "+selectedMinute);
                    Log.i("CHECKTEST: ",calendar.getTimeInMillis()+"");
//                    Toast.makeText(getApplicationContext(),"Done!",Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(MREntryActivity.this, MRBroadcast.class);
//                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MREntryActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//                    alarmManager.setExact(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

                    HashMap<String, Object> data = new HashMap<>();
                    data.put("event", enteredEvent);
                    data.put("eventDate", selectedEventDate);
                    data.put("eventTime", selectedEventTime);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("MR").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            setAlarm(getApplicationContext(),1);
                            skip.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            submit.setVisibility(View.VISIBLE);
//                            Toast.makeText(getApplicationContext(), "MR Entry added", Toast.LENGTH_SHORT).show();
//                            Intent intent2 = new Intent(getApplicationContext(), PAEntryActivity.class);
//                            startActivity(intent2);
//                            finish();
                        }
                    });
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PAEntryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void handleTimeButton(){
        Calendar calendar = Calendar.getInstance();
        int Hour = calendar.get(Calendar.HOUR);
        int Minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.MRTimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                selectedHour = hourOfDay;
                selectedMinute = minute;
                tHour = hourOfDay;
                tMinute = minute;
                calendarNotification.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendarNotification.set(Calendar.MINUTE,minute);
//                calendarNotification.set(Calendar.HOUR_OF_DAY,hourOfDay);
                Calendar calendar = Calendar.getInstance();
                calendar.set(0,0,0,tHour,tMinute);
                String time = hourOfDay + ":" + minute;
                eventTime.setText("Event Time - " + DateFormat.format("hh:mm aa", calendar));
                selectedEventTime = String.valueOf(DateFormat.format("hh:mm aa", calendar));
            }
        },Hour,Minute,true);

//        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                selectedHour = hourOfDay;
//                selectedMinute = minute;
//                tHour = hourOfDay;
//                tMinute = minute;
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(0,0,0,tHour,tMinute);
//                String time = hourOfDay + ":" + minute;
//                eventTime.setText("Event Time - " + DateFormat.format("hh:mm aa", calendar));
//                selectedEventTime = String.valueOf(DateFormat.format("hh:mm aa", calendar));
//            }
//        },Hour,Minute,true);

        timePickerDialog.show();
    }

    private void handleDateButton(){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

//        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.MRTimePickerTheme, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                selectedYear = year;
//                selectedMonth = month;
//                selectedDay = dayOfMonth;
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(year,month,dayOfMonth);
//                eventDate.setText("Event Date - " + DateFormat.format("dd/MM/yyyy", calendar));
//                selectedEventDate = String.valueOf(DateFormat.format("dd/MM/yyyy", calendar));
//
//            }
//        },year,month,date);

//        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor("#000000"));
//        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.orangeMR));
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.MRTimePickerTheme,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectedYear = year;
                selectedMonth = month;
                selectedDay = dayOfMonth;
                calendarNotification.set(Calendar.YEAR,year);
                calendarNotification.set(Calendar.MONTH,month);
                calendarNotification.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,dayOfMonth);
                eventDate.setText("Event Date - " + DateFormat.format("dd/MM/yyyy", calendar));
                selectedEventDate = String.valueOf(DateFormat.format("dd/MM/yyyy", calendar));
            }
        }, year, month, date);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DATE, 0);
        datePickerDialog.getDatePicker().setMinDate(calendar1.getTimeInMillis());
        datePickerDialog.show();
    }

//    private void createNotificationChannel(){
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            CharSequence name = "PsychologyReminderChannel";
//            String description = "Channel for Psychology reminder";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("notifyMR", name, importance);
//            channel.setDescription(description);
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
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
        intent.putExtra("description",event.getText().toString().trim()+" today. Take a moment. Be mindful.");
        intent.putExtra("notificationId",notificationId);

        pendingIntent = PendingIntent.getBroadcast(context,(int) System.currentTimeMillis(),intent,0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendarNotification.getTimeInMillis(),
                    pendingIntent);
        }
//        Toast.makeText(context,"Alarm Success",Toast.LENGTH_SHORT).show();


        Toast.makeText(getApplicationContext(), "MR Entry added", Toast.LENGTH_SHORT).show();
        Intent intent2 = new Intent(getApplicationContext(), PAEntryActivity.class);
        startActivity(intent2);
        finish();
    }
}
