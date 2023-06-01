package com.psychology.glowMentally.LoginUI;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.psychology.glowMentally.MainActivity;
import com.psychology.glowMentally.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class LoginDetailsCollectionActivity extends AppCompatActivity {

    ArrayList<String> ages = new ArrayList<>();
    EditText name, age;
    Button getStarted;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    RadioGroup genders;
    RadioButton selectedGenderButton;

    String gender;

    EditText workDays;
    boolean[] checkedWorkDays;
    ArrayList<Integer> userWorkDaysSelections = new ArrayList<>();

    Button startTime, endTime;
    int tHour, tMinute;

    String workStartTime = "", workEndTime = "";

    EditText company, role;

    ImageView img;
    CardView camerabtn;
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;
    StorageReference storageReference;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    SharedPreferences prefs;
    boolean imageSelected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_details_collection);
        prefs = getSharedPreferences("Psychology", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = prefs.edit();
        myEdit.putBoolean("isFirstTime",true);
        myEdit.commit();
        storageReference = storage.getReference();
        age = findViewById(R.id.age_edit);
        name = findViewById(R.id.name_edit);
        getStarted = findViewById(R.id.getStarted);
        genders = findViewById(R.id.radioGroup);
        workDays = findViewById(R.id.working_days_edit);
        endTime = findViewById(R.id.workEndTime);
        startTime = findViewById(R.id.workStartTime);
        company = findViewById(R.id.company_edit);
        role = findViewById(R.id.designation_edit);
        img=findViewById(R.id.img);
        camerabtn=findViewById(R.id.cameraBtn);

        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

        final String[] WORKDAYS = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        checkedWorkDays = new boolean[WORKDAYS.length];

        workDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginDetailsCollectionActivity.this);
                builder.setTitle("Work Days");
                builder.setMultiChoiceItems(WORKDAYS, checkedWorkDays, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked){
                            if(! userWorkDaysSelections.contains(position)){
                                userWorkDaysSelections.add(position);
                            }
                        }
                        else if(userWorkDaysSelections.contains(position)){
                            userWorkDaysSelections.remove(userWorkDaysSelections.indexOf(position));
                        }
                    }
                });

                builder.setCancelable(false);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selections = "";
                        for(int i =0; i<userWorkDaysSelections.size();i++){
                            selections = selections + WORKDAYS[userWorkDaysSelections.get(i)];
                            if( i != userWorkDaysSelections.size() - 1){
                                selections = selections + ", ";
                            }
                        }
                        workDays.setText(selections);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        checkedWorkDays = new boolean[WORKDAYS.length];
                        userWorkDaysSelections.clear();
                        workDays.setText("");
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimeButton("start");
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimeButton("end");
            }
        });

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredName = name.getText().toString().trim();
                String selectedAge = age.getText().toString().trim();
                String enteredCompanyName = company.getText().toString().trim();
                String enteredWorkingRole = role.getText().toString().trim();
                String selectedWorkingDays = workDays.getText().toString().trim();

                int selectedGender = genders.getCheckedRadioButtonId();

                if(selectedGender == -1){
                    Toast.makeText(getApplicationContext(), "Please select your gender",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(enteredName)){
                    Toast.makeText(getApplicationContext(), "Please enter your name",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(selectedAge)){
                    Toast.makeText(getApplicationContext(), "Please enter your age", Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(selectedAge) > 100 || Integer.parseInt(selectedAge) <18){
                    Toast.makeText(getApplicationContext(), "Please enter a age between 18 and 100", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(enteredCompanyName)){
                    Toast.makeText(getApplicationContext(), "Please enter the company name you work at", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(enteredWorkingRole)){
                    Toast.makeText(getApplicationContext(), "Please enter your designation at work", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(selectedWorkingDays)){
                    Toast.makeText(getApplicationContext(), "Please select your working days", Toast.LENGTH_SHORT).show();
                }
                else if(workStartTime.equals("")){
                    Toast.makeText(getApplicationContext(), "Please select your work starting time", Toast.LENGTH_SHORT).show();
                }
                else if(workEndTime.equals("")){
                    Toast.makeText(getApplicationContext(), "Please select your work ending time", Toast.LENGTH_SHORT).show();
                }else if(selectedWorkingDays.equals("")){
                    Toast.makeText(getApplicationContext(), "Please select your working days", Toast.LENGTH_SHORT).show();
                }else if(!imageSelected){
                    Toast.makeText(getApplicationContext(), "Please select your Profile Image", Toast.LENGTH_SHORT).show();
                }
                else {
                    selectedGenderButton = findViewById(selectedGender);
                    uploadImage();
                }
            }
        });

    }

    private  void uploadData(String imgUrl){
        String enteredName = name.getText().toString().trim();
        String selectedAge = age.getText().toString().trim();
        String enteredCompanyName = company.getText().toString().trim();
        String enteredWorkingRole = role.getText().toString().trim();
        String selectedWorkingDays = workDays.getText().toString().trim();
        int selectedGender = genders.getCheckedRadioButtonId();
        if(selectedGenderButton.getText().toString().equals("Male")){
            gender = "Male";
        }
        else if(selectedGenderButton.getText().toString().equals("Female")){
            gender = "Female";
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("name",enteredName);
        data.put("age",Integer.parseInt(selectedAge));
        data.put("gender",gender);
        data.put("company", enteredCompanyName);
        data.put("designation", enteredWorkingRole);
        data.put("workingDays", userWorkDaysSelections);
        data.put("workingDaysText", selectedWorkingDays);
        data.put("workStartTime", workStartTime);
        data.put("workEndingTime", workEndTime);
        data.put("image",imgUrl);

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void handleTimeButton(String type){
        Calendar calendar = Calendar.getInstance();
        int Hour = calendar.get(Calendar.HOUR);
        int Minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                tHour = hourOfDay;
                tMinute = minute;

                Calendar calendar = Calendar.getInstance();
                calendar.set(0,0,0,tHour,tMinute);

                if(type.equals("start")){
                    startTime.setText("Work Start Time - " + DateFormat.format("hh:mm aa", calendar));
                    workStartTime = String.valueOf(DateFormat.format("hh:mm aa", calendar));
                }
                else{
                    endTime.setText("Work End Time - " + DateFormat.format("hh:mm aa", calendar));
                    workEndTime = String.valueOf(DateFormat.format("hh:mm aa", calendar));
                }
            }
        },Hour,Minute,false);
        timePickerDialog.show();
    }

    private void SelectImage()
    {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                Glide.with(getApplicationContext())
                        .load(filePath)
                        .into(img);
                imageSelected=true;

//                img.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            String randomString = UUID.randomUUID().toString();
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + randomString);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(LoginDetailsCollectionActivity.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
//                                    Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
//                                    if(downloadUri.isSuccessful()){
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.i("GENERATEDPATH: ",uri.toString());
//                                        System.out.println("## Stored path is "+generatedFilePath);
                                            uploadData(uri.toString());
                                        }
                                    });
//                                        String generatedFilePath = downloadUri.getResult().toString();

//                                    }
//                                    String urlData = taskSnapshot.getStorage().getDownloadUrl().toString();
//                                    Log.i("URLDATA: ",urlData);

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(LoginDetailsCollectionActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            new com.google.firebase.firestore.OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            };
                        }});
        }
    }


}