package com.example.shaafi.mydoctor.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.shaafi.mydoctor.R;
import com.example.shaafi.mydoctor.doctor.DoctorRegistration;
import com.example.shaafi.mydoctor.patient.PatientHomePage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asif Imiaz Shaafi, on 03-Jul-16.
 * Email: a15shaafi.209@gmail.com
 */
public class DialogClass {

    /*
            a class for all dialogs in the app
    */

    /*
       a warring alert for user about the error.
    */
    public static void warringAlert(Context context, String alert) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle("Ups!!")
                .setMessage(alert)
                .setPositiveButton("OK", null);

        Dialog dialog = builder.create();
        dialog.show();
    }

    /*
        when the registration is successful show this success msg
     */
    public static void successAlert(final Activity activity, String alert) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .setTitle("Congrats!!")
                .setMessage(alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .setCancelable(false);

        Dialog dialog = builder.create();
        dialog.show();
    }


    /*
        a dialog fragment in alert dialog form where doctor can choose the sectors of his/her, select
        them and then the sectors are added in the sector list of doctor registration class
     */
    public static void doctorSectorListDialog(final Context context) {

        final StringBuffer list = new StringBuffer();
        final List<Integer> selectedList = new ArrayList<>();
        final String[] sectorList = context.getResources().getStringArray(R.array.doctor_sectors);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Select Sectors")
                .setMultiChoiceItems(sectorList, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        if (isChecked) {
                            selectedList.add(which);
                        } else {

                            if (selectedList.contains(which)) {
                                selectedList.remove(Integer.valueOf(which));
                            }
                        }
                    }
                })

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for (int i = 0; i < selectedList.size(); i++) {
                            if (i != 0)
                                list.append(", ");
                            list.append(sectorList[selectedList.get(i)]);
                        }
                        if (selectedList.size() == 0)
                        {
                            DialogClass.warringAlert(context, "No sector selected!\nPlease select a sector!");
                        }else{
                            DoctorRegistration.SECTOR_LIST = list.toString();
                        }
                    }
                });

        Dialog dialog = builder.create();
        dialog.show();

    }

    /*
            a dialog list for patient's problem where patient can find and select their problems
     */
    public static void dataListForPatient(final Context context) {

        final StringBuffer list = new StringBuffer();
        final List<Integer> selectedList = new ArrayList<>();
        final String[] dataList = context.getResources().getStringArray(R.array.patient_data);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Select Sectors")
                .setMultiChoiceItems(dataList, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        if (isChecked) {
                            selectedList.add(which);
                        } else {

                            if (selectedList.contains(which)) {
                                selectedList.remove(Integer.valueOf(which));
                            }
                        }
                    }
                })

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for (int i = 0; i < selectedList.size(); i++) {
                            if (i != 0)
                                list.append(", ");
                            list.append(dataList[selectedList.get(i)]);
                        }
                        PatientHomePage.PATIENT_DATA_LIST = list.toString();
                    }
                });

        Dialog dialog = builder.create();
        dialog.show();

    }

}
