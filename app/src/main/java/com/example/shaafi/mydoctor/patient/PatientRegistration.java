package com.example.shaafi.mydoctor.patient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shaafi.mydoctor.R;
import com.example.shaafi.mydoctor.utilities.ImageHandler;
import com.example.shaafi.mydoctor.utilities.NetworkConnection;

import java.io.ByteArrayOutputStream;
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

public class PatientRegistration extends AppCompatActivity {

    private Bitmap bitmap;
    private String image_encoded_string;

    @BindView(R.id.patient_name)
    EditText mPatientName;
    @BindView(R.id.patient_username)
    EditText mPatientUsername;
    @BindView(R.id.patient_age)
    EditText mPatientAge;
    @BindView(R.id.patient_reg_progressBar)
    ProgressBar mProgress;
    @BindView(R.id.uploadImageView)
    ImageView mUplodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);
        ButterKnife.bind(this);
        mProgress.setVisibility(View.GONE);
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
            warringAlert(PatientRegistration.this, "please check your internet connection");
        }
        else {
            warringAlert(PatientRegistration.this, "please fill all fields");
        }
    }

    /*
        pick image from gallery
     */
    public void pickImageForUpload(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, ImageHandler.REQUEST_CODE);
    }

    /*
        set image to the imageview and get it ready for uploading by converting it to a encoded string
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageHandler.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri image = data.getData();
            mUplodedImage.setImageURI(image);
            bitmap = ((BitmapDrawable) mUplodedImage.getDrawable()).getBitmap();

            //starting to convert the image into encode image
            EncodeAndUploadImage encodeAndUploadImage = new EncodeAndUploadImage();
            encodeAndUploadImage.execute();
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
                .add("image", image_encoded_string)
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
                        warringAlert(PatientRegistration.this, "Registration failed");
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
                                successAlert(PatientRegistration.this, "Registration Successful, Please login to continue!");

                            } else if (result.equals("found")){
                                mProgress.setVisibility(View.GONE);
                                warringAlert(PatientRegistration.this, "Sorry username already taken, use another one");
                            }
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgress.setVisibility(View.GONE);
                            warringAlert(PatientRegistration.this, "There was some problem, please try again later");
                        }
                    });
                }
            }
        });
    }

    /*
        a async class to convert the image into a encoded string
     */
    private class EncodeAndUploadImage extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("upload_image", "onPreExecute: process started");

            Toast.makeText(getBaseContext(), "started", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            image_encoded_string = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            Log.i("upload_image", "onPostExecute: process end::: " + image_encoded_string);
            Toast.makeText(getBaseContext(), "ended encoding", Toast.LENGTH_SHORT).show();
        }
    }
}
