package com.example.shaafi.mydoctor.mainUi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shaafi.mydoctor.R;
import com.example.shaafi.mydoctor.doctor.DoctorHomePage;
import com.example.shaafi.mydoctor.doctor.DoctorRegistration;
import com.example.shaafi.mydoctor.patient.PatientHomePage;
import com.example.shaafi.mydoctor.patient.PatientRegistration;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class LoginActivity extends AppCompatActivity {

    public static final String PATIENT_JSON_DATA = "patientJsonData";
    public static final String DOCTOR_JSON_DATA = "doctorJsonData";

    // UI references. binding all the fields with ButterKnife support library
    @BindView(R.id.username_editText)
    EditText mUsernameView;
    @BindView(R.id.password)
    EditText mPasswordView;
    @BindView(R.id.login_progress)
    View mProgressView;
    @BindView(R.id.login_form)
    View mLoginFormView;
    @BindView(R.id.passwordInputLayout)
    TextInputLayout mTextInputLayout;

    ProgressDialog mProgressDialog;

    public static String CURRENT_USER;
    View focusView = null;
    boolean userTyped = false;
    boolean passwordTyped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setupActionBar();

        setOnFocusForFields();
        mProgressDialog = new ProgressDialog(LoginActivity.this);
        mProgressDialog.setCancelable(false);

        // Set up the login form.
        if (CURRENT_USER.equals(MainActivity.PATIENT)) {
            mTextInputLayout.setVisibility(View.INVISIBLE);
            passwordTyped = true;
        }

    }

    //setting up the back/home button in the action bar to go to previous page
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    /*  setting the focus change in the edit text fields so that when a user leaves a editTextField
         *  it checks if it is filled up or nor,, if not then show error
         */
    private void setOnFocusForFields() {
        //focus change for password field
        mPasswordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(mPasswordView.getText().toString())) {
                        mPasswordView.setError(getString(R.string.enter_password));
                        focusView = mPasswordView;
                        passwordTyped = false;
                    } else {
                        passwordTyped = true;
                    }
                } else {
                    focusView = mPasswordView;
                }
            }
        });

        //focus change for username field
        mUsernameView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(mUsernameView.getText().toString())) {
                        mUsernameView.setError(getString(R.string.error_field_required));
                        focusView = mUsernameView;
                        userTyped = false;
                    } else {
                        userTyped = true;
                    }
                } else {
                    focusView = mUsernameView;
                }
            }
        });
    }

    //checks the editText fields to if they have been correctly filled up
    private void checkAllField() {

        userTyped = !TextUtils.isEmpty(mUsernameView.getText());

        passwordTyped = !TextUtils.isEmpty(mPasswordView.getText());

    }


    /*
     first checks if all the fields are filled and then trigger the login process to check if
      username exists and then matches the password
     */
    private void proceedToLogin() {

        if (userTyped && passwordTyped && CURRENT_USER.equals(MainActivity.DOCTOR)) {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (NetworkConnection.hasNetworkConnection(this)) {
                //showProgress(true);
                connectToNetwork(CURRENT_USER, mUsernameView.getText().toString().toLowerCase());
            } else {
                Toast.makeText(this,
                        "Network connection failed!\nCheck your internet connection",
                        Toast.LENGTH_LONG)
                        .show();
            }
        } else if (userTyped && CURRENT_USER.equals(MainActivity.PATIENT)) {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (NetworkConnection.hasNetworkConnection(this)) {
                //showProgress(true);
                connectToNetwork(CURRENT_USER, mUsernameView.getText().toString().toLowerCase());
            } else {
                Toast.makeText(this,
                        "Network connection failed!\nCheck your internet connection",
                        Toast.LENGTH_LONG)
                        .show();
            }
        } else {
            DialogClass.warringAlert(LoginActivity.this,"Please fill all fields first");
            focusView.requestFocus();
        }
    }

    /*
        the method is invoked when login button is pressed. It first checks if the fields are
        filled and then invoke the login process method
     */
    public void login(View view) {
        checkAllField();
        proceedToLogin();
    }

    /*
        this method takes the user to registration activity to register according to what type of
        the user is by checking the user choice in home page
     */
    public void goToRegistration(View view) {

        if (CURRENT_USER.equals(MainActivity.DOCTOR)) {
            startActivity(new Intent(this, DoctorRegistration.class));
        } else {
            startActivity(new Intent(this, PatientRegistration.class));
        }
    }

    /*
        get everything ready for network based works and then calls the background class to do
        network work in the background and checks the user for login
     */
    private void connectToNetwork(String option, String username) {
        String path;

        //url based on the user type
        if (option.equals(MainActivity.DOCTOR)) {
            //url = "http://192.168.13.2/my_doctor/checkUserAsDoctor.php";
            path = "checkUserAsDoctor.php";
        } else {
            path = "checkUserAsPatient.php";
        }

        /*
            ready a request body for the post form data with relative form data
            format: .add(form_variable_name/post_value_index, value)
         */
        RequestBody formBody = new FormBody.Builder()
                .add("submit", "submit")
                .add("username", username)
                .build();

        NetworkConnection.RequestPackage requestPackage =
                new NetworkConnection.RequestPackage(path, formBody);

        BackgroundTasks tasks = new BackgroundTasks(this);
        tasks.execute(requestPackage); //passing the formbody to the background task
    }

    /*
        does all the network related work in background and fetches all the data of the user,,checks
        if they are correct. the data is returned as json from the server and it is parsed and
        used to do further work
     */
    private class BackgroundTasks extends AsyncTask<NetworkConnection.RequestPackage, Void, String> {

        Context mContext;
        String jsonData;

        public BackgroundTasks(Context context) {
            mContext = context;
        }

        /*
            called before the background task is done.. a progress dialog is shown to let the user
            know that checking is happening in the background
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage("Logging In...");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(NetworkConnection.RequestPackage... params) {

            NetworkConnection.RequestPackage requestPackage = params[0];

            jsonData = NetworkConnection.getJsonResultFromNetwork(
                    LoginActivity.this,
                    requestPackage.getUrl(),
                    requestPackage.getRequestBody()
            );

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {

            mProgressDialog.dismiss();

            if (jsonData != null) {

                if (jsonData.equals("not found")) {
                    DialogClass.warringAlert(LoginActivity.this, getString(R.string.username_dont_match));
                } else {
                    try {

                        //getting the json object sent from the server to a json object
                        JSONObject object = new JSONObject(jsonData);
                        JSONObject jsonObject;

                        //checking if the child json object is of doctor_json_object or patient_json_object
                        //Toast.makeText(getBaseContext(), jsonData, Toast.LENGTH_SHORT).show();

                        if (jsonData.contains("doctor")) {
                            jsonObject = object.getJSONObject("doctor");
                            String password = jsonObject.getString("password");
                            if (password.equals(mPasswordView.getText().toString().toLowerCase())) {

                                /*
                                    if the username and password matches then start the doctor's
                                    homepage activity and send the doctor details found from the
                                    server as an intent which is a json formatted data wraped as
                                    a string
                                 */
                                Intent intent = new Intent(LoginActivity.this, DoctorHomePage.class);
                                intent.putExtra(DOCTOR_JSON_DATA, jsonData);
                                startActivity(intent);
                            } else {
                                DialogClass.warringAlert(LoginActivity.this,getString(R.string.username_dont_match));
                            }

                        } else if (jsonData.contains("patient")) {
                            Intent intent = new Intent(LoginActivity.this, PatientHomePage.class);
                            intent.putExtra(PATIENT_JSON_DATA, jsonData);
                            startActivity(intent);
                        }

                    } catch (JSONException e) {
                        Log.e("json", e.getMessage());
                    }
                }
            } else {
                DialogClass.warringAlert(LoginActivity.this, "Network error, please check your connection and try again!");
            }
        }
    }

}

