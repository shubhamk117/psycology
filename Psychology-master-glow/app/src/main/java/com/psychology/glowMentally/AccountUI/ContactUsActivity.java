package com.psychology.glowMentally.AccountUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.psychology.glowMentally.R;

public class ContactUsActivity extends AppCompatActivity {

    EditText cause;
    Button sendMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        cause = findViewById(R.id.cause_edit);
        sendMail = findViewById(R.id.sendMail);

        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = cause.getText().toString().trim();

                if(TextUtils.isEmpty(message)){
                    Toast.makeText(getApplicationContext(), "Enter a message to continue", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"glowmentally@outlook.com"});
                    email.putExtra(Intent.EXTRA_SUBJECT, "Message for us");
                    email.putExtra(Intent.EXTRA_TEXT, cause.getText().toString().trim());

                    email.setType("text/plain");
                    email.setPackage("com.google.android.gm");

                    startActivity(Intent.createChooser(email, "Send mail"));
                }
            }
        });

    }
}