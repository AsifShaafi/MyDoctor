package com.example.shaafi.mydoctor.doctor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shaafi.mydoctor.R;
import com.example.shaafi.mydoctor.doctor.listClasses.ListActivity;
import com.example.shaafi.mydoctor.utilities.ImageHandler;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoctorHomePage extends AppCompatActivity {

    public static final String DOCTOR_NAME = "patientList";
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

        // get the json formatted string wraped data from the intent and pass it to parse it for doctor details
        String doctorJson = getIntent().getStringExtra("doctorJsonData");
        mDoctor = getDoctorObjectFromJson(doctorJson);

        mDoctorName.setText(mDoctor.getFull_name());
        mDoctorSector.setText(mDoctor.getSectors());

        /*
            creating the image cache memory to save images
         */
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        ImageHandler.imageCache = new LruCache<>(maxMemory / 8);

    }

    /*
        it takes a json formatted string and parse it and get the doctors details, sets it to a dummy
        doctor object and then returns the dummy object
     */
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

    //setting up the back/home button in the action bar to go to previous page
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && getSupportActionBar() != null) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    //get called when the button for patient list is clicked
    @OnClick(R.id.getPatientListButton)
    public void goToPatientList(View view) {
        Intent intent = new Intent(this, ListActivity.class);

        //sends the doctors username as intent so that the patient list can be retrived
        intent.putExtra(DOCTOR_NAME, mDoctor.getUsername());
        startActivity(intent);
    }
}
