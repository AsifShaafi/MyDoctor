package com.example.shaafi.mydoctor.patient;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shaafi.mydoctor.R;
import com.example.shaafi.mydoctor.mainUi.LoginActivity;
import com.example.shaafi.mydoctor.utilities.ImageHandler;
import com.example.shaafi.mydoctor.utilities.NetworkConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Asif Imtiaz Shaafi, on 03-Jul-16.
 * Email: a15shaafi.209@gmail.com
 */
public class PatientHomeForDoctor extends AppCompatActivity {

    private String patientName;

    @BindView(R.id.patientHomeForDoctorImage)
    ImageView mImageViewOfPatientForDoctor;
    @BindView(R.id.patientNameForDoctorTextView)
    TextView mNameTextViewOfPatientForDoctor;
    @BindView(R.id.patientUsernameForDoctorTextView)
    TextView mUsernameTextViewOfPatientForDoctor;
    @BindView(R.id.patientAgeForDoctorTextView)
    TextView mAgeTextViewOfPatientForDoctor;
    @BindView(R.id.progressBarForDoctorPatient)
    ProgressBar mPatientHomeForDoctorProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home_for_doctor);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ButterKnife.bind(this);

        patientName = getIntent().getStringExtra("patient_clicked");
        getPatientFromDatabase(patientName);

    }

    /*
       setting the patient home page with patient data
    */
    private void setViewInLayout(PatientDetails mPatientDetails) {
        mUsernameTextViewOfPatientForDoctor.setText(mPatientDetails.getUserName());
        mNameTextViewOfPatientForDoctor.setText(mPatientDetails.getFullName());
        mAgeTextViewOfPatientForDoctor.setText(mPatientDetails.getAge());

        Bitmap bitmap = ImageHandler.imageCache.get(mPatientDetails.getUserName());

        if (bitmap != null) {
            mImageViewOfPatientForDoctor.setImageBitmap(bitmap);
        } else {
            ImageHandler.PatientNView mPatientNView =
                    new ImageHandler.PatientNView(mPatientDetails.getUserName(),
                            mPatientHomeForDoctorProgressBar,
                            mImageViewOfPatientForDoctor);
            ImageHandler.ImageLoader loader = new ImageHandler.ImageLoader(mPatientNView);
            loader.execute(mPatientNView);
        }
    }

    private void getPatientFromDatabase(String name) {
        String url = NetworkConnection.mainUrl + LoginActivity.CHECK_USER_AS_PATIENT_PHP;
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("submit", "submit")
                .add("username", name)
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String json = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getPatientDetailsFromJsonString(json);
                        }
                    });
                }
            }
        });
    }

    /*
        making a dummy patient object and populating it with the data found from the jsonString
        which was sent from the intent and then returning the dummy object to populate the main object
     */
    private void getPatientDetailsFromJsonString(String jsonForPatient) {

        PatientDetails dummyPatient = new PatientDetails();

        try {
            JSONObject object = new JSONObject(jsonForPatient);
            JSONObject patientObject = object.getJSONObject("patient");
            dummyPatient.setFullName(patientObject.getString("name"));
            dummyPatient.setUserName(patientObject.getString("username"));
            dummyPatient.setAge(patientObject.getString("age"));
            dummyPatient.setBirthDay(patientObject.getString("birthday"));
            setViewInLayout(dummyPatient);

        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }


}
