package com.example.shaafi.mydoctor.patient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shaafi.mydoctor.R;
import com.example.shaafi.mydoctor.utilities.NetworkConnection;

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

import static com.example.shaafi.mydoctor.utilities.DialogClass.successAlert;
import static com.example.shaafi.mydoctor.utilities.DialogClass.warringAlert;

public class AddDoctorForPatient extends AppCompatActivity {

    /*
        An activity which is used as a dialog to add the doctor for a prtient
     */


    /*
        binding the views with varable using butter knife
     */
    @BindView(R.id.doctor_userEditText)
    EditText mDoctorUserEditText;

    //string variables about the patient that will come from intent and be used to store data
    String patient_username, patient_name, age, imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor_for_patient);
        ButterKnife.bind(this);

        //getting the patient details from intent
        patient_name = getIntent().getStringExtra("patient_name");
        patient_username = getIntent().getStringExtra("patient_username");
        age = getIntent().getStringExtra("age");
    }

    /* exits the dialog activity when the user pressed skip button */
    public void finishActivity(View view) {
        finish();
    }

    /*
        responds when user press ok/add button,, it first checks if the doctor username has been given then
    proceed to add it to the patients doctor list as well as the database
     */
    public void addDoctorForPatient(View view) {
        if (!TextUtils.isEmpty(mDoctorUserEditText.getText()) &&
                NetworkConnection.hasNetworkConnection(AddDoctorForPatient.this)) {
            addDoctor();
        } else if (!NetworkConnection.hasNetworkConnection(AddDoctorForPatient.this)) {
            warringAlert(AddDoctorForPatient.this, "Check your network connection");
        } else {
            Toast.makeText(AddDoctorForPatient.this, "Please enter a doctor username", Toast.LENGTH_SHORT).show();
        }
    }

    /*
        a private method that connects with internet and add the doctor the patient added
     */
    private void addDoctor() {
        String url = NetworkConnection.mainUrl + "addDoctorForPatient.php";

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("submit", "submit")
                .add("doctor_username", mDoctorUserEditText.getText().toString())
                .add("patient_username", patient_username)
                .add("patient_name", patient_name)
                .add("age",age)
                .build();

        Request request = new Request.Builder()
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
                        warringAlert(AddDoctorForPatient.this, "Adding Failed");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    final String result = response.body().string();
                    Log.i("okhttp", "result = " + result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result.equals("inserted")) {
                                successAlert(AddDoctorForPatient.this, "Doctor Added");
                            } else {
                                warringAlert(AddDoctorForPatient.this, "Sorry could not add doctor,Please try again");
                            }
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            warringAlert(AddDoctorForPatient.this, "There was some problem, please try again later");
                        }
                    });
                }
            }
        });
    }
//
//    /*
//    a warring alert for user about the error.
// */
//    public void warringAlert(String alert) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this)
//                .setCancelable(true)
//                .setTitle("Ups!!")
//                .setMessage(alert)
//                .setPositiveButton("OK", null);
//
//        Dialog dialog = builder.create();
//        dialog.show();
//    }
//
//    /*
//a warring alert for user about the error.
//*/
//    public void successAlert(String alert) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this)
//                .setCancelable(true)
//                .setTitle("Success!!")
//                .setMessage(alert)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                });
//
//        Dialog dialog = builder.create();
//        dialog.show();
//    }
}
