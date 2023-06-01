package com.psychology.glowMentally.OnboardingUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.psychology.glowMentally.LoginUI.LoginDetailsCollectionActivity;
import com.psychology.glowMentally.LoginUI.LoginMainActivity;
import com.psychology.glowMentally.MainActivity;
import com.psychology.glowMentally.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorEg));
            window.setNavigationBarColor(this.getResources().getColor(R.color.colorEg));

//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        SharedPreferences sharedPreferences = getSharedPreferences("Psychology",MODE_PRIVATE);
        Boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);
//        System.out.println("CHECKNOWROBIN: "+sharedPreferences.getString("name","0"));
        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
//                Toast.makeText(getApplicationContext(), sharedPreferences.getBoolean("isFirstTime",false)+"a", Toast.LENGTH_SHORT).show();

                if(isFirstTime){
                    Intent intent = new Intent(getApplicationContext(), InitialMessageActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
//                    Intent intent = new Intent(getApplicationContext(), InitialMessageActivity.class);
//                    startActivity(intent);
//                    finish();
                    redirect();
                }
            }
        }, secondsDelayed * 2000);

    }

    private void redirect(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), LoginDetailsCollectionActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
        else {
            Intent intent = new Intent(getApplicationContext(), LoginMainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}