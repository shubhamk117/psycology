package com.psychology.glowMentally.Notification;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.psychology.glowMentally.EntriesUI.ERTEntryActivity;
import com.psychology.glowMentally.EntriesUI.MREntryActivity;
import com.psychology.glowMentally.EntriesUI.PAEntryActivity;
import com.psychology.glowMentally.R;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    String description = "Description";
    String title = "GlowMentally";
    int notificationId = 0;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Calendar calendar;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i;
        if (notificationId == 1) {
            i = new Intent(context, MREntryActivity.class);
        } else if (notificationId == 2) {
            i = new Intent(context, PAEntryActivity.class);
        } else {
            i = new Intent(context, ERTEntryActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        description = intent.getStringExtra("description");
        notificationId = intent.getIntExtra("notificationId", 0);

        if (notificationId == 0) {
            description = "Hi, How would you describe regulating your emotions today? "+System.currentTimeMillis();
        }

        System.out.println("INTENT DESC: " + description);
        int id = (int) System.currentTimeMillis(); //this is the fix
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "MyGlowApp")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(description)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(description))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
//                .setOngoing(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManagerCompat.notify(id, builder.build());
//        Toast.makeText(context,"WORKS!",Toast.LENGTH_SHORT).show();

        if(notificationId==0){
//            setAlarm(context,24*60*60*1000,0);
//            setAlarm(context,1*60*60*1000,0);
        }
    }

    private void setAlarm(Context context,int duration,int notificationId) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,AlarmReceiver.class);
        intent.putExtra("description","Hello There!");
        intent.putExtra("notificationId",notificationId);
        calendar=Calendar.getInstance();
        pendingIntent = PendingIntent.getBroadcast(context,(int) System.currentTimeMillis(),intent,0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis()+duration,
                    pendingIntent);
        }
//        Toast.makeText(context,"Alarm Success",Toast.LENGTH_SHORT).show();
    }
}
