package com.psychology.glowMentally.MainUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.psychology.glowMentally.AboutTheAppActivity;
import com.psychology.glowMentally.AccountUI.ContactUsActivity;
import com.psychology.glowMentally.AccountUI.EditAccountActivity;
import com.psychology.glowMentally.AccountUI.KnowMoreActivity;
import com.psychology.glowMentally.LoginUI.LoginMainActivity;
import com.psychology.glowMentally.R;
import com.psychology.glowMentally.StaticVars;

public class AccountFragment extends Fragment {

    Button aboutTheApp, knowMore, contactUs, privacyPolicy, logout;

    TextView userName, userMobileNumber, userWorkWeek, userWorkTimings;
    Button editWorkCycle, rateApp;
    ImageButton editName;
    ImageView img;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String imgUrl="";

    SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        logout = view.findViewById(R.id.logout);

        userName = view.findViewById(R.id.userName);
        userMobileNumber = view.findViewById(R.id.userMobileNumber);
        userWorkWeek = view.findViewById(R.id.userWorkWeek);
        userWorkTimings = view.findViewById(R.id.userWorkTimings);
        editWorkCycle = view.findViewById(R.id.editWorkingCycle);
        rateApp = view.findViewById(R.id.rateApp);
        editName = view.findViewById(R.id.editName);
        img=view.findViewById(R.id.img);
        aboutTheApp = view.findViewById(R.id.aboutTheApp);
        knowMore = view.findViewById(R.id.knowMore);
        contactUs = view.findViewById(R.id.contactUs);
        privacyPolicy = view.findViewById(R.id.privacyPolicy);

        aboutTheApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://docs.google.com/document/d/1QneXgRnA73Zgxuhgly_MaTJsLZbfWfD9/edit?usp=sharing&ouid=113224595630128607317&rtpof=true&sd=true"); // missing 'http://' will cause crashed

//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                Intent intent = new Intent(getActivity(), AboutTheAppActivity.class);
                startActivity(intent);
            }
        });

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri uri = Uri.parse("https://docs.google.com/document/d/13Ht2OoE9B8cPaxLhvb1jfTrMaYnATYYH/edit?usp=sharing&ouid=113224595630128607317&rtpof=true&sd=true"); // missing 'http://' will cause crashed
                Uri uri = Uri.parse("https://docs.google.com/document/d/1ZONLVmphN47TtrRkrElLRUd_IOrQxGRrEU_RgOTmCGM/edit?usp=sharing"); // missing 'http://' will cause crashed

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        knowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), KnowMoreActivity.class);
                startActivity(intent);
            }
        });

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ContactUsActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs = getActivity().getSharedPreferences("Psychology", Context.MODE_PRIVATE);
                SharedPreferences.Editor myEdit = prefs.edit();
                myEdit.putBoolean("isFirstTime",true);
                myEdit.commit();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), LoginMainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditAccountActivity.class);
                startActivity(intent);
            }
        });

        editWorkCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditAccountActivity.class);
                startActivity(intent);
            }
        });

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                String upperString = value.getString("name").substring(0, 1).toUpperCase() + value.getString("name").substring(1).toLowerCase();
                userName.setText(value.getString("name"));
                userName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                userMobileNumber.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                userWorkWeek.setText(value.getString("workingDaysText"));
                userWorkTimings.setText(value.getString("workStartTime") + " - " + value.getString("workEndingTime"));
                imgUrl = value.getString("image");
                Glide.with(getContext())
                        .load(imgUrl)
                        .into(img);
                StaticVars.current_img=imgUrl;
            }
        });
        return view;
    }
}