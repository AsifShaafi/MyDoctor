package com.example.shaafi.mydoctor.patient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.shaafi.mydoctor.R;

public class AddDoctorForPatient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor_for_patient);
    }

    public void finishActivity(View view) {
        finish();
    }

    public void addDoctorForPatient(View view) {

    }
}
