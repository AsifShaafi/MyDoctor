package com.example.shaafi.mydoctor.doctor;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaafi.mydoctor.R;
import com.example.shaafi.mydoctor.doctor.listClasses.ListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DoctorHomePage extends AppCompatActivity {

    public static final String PATIENT_LIST = "patientList";
    DoctorDetails mDoctor;

    @BindView(R.id.doctorNAmeTextView)
    TextView mDoctorName;
    @BindView(R.id.doctorSectorTextView)
    TextView mDoctorSector;
    @BindView(R.id.patientLsitProgressBar)
    ProgressBar mDoctorProgressBar;
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

        getPatientList(mDoctor.getUsername());

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
        intent.putExtra(PATIENT_LIST, mDoctor.getPatientList());
        startActivity(intent);
    }

    private void getPatientList(String username) {

        mDoctorProgressBar.setVisibility(View.VISIBLE);
        mListButton.setVisibility(View.INVISIBLE);

        //String url = "http://192.168.13.2/my_doctor/getPatientList.php";
        String url = "http://www.mydoctorbd.cf/getPatientList.php";
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("submit", "submit")
                .add("username", username)
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DoctorHomePage.this, "Operation failed", Toast.LENGTH_SHORT).show();
                        mDoctorProgressBar.setVisibility(View.INVISIBLE);
                        mListButton.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    try {
                        setPatientListForDoctor(jsonData);

                    } catch (JSONException e) {
                        Log.e("patientList", e.getMessage());
                    }
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DoctorHomePage.this, "unsuccessful", Toast.LENGTH_SHORT).show();
                            mDoctorProgressBar.setVisibility(View.INVISIBLE);
                            mListButton.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
    }

    private void setPatientListForDoctor(String jsonData) throws JSONException {
        //Toast.makeText(DoctorHomePage.this, jsonData, Toast.LENGTH_LONG).show();
        Log.i("Djson" , jsonData);
        JSONObject object = new JSONObject(jsonData);
        JSONArray array = object.getJSONArray("patient_list");

        PatientDetailsForDoctorList[] mList = new PatientDetailsForDoctorList[array.length()];

        for (int i = 0; i < array.length(); i++) {

            PatientDetailsForDoctorList p = new PatientDetailsForDoctorList();

            JSONObject jsonObject = array.getJSONObject(i);
            p.setName(jsonObject.getString("patient_name"));
            p.setUserID(jsonObject.getString("userId"));
            p.setAge(jsonObject.getString("age"));
            Log.i("listP" , jsonObject.getString("patient_name"));

            mList[i] = p;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mDoctorProgressBar.setVisibility(View.INVISIBLE);
                mListButton.setVisibility(View.VISIBLE);
            }
        });

        if (mList.length > 0) {
            mDoctor.setPatientList(mList);
        }
        else {
            //ifthe list is empty then alert user about it
        }

    }
}
