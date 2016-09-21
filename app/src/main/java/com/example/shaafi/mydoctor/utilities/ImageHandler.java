package com.example.shaafi.mydoctor.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Asif Imtiaz Shaafi, on 8/25/2016.
 * Email: a15shaafi.209@gmail.com
 */
public class ImageHandler {

    /*
        a helper class to help the app to handle the images used in the app
     */

    public static final int REQUEST_CODE = 100;
    private static final String PHOTO_BASE_URI = "http://www.mydoctorbd.cf/images/";
    public static LruCache<String, Bitmap> imageCache;

    /*
        a static class that is used to get the patient's details to show the image of the patient list
     */
    public static class PatientNView {
        String mName;
        ProgressBar mProgressBar;
        ImageView mImageView;
        Bitmap bitmap;

        public PatientNView(String mName, ProgressBar mProgressBar, ImageView mImageView) {
            this.mName = mName;
            this.mProgressBar = mProgressBar;
            this.mImageView = mImageView;
        }
    }

    /*
        a async class that downloads the image of a patient from the server using the image name and
        then shows it in the image_view of the patient in patient list and also stores the image
        into a cache memory and in the patient's object for further use
     */
    public static class ImageLoader extends AsyncTask<PatientNView, Void, PatientNView> {

        //PatientNView container;
        ProgressBar progressBar;

        public ImageLoader(PatientNView container) {
            // this.container = container;
            progressBar = container.mProgressBar;
        }

        @Override
        protected void onPreExecute() {

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected PatientNView doInBackground(PatientNView... params) {

            PatientNView container = params[0];

            String userName = container.mName;

            try {

                String imageUrl = PHOTO_BASE_URI + userName + ".png";

                InputStream inputStream = (InputStream) new URL(imageUrl).getContent();

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                inputStream.close();

                container.bitmap = bitmap;

                return container;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(PatientNView mPatientNView) {

            progressBar.setVisibility(View.GONE);

            if (mPatientNView.bitmap != null) {
                mPatientNView.mImageView.setImageBitmap(mPatientNView.bitmap);

                //saving the image into the cache memory for future use
                imageCache.put(mPatientNView.mName,
                        mPatientNView.bitmap);
            }

        }
    }
}
