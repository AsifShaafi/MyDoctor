package com.example.shaafi.mydoctor.mainUi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shaafi.mydoctor.R;
import com.example.shaafi.mydoctor.doctor.DoctorHomePage;
import com.example.shaafi.mydoctor.doctor.DoctorRegistration;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class LoginActivity extends AppCompatActivity {

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

    private final String CURRENT_USER = MainActivity.USER_MODE;
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

    /*  setting the focus change in the edit text fields so that when a user leaves a editTextField it
     *  checks if it is filled up or nor,, if not then show error
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
    checks if user has internet connection
     */
    private boolean hasNetworkConnection() {

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    /*
     first checks if all the fields are filled and then trigger the login process to check if
      username exists and then matches the password
     */
    private void proceedToLogin() {

        if (userTyped && passwordTyped) {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (hasNetworkConnection()) {
                //showProgress(true);
                connectToNetwork(CURRENT_USER, mUsernameView.getText().toString().toLowerCase());
            } else {
                Toast.makeText(this, "Network connection failed!\nCheck your internet connection", Toast.LENGTH_LONG).show();
            }
        } else {
            //Toast.makeText(LoginActivity.this, "Please fill all fields first", Toast.LENGTH_SHORT).show();
            NetworkConnection.warringAlert("Please fill all fields first");
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
            Toast.makeText(this, R.string.under_construction, Toast.LENGTH_LONG).show();
        }
    }

    /*
        get everything ready for network based works and then calls the background class to do
        network work in the background and checks the user for login
     */
    private void connectToNetwork(String option, String username) {
        String url;

        //url based on the user type
        if (option.equals(MainActivity.DOCTOR)) {
            //url = "http://192.168.13.2/my_doctor/checkUserAsDoctor.php";
            url = "http://www.mydoctorbd.cf/checkUserAsDoctor.php";
        } else {
            url = "";
        }

        /*
            ready a request body for the post form data with relative form data
            format: .add(form_variable_name/post_value_index, value)
         */
        RequestBody formBody = new FormBody.Builder()
                .add("submit", "submit")
                .add("username", username)
                .build();

        NetworkConnection.RequestPackage requestPackage = new NetworkConnection.RequestPackage(url, formBody);
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
            know that checking is happenning in the background
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

            jsonData = NetworkConnection.getJsonResultFromNetwork(LoginActivity.this,requestPackage.getUrl(), requestPackage.getRequestBody());

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {

            mProgressDialog.dismiss();

            Toast.makeText(mContext, jsonData, Toast.LENGTH_SHORT).show();

            if (jsonData != null) {

                if (jsonData.equals("not found")) {
                    NetworkConnection.warringAlert(getString(R.string.username_dont_match));
                } else {
                    try {

                        //getting the json object sent from the server to a json object
                        JSONObject object = new JSONObject(jsonData);
                        JSONObject jsonObject;

                        //checking if the child json object is of doctor_json_object or patient_json_object
                        if ((jsonObject = object.getJSONObject("doctor")) != null) {
                            String password = jsonObject.getString("password");
                            if (password.equals(mPasswordView.getText().toString().toLowerCase())) {
                                Intent intent = new Intent(LoginActivity.this, DoctorHomePage.class);
                                intent.putExtra("doctorJsonData", jsonData);
                                //Toast.makeText(LoginActivity.this, jsonData, Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            } else {
                                //warringAlert(getString(R.string.username_dont_match));
                                NetworkConnection.warringAlert(getString(R.string.username_dont_match));
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                NetworkConnection.warringAlert("Network error, please check your connection and try again!");
            }
        }
    }
}

