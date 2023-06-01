package com.psychology.glowMentally.OnboardingUI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.psychology.glowMentally.LoginUI.LoginMainActivity;
import com.psychology.glowMentally.R;

import java.util.Calendar;

public class InitialMessageActivity extends AppCompatActivity {

    TextView message;

    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_message);
//        createNotificationChannel();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorEg));
            window.setNavigationBarColor(this.getResources().getColor(R.color.colorEg));

//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        message = findViewById(R.id.message);
        message.setText(getResources().getString(R.string.app_name) + " is your own self tracking app for superior mental health at workplace.");
        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginMainActivity.class);
                startActivity(intent);
                finish();
            }
        }, secondsDelayed * 4000);
//        setAlarm(getApplicationContext(),1,0);
    }

//    private void setAlarm(Context context, int duration, int notificationId) {
//        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, AlarmReceiver.class);
//        intent.putExtra("description","Hello There!");
//        intent.putExtra("notificationId",notificationId);
//        calendar= Calendar.getInstance();
//        if(notificationId==0){
//            calendar.set(Calendar.HOUR_OF_DAY, 21);
//        }
//        calendar.set(Calendar.MINUTE, 52);
//        pendingIntent = PendingIntent.getBroadcast(context,(int) System.currentTimeMillis(),intent,0);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
//                pendingIntent);
//        Toast.makeText(context,"Alarm Success "+notificationId,Toast.LENGTH_SHORT).show();
//        int secondsDelayed = 1;
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                Intent intent = new Intent(getApplicationContext(), LoginMainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }, secondsDelayed * 4000);
//    }
//
//    private void createNotificationChannel() {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            CharSequence name = "MyAndroidChannelForGlow";
//            String description = "Channel for Anroid Manager Glow";
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel chanel = new NotificationChannel("MyGlowApp",name,importance);
//            chanel.setDescription(description);
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(chanel);
//        }
//    }

}