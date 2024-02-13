package com.psychology.glowMentally;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideoPlayerActivity extends AppCompatActivity {

    TextView heading;

    String headingText="Know More";
    String videoID="w3-1_OyqdDs";
    FrameLayout appBar;
    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        heading=findViewById(R.id.heading);
        Intent intent = getIntent();
        headingText = intent.getStringExtra("headingText");
        videoID = intent.getStringExtra("videoID");
        heading.setText(headingText);
        appBar = findViewById(R.id.appBar);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            appBar.setBackgroundColor(Color.parseColor("#ff5733"));
            if(headingText.contains("Emotion")){
                window.setStatusBarColor(this.getResources().getColor(R.color.violetERT));
                appBar.setBackgroundColor(Color.parseColor("#8f00ff"));
            }else if(headingText.contains("Mind")){
                window.setStatusBarColor(this.getResources().getColor(R.color.orangeMR));
                appBar.setBackgroundColor(Color.parseColor("#ff5733"));
            }else{
                window.setStatusBarColor(this.getResources().getColor(R.color.bluePA));
                appBar.setBackgroundColor(Color.parseColor("#00B5E2"));
            }
        }

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(videoID, 0);
                youTubePlayer.pause();
            }
        });

        backBtn=findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}