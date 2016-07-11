package com.example.shaafi.mydoctor.mainUi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaafi.mydoctor.R;
import com.example.shaafi.mydoctor.doctor.DoctorHomePage;
import com.example.shaafi.mydoctor.doctor.DoctorRegistration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

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
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    // UI references.
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

        attemptLogin();

        // Set up the login form.
        if (CURRENT_USER.equals(MainActivity.PATIENT)) {
            mTextInputLayout.setVisibility(View.INVISIBLE);
            passwordTyped = true;
        }

    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Check for a valid password
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
                }
                else {
                    focusView = mPasswordView;
                }
            }
        });


        // Check for a valid username.
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

                }
                else {
                    focusView = mUsernameView;
                }
            }
        });
    }

    private void checkAllField() {

        if (TextUtils.isEmpty(mUsernameView.getText())) {
            userTyped = false;
        } else
            userTyped = true;

        if (TextUtils.isEmpty(mPasswordView.getText()))
            passwordTyped = false;
        else
            passwordTyped = true;

    }

    private boolean hasNetworkConnection() {

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    private void proceedToLogin() {

        if (userTyped && passwordTyped) {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (hasNetworkConnection()) {
                showProgress(true);
                connectToNetwork(CURRENT_USER, mUsernameView.getText().toString());
            }else {
                Toast.makeText(this, "Network connection failed!\nCheck your internet connection", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "Please fill all fields first", Toast.LENGTH_SHORT).show();
            focusView.requestFocus();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void login(View view) {
        checkAllField();
        proceedToLogin();
    }

    public void goToRegistration(View view) {

        if (CURRENT_USER.equals(MainActivity.DOCTOR)) {
            startActivity(new Intent(this, DoctorRegistration.class));
        } else {
            Toast.makeText(this, R.string.under_construction, Toast.LENGTH_LONG).show();
        }
    }

    private void connectToNetwork(String option, String username) {
        String url;

        if (option.equals(MainActivity.DOCTOR)) {
            //url = "http://192.168.13.2/my_doctor/checkUserAsDoctor.php";
            url = "http://www.mydoctorbd.cf/checkUserAsDoctor.php";
        } else {
            url = "";
        }

        Log.i("okhttp", "called");

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("submit", "submit")
                .add("username", username)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.i("okhttp", "failed");
                Log.i("okhttp", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    final String jsonData = response.body().string();
                    Log.i("okhttp", jsonData);

                    if (jsonData.equals("not found")) {
                        showProgress(false);
                        warringAlert(getString(R.string.username_dont_match));
                    } else if (CURRENT_USER.equals(MainActivity.DOCTOR)) {

                        try {
                            JSONObject object = new JSONObject(jsonData);
                            JSONObject doctorObject = object.getJSONObject("doctor");

                            final String password = doctorObject.getString("password");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(LoginActivity.this, "username: " + username
                                            //+ "pass: " + password, Toast.LENGTH_SHORT).show();

                                    if (password.equals(mPasswordView.getText().toString())) {
                                        showProgress(false);
                                        Intent intent = new Intent(LoginActivity.this, DoctorHomePage.class);
                                        intent.putExtra("doctorJsonData", jsonData);
                                        //Toast.makeText(LoginActivity.this, jsonData, Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                    } else {
                                        showProgress(false);
                                        warringAlert(getString(R.string.username_dont_match));
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            Log.e("okhttp", e.getMessage());
                        }
                    } else if (CURRENT_USER.equals(MainActivity.PATIENT)) {
                    }

                } else {

                    Log.i("okhttp", "unsuccessful");
                }
            }
        });

//        showProgress(false);
//        warringAlert("unsuccessful");
    }

    private void warringAlert(String alert){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("Ups!!")
                .setMessage(alert)
                .setPositiveButton("OK", null);

        Dialog dialog = builder.create();
        dialog.show();
    }
}

