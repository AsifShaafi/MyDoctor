package com.example.shaafi.mydoctor.mainUi;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Shaafi on 03-Jul-16.
 */
public class AlertUser {

    public static void warringAlert(Context context, String alert) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle("Ups!!")
                .setMessage(alert)
                .setPositiveButton("OK", null);

        Dialog dialog = builder.create();
        dialog.show();
    }
}
