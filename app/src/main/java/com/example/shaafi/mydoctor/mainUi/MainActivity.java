package com.example.shaafi.mydoctor.mainUi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.shaafi.mydoctor.R;

public class MainActivity extends AppCompatActivity {

    public static String USER_MOOD = "userMood";
    public static final String DOCTOR = "doctor";
    public static final String PATIENT = "patient";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
        method is called when any button is pressed and an intent starts login activity
         with a parameter deciding the user mood
     */
    public void proceedToNextStep(View view) {
        Intent intent = new Intent(this, LoginActivity.class);

        switch (view.getId())
        {
            case R.id.doctorButton:
                LoginActivity.CURRENT_USER = DOCTOR;
                break;
            case R.id.patientButton:
                LoginActivity.CURRENT_USER = PATIENT;
                break;
        }

        startActivity(intent);

        /*
            setting custom transition or animation between the activity changing
         */
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
