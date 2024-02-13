package com.psychology.glowMentally.EntriesUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.psychology.glowMentally.MainActivity;
import com.psychology.glowMentally.R;
import com.psychology.glowMentally.VideoPlayerActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ERTEntryActivity extends AppCompatActivity {
    ImageButton verySad,sad, normal, happy, veryHappy;
    ImageView backBtn;
    String emotionSelected = "";
    ProgressBar progressBar;

    Button learnMoreBtn;
    Button submit;
    TextView skip;

    int existingVerySad = 0;
    int existingSad = 0;
    int existingNormal = 0;
    int existingHappy = 0;
    int existingVeryHappy = 0;

    Date c = Calendar.getInstance().getTime();
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    String formattedDate = df.format(c);

    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ertentry);
        progressBar=findViewById(R.id.progressBar);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.violetERT));

//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        verySad = findViewById(R.id.verySad);
        sad = findViewById(R.id.sad);
        normal = findViewById(R.id.normal);
        happy = findViewById(R.id.happy);
        veryHappy = findViewById(R.id.veryHappy);
        submit = findViewById(R.id.submit);
        skip = findViewById(R.id.skip);
        learnMoreBtn = findViewById(R.id.learnMore);

        final int sdk = android.os.Build.VERSION.SDK_INT;

        backBtn=findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        learnMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
                intent.putExtra("headingText","Emotion Regulation Tracker");
                intent.putExtra("videoID","58qylelmzoQ");
                startActivity(intent);
            }
        });


        verySad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emotionSelected = "Very Sad";

//                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                    verySad.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.color_roundedcorners) );
//                } else {
                    verySad.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.color_roundedcorners));

                    sad.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedcorner));
                    normal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedcorner));
                    happy.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedcorner));
                    veryHappy.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedcorner));
//                }

            }
        });

        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emotionSelected = "Sad";

//                verySad.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_verysad, 0, 0, 0);
                verySad.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedcorner));

                sad.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.color_roundedcorners));
                normal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedcorner));
                happy.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedcorner));
                veryHappy.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedcorner));
//
            }
        });

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emotionSelected = "Normal";

//                verySad.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_verysad, 0, 0, 0);
                verySad.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedcorner));

                sad.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedcorner));
                normal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.color_roundedcorners));
                happy.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedcorner));
                veryHappy.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedcorner));
//
            }
        });

        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emotionSelected = "Happy";

//                verySad.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_verysad, 0, 0, 0);
                verySad.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedcorner));

                sad.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedcorner));
                normal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedcorner));
                happy.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.color_roundedcorners));
                veryHappy.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedcorner));
//
            }
        });

        veryHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emotionSelected = "Very Happy";

//                verySad.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_verysad, 0, 0, 0);
                verySad.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedcorner));

                sad.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedcorner));
                normal.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedcorner));
                happy.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.roundedcorner));
                veryHappy.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.color_roundedcorners));
//
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(emotionSelected)){
                    Toast.makeText(getApplicationContext(), "Select an emotion to continue",Toast.LENGTH_SHORT).show();
                }
                else{
                    skip.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.INVISIBLE);
                    db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("ERTbyDates").document(formattedDate).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot = task.getResult();

                            if(documentSnapshot.exists()){

                                existingVerySad = (int)(long)documentSnapshot.get("verySad");
                                existingSad = (int)(long)documentSnapshot.get("sad");
                                existingNormal = (int)(long)documentSnapshot.get("normal");
                                existingHappy = (int)(long)documentSnapshot.get("happy");
                                existingVeryHappy = (int)(long)documentSnapshot.get("veryHappy");

                                if(emotionSelected.equals("Very Sad")){
                                    existingVerySad += 1;
                                }
                                else if(emotionSelected.equals("Sad")){
                                    existingSad += 1;
                                }
                                else if(emotionSelected.equals("Normal")){
                                    existingNormal += 1;
                                }
                                else if(emotionSelected.equals("Happy")){
                                    existingHappy += 1;
                                }
                                else if(emotionSelected.equals("Very Happy")){
                                    existingVeryHappy += 1;
                                }
                                else {
                                    existingNormal += 1;
                                }

                                saveData1();
                            }
                            else {

                                if(emotionSelected.equals("Very Sad")){
                                    existingVerySad += 1;
                                }
                                else if(emotionSelected.equals("Sad")){
                                    existingSad += 1;
                                }
                                else if(emotionSelected.equals("Normal")){
                                    existingNormal += 1;
                                }
                                else if(emotionSelected.equals("Happy")){
                                    existingHappy += 1;
                                }
                                else if(emotionSelected.equals("Very Happy")){
                                    existingVeryHappy += 1;
                                }
                                else {
                                    existingNormal += 1;
                                }

                                saveData1();
                            }
                        }
                    });


                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MREntryActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void saveData1(){
        HashMap<String, Object> data = new HashMap<>();
        data.put("verySad", existingVerySad);
        data.put("sad", existingSad);
        data.put("normal", existingNormal);
        data.put("happy", existingHappy);
        data.put("veryHappy", existingVeryHappy);

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("ERTbyDates").document(formattedDate).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                saveData();
            }
        });
    }

    private void saveData(){
        HashMap<String, Object> data = new HashMap<>();
        data.put("emotion", emotionSelected);
        data.put("date", formattedDate);

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("ERT").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

                skip.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                submit.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "ERT Entry Added", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), MREntryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

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
}