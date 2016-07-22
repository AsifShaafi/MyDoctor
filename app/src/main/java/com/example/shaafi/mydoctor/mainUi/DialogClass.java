package com.example.shaafi.mydoctor.mainUi;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.shaafi.mydoctor.R;
import com.example.shaafi.mydoctor.doctor.DoctorRegistration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shaafi on 03-Jul-16.
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
                        DoctorRegistration.SECTOR_LIST = list.toString();
                    }
                });

        Dialog dialog = builder.create();
        dialog.show();

    }

    /*
        a dialog for the patient so that he/she can add a doctor in the list and give him the
        permission to see the patient details
     */
    public static void addDoctorDialog(Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Add A Doctor");
    }

}
