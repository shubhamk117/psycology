package com.psychology.glowMentally;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.psychology.glowMentally.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.psychology.glowMentally.EntriesUI.ERTEntryActivity;
import com.psychology.glowMentally.MainUI.AccountFragment;
import com.psychology.glowMentally.MainUI.EntriesFragment;
import com.psychology.glowMentally.MainUI.HomeFragment;
import com.psychology.glowMentally.Notification.AlarmReceiver;
import com.psychology.glowMentally.StatsUI.StatisticsFragment;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    EntriesFragment entriesFragment = new EntriesFragment();
    StatisticsFragment statisticsFragment = new StatisticsFragment();
    AccountFragment accountFragment = new AccountFragment();
    SharedPreferences prefs;
    int reminder = 0;


    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;

    private Calendar calendar;
    //Set Alarm Notification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("Psychology", Context.MODE_PRIVATE);


        Toast.makeText(getApplicationContext(), "isFirstTime"+prefs.getBoolean("isFirstTime",false), Toast.LENGTH_SHORT).show();
        if(prefs.getBoolean("isFirstTime",false)){
//            setAlarm(getApplicationContext(),0);
        }

        reminder=prefs.getInt("reminder",0);
        SharedPreferences.Editor myEdit = prefs.edit();
        myEdit.putInt("reminder",1);
        myEdit.putBoolean("isFirstTime",false);
        myEdit.commit();
//        checkForNotification();
        createNotificationChannel();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.mainColor));

//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        bottomNavigationView = findViewById(R.id.home_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.home:
                if (Build.VERSION.SDK_INT >= 21) {
                    Window window = this.getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.setStatusBarColor(this.getResources().getColor(R.color.mainColor));
//                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,homeFragment).commit();
                return true;
            case R.id.entries:
                if (Build.VERSION.SDK_INT >= 21) {
                    Window window = this.getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.setStatusBarColor(this.getResources().getColor(R.color.white));

//                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                Intent intent = new Intent(getApplicationContext(), ERTEntryActivity.class);
                startActivity(intent);
                return true;
            case R.id.stats:
                if (Build.VERSION.SDK_INT >= 21) {
                    Window window = this.getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.setStatusBarColor(this.getResources().getColor(R.color.mainColor));
//                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,statisticsFragment).commit();
                return true;
            case R.id.account:
                if (Build.VERSION.SDK_INT >= 21) {
                    Window window = this.getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.setStatusBarColor(this.getResources().getColor(R.color.mainColor));


//                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,accountFragment).commit();
                return true;
        }

        return false;
    }

//    void checkForNotification(){
//
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            System.out.println("Fetching FCM registration token failed");
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        String token = task.getResult();
//
//                        // Log and toast
////                        String msg = getString(R.string.msg_token_fmt, token);
//                        System.out.println("TOEKEN: "+token);
////                        Toast.makeText(MainActivity.this, "Your device registration token is: "+token, Toast.LENGTH_SHORT).show();
//                    }
//                });
////        if(value==0){
////            createNotificationChannel();
////            Toast.makeText(getApplicationContext(),"Reminder Set!",Toast.LENGTH_SHORT).show();
////            SharedPreferences sharedPreferences = getSharedPreferences("Psychology",MODE_PRIVATE);
////            SharedPreferences.Editor myEdit = sharedPreferences.edit();
////            myEdit.putInt("reminder",1);
////            myEdit.putBoolean("isFirstTime",false);
////            myEdit.commit();
////
////            Calendar calendar = Calendar.getInstance();
////            calendar.setTimeInMillis(System.currentTimeMillis());
////            calendar.set(Calendar.HOUR_OF_DAY, 13);
////            calendar.set(Calendar.MINUTE, 25);
////
////            Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
////            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,0);
////            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
////            alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis()/1000,
////                    AlarmManager.INTERVAL_DAY, pendingIntent);
////        }else{
////            Toast.makeText(getApplicationContext(),"Reminder Already Set!",Toast.LENGTH_SHORT).show();
////        }
//
//    }
//
    private void setAlarm(Context context, int notificationId) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("description","Hello There!");
        intent.putExtra("notificationId",notificationId);
        calendar= Calendar.getInstance();
//        if(notificationId==0){
            calendar.set(Calendar.HOUR_OF_DAY, 20);
            calendar.set(Calendar.MINUTE, 10);
            calendar.set(Calendar.SECOND,00);
//        }
//        calendar.set(Calendar.MINUTE,10);
        pendingIntent = PendingIntent.getBroadcast(context,(int) System.currentTimeMillis(),intent,0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                    pendingIntent);
        }
//        Toast.makeText(context,"Alarm Success "+notificationId,Toast.LENGTH_SHORT).show();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "MyAndroidChannelForGlow";
            String description = "Channel for Anroid Manager Glow";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel chanel = new NotificationChannel("MyGlowApp",name,importance);
            chanel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(chanel);
        }
    }
}