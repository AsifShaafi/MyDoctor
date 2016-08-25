package com.example.shaafi.mydoctor.patient;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.shaafi.mydoctor.R;
import com.example.shaafi.mydoctor.mainUi.LoginActivity;
import com.example.shaafi.mydoctor.utilities.DialogClass;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PatientHomePage extends AppCompatActivity {

    public static String PATIENT_DATA_LIST;

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

        setViewInLayout(mPatientDetails);

    }

    /*
        setting the patient home page with patient data
     */
    private void setViewInLayout(PatientDetails mPatientDetails) {
        mPatientName.setText(mPatientDetails.getFullName());
        mPatientUsername.setText(mPatientDetails.getUserName());
        mPatientAge.setText(mPatientDetails.getAge());
    }

    /*
        making a dummy patient object and populating it with the data found from the jsonString
        which was sent from the intent and then returning the dummy object to populate the main object
     */
    private PatientDetails getPatientDetailsFromJsonString(String jsonForPatient) {

        PatientDetails dummyPatient = new PatientDetails();

        try {
            JSONObject object = new JSONObject(jsonForPatient);
            JSONObject patientObject = object.getJSONObject("patient");

            dummyPatient.setFullName(patientObject.getString("name"));
            dummyPatient.setUserName(patientObject.getString("username"));
            dummyPatient.setAge(patientObject.getString("age"));
            dummyPatient.setBirthDay(patientObject.getString("birthday"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    /*
        start the process to add a doctor to the patient's doctor list
        it starts a activity which has a alert dialog layout
     */
    public void proceedToAddDoctor(View view) {
        Intent intent = new Intent(this, AddDoctorForPatient.class);
        intent.putExtra("patient_username", mPatientDetails.getUserName());
        intent.putExtra("patient_name", mPatientDetails.getFullName());
        intent.putExtra("age", mPatientDetails.getAge());
        startActivity(intent);
    }

    public void proceedToAddData(View view) {
        DialogClass.dataListForPatient(this);
    }

    public void showPatientData(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("PatientData")
                .setMessage(PATIENT_DATA_LIST)
                .setPositiveButton("OK", null);

        Dialog dialog = builder.create();
        dialog.show();
    }
}
