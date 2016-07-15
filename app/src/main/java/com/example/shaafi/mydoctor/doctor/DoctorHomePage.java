package com.example.shaafi.mydoctor.doctor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shaafi.mydoctor.R;
import com.example.shaafi.mydoctor.doctor.listClasses.ListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoctorHomePage extends AppCompatActivity {

    public static final String PATIENT_LIST = "patientList";
    DoctorDetails mDoctor;

    @BindView(R.id.doctorNAmeTextView)
    TextView mDoctorName;
    @BindView(R.id.doctorSectorTextView)
    TextView mDoctorSector;
    @BindView(R.id.getPatientListButton)
    Button mListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home_page);
        setupActionBar();
        ButterKnife.bind(this);

        String doctorJson = getIntent().getStringExtra("doctorJsonData");
        mDoctor = getDoctorObjectFromJson(doctorJson);

        mDoctorName.setText(mDoctor.getFull_name());
        mDoctorSector.setText(mDoctor.getSectors());

    }


    private DoctorDetails getDoctorObjectFromJson(String doctorJson) {
        DoctorDetails mDoctor = new DoctorDetails();

        try {
            JSONObject object = new JSONObject(doctorJson);
            JSONObject doctorObject = object.getJSONObject("doctor");
            mDoctor.setId(doctorObject.getInt("id"));
            mDoctor.setUsername(doctorObject.getString("user_name"));
            mDoctor.setFull_name(doctorObject.getString("full_name"));
            mDoctor.setSectors(doctorObject.getString("sectors"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mDoctor;
    }

    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @OnClick(R.id.getPatientListButton)
    public void goToPatientList(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        //intent.putExtra(PATIENT_LIST, mDoctor.getPatientList());
        intent.putExtra(PATIENT_LIST, mDoctor.getUsername());
        startActivity(intent);
    }
}
