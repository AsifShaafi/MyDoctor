package com.example.shaafi.mydoctor.patient;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.shaafi.mydoctor.R;
import com.example.shaafi.mydoctor.mainUi.NetworkConnection;

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

public class PatientRegistration extends AppCompatActivity {

    @BindView(R.id.patient_name)
    EditText mPatientName;
    @BindView(R.id.patient_username)
    EditText mPatientUsername;
    @BindView(R.id.patient_age)
    EditText mPatientAge;
    @BindView(R.id.patient_reg_progressBar)
    ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);
        ButterKnife.bind(this);
        mProgress.setVisibility(View.GONE);
    }

    /*
    a warring alert for user about the error.
 */
    public void warringAlert(String alert) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("Ups!!")
                .setMessage(alert)
                .setPositiveButton("OK", null);

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void successAlert(String alert) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("Congrats!!")
                .setMessage(alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setCancelable(false);

        Dialog dialog = builder.create();
        dialog.show();
    }

    /*
        gets called when user press register button,checks if all the fields are checked and there
        is an active internet connection and then invokes the method to register the patient
     */
    public void patient_reg(View view) {

        if (!TextUtils.isEmpty(mPatientAge.getText()) &&
                !TextUtils.isEmpty(mPatientName.getText()) &&
                !TextUtils.isEmpty(mPatientUsername.toString()) &&
                NetworkConnection.hasNetworkConnection(getBaseContext()))
        {
            mProgress.setVisibility(View.VISIBLE);
            proceedToRegistration();
        }
        else if(!NetworkConnection.hasNetworkConnection(getBaseContext()))
        {
            warringAlert("please check your internet connection");
        }
        else {
            warringAlert("please fill all fields");
        }
    }

    /*
        get connection with the network and tryes to register the user as a patient
     */
    private void proceedToRegistration() {

        String url = NetworkConnection.mainUrl + "registerPatient.php";

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("submit", "submit")
                .add("name", mPatientName.getText().toString())
                .add("username", mPatientUsername.getText().toString())
                .add("age", mPatientAge.getText().toString())
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
                        mProgress.setVisibility(View.GONE);
                        warringAlert("Registration failed");
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
                            if (result.equals("registered")) {

                                mProgress.setVisibility(View.GONE);
                                successAlert("Registration Successful, Please login to continue!");

                            } else if (result.equals("found")){
                                mProgress.setVisibility(View.GONE);
                                warringAlert("Sorry username already taken, use another one");
                            }
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgress.setVisibility(View.GONE);
                            warringAlert( "There was some problem, please try again later");
                        }
                    });
                }
            }
        });
    }
}
