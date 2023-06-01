package com.psychology.glowMentally;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.webkit.WebView;

import com.psychology.glowMentally.R;
import com.psychology.glowMentally.databinding.ActivityAboutTheAppBinding;

public class AboutTheAppActivity extends AppCompatActivity {

    WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_the_app);
        webView=findViewById(R.id.webView);
//        webView.loadUrl("https://docs.google.com/document/d/1QneXgRnA73Zgxuhgly_MaTJsLZbfWfD9/edit?usp=sharing&ouid=113224595630128607317&rtpof=true&sd=true");

        webView.loadUrl("https://docs.google.com/document/d/1d9hcnsqtiR8NKiq8NP28rQ6Yfsver1CcjgOndThSyWM/edit?usp=sharing");
        //        webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url="+);
    }

}