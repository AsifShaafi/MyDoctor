package com.example.shaafi.mydoctor.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Asif Imtiaz Shaafi, on 03-Jul-16.
 * Email: a15shaafi.209@gmail.com
 */
public class NetworkConnection {

    /*
            A class for the necessary common methods for networking that we will need almost
            every class in which network is need. All the methods are static so that we can
            use them whenever we need them without writing editional code in each class
     */

    /*
            main website link
     */
//    public static final String mainUrl = "http://www.mydoctorbd.cf/";

    /*
            testing link,,database in localhost
     */
    public static final String mainUrl = "http://10.0.2.2/my_doctor/";

    /*
        Checks if the the user is connected to the internet/network or not.
        If connected returns true
     */
    public static boolean hasNetworkConnection(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    /*
        method that will take the url and the form body,process the request and return the result of
        the request as a string variable
     */
    public static String getJsonResultFromServer(String path, RequestBody requestBody) {
        String url = mainUrl + path;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();


        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("json", "getJsonResultFromServer: " + e.getLocalizedMessage());
            return null;
        }
    }

    /*
        A static class that will take the url and the request_form_body and make a object with them
        so that we can easily pass and handle it
     */
    public static class RequestPackage {
        String url;
        RequestBody mRequestBody;

        public RequestPackage(String url, RequestBody requestBody) {
            this.url = url;
            mRequestBody = requestBody;
        }

        public String getUrl() {
            return url;
        }

        public RequestBody getRequestBody() {
            return mRequestBody;
        }
    }
}
