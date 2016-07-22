package com.example.shaafi.mydoctor.patient;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.shaafi.mydoctor.R;
import com.example.shaafi.mydoctor.mainUi.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PatientHomePage extends AppCompatActivity {

    PatientDetails mPatientDetails;

    //binding the view with butter knife
    @BindView(R.id.patientNameTextView)
    TextView mPatientName;
    @BindView(R.id.patientUsernameTextView)
    TextView mPatientUsername;
    @BindView(R.id.patientAgeTextView)
    TextView mPatientAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home_page);
        ButterKnife.bind(this);
        setupActionBar();

        String jsonForPatient = getIntent().getStringExtra(LoginActivity.PATIENT_JSON_DATA);
        mPatientDetails = getPatientDetailsFromJsonString(jsonForPatient);

    }

    private PatientDetails getPatientDetailsFromJsonString(String jsonForPatient) {

        PatientDetails dummyPatient = new PatientDetails();



        return dummyPatient;
    }

    /*
        getting the action bar and setting the home button on it to go to previous activity
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }
}
