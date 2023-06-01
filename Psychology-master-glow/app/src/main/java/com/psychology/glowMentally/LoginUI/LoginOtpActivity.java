package com.psychology.glowMentally.LoginUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.psychology.glowMentally.MainActivity;
import com.psychology.glowMentally.R;

import java.util.concurrent.TimeUnit;

public class LoginOtpActivity extends AppCompatActivity {

    EditText otp;
    String sentOTP;
    String mobile;

    Button validate;

    FirebaseAuth firebaseAuth;

    ProgressBar progressBar;
    TextView time,mobile_no;

    Button resend;

    String code;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        prefs = getSharedPreferences("Psychology", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = prefs.edit();
        myEdit.putBoolean("isFirstTime",true);
        myEdit.commit();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseApp.initializeApp(this);
//        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
//        firebaseAppCheck.installAppCheckProviderFactory( SafetyNetAppCheckProviderFactory.getInstance());
//        firebaseAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        Intent intent = getIntent();
        sentOTP = intent.getStringExtra("otp");
        mobile = intent.getStringExtra("phone");

        otp = findViewById(R.id.login_otp_edit);
        validate = findViewById(R.id.validate_button);
        time = findViewById(R.id.textView24);
        resend = findViewById(R.id.resend);
        mobile_no=findViewById(R.id.validate_otp_sub_header);
        String number_text="A one-time password has been sent to " + "<b><font color='#000000'>"+"+91 "+mobile+"</font></b>";
        mobile_no.setText(Html.fromHtml(number_text));
        progressBar = findViewById(R.id.progressBar7);

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                validate.setEnabled(false);
                if(TextUtils.isEmpty(otp.getText().toString())){
                    Toast.makeText(LoginOtpActivity.this,"Please enter OTP", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    validate.setEnabled(true);
                }
                else {
                    verifyOTPSign();
                }
            }
        });

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                time.setText("Please wait: " + millisUntilFinished / 1000 + " seconds");
            }

            public void onFinish() {
                resend.setVisibility(View.VISIBLE);
                time.setText("done!");
            }
        }.start();

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTP();
                resend.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void sendOTP(){

        String number = "+" + String.valueOf(GetCountryZipCode()) + mobile;
        sentOTP = "";
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        );
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Toast.makeText(LoginOtpActivity.this,"OTP sent", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(LoginOtpActivity.this,"Please enter correct Number", Toast.LENGTH_SHORT).show();
            Log.i("FIREERROR: ",e.getLocalizedMessage());

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            sentOTP = s;
            Toast.makeText(getApplicationContext(), "OTP resend successful", Toast.LENGTH_SHORT).show();
        }

    };

    private void verifyOTPSign(){
        code = otp.getText().toString();
        Log.i("code",code);
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(sentOTP,code);
        signInWithPhoneAuthCredential(phoneAuthCredential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    redirect();
                }
                else {
                    Toast.makeText(LoginOtpActivity.this, "Please enter correct otp", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    validate.setEnabled(true);
                }
            }
        });
    }

    public String GetCountryZipCode(){
        String CountryID="";
        String CountryZipCode="";

        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID= manager.getSimCountryIso().toUpperCase();
        String[] rl=this.getResources().getStringArray(R.array.CountryCodes);
        for(int i=0;i<rl.length;i++){
            String[] g=rl[i].split(",");
            if(g[1].trim().equals(CountryID.trim())){
                CountryZipCode=g[0];
                break;
            }
        }
        return CountryZipCode;
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