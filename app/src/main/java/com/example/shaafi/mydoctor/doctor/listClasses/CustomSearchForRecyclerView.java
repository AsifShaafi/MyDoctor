package com.example.shaafi.mydoctor.doctor.listClasses;

import android.widget.Filter;

import com.example.shaafi.mydoctor.doctor.PatientDetailsForDoctorList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asif Imtiaz Shaafi on 9/17/2016.
 * Email: a15shaafi.209@gmail.com
 */
public class CustomSearchForRecyclerView extends Filter {

    List<PatientDetailsForDoctorList> mainList;
    PatientListAdapter adapter;

    public CustomSearchForRecyclerView(List<PatientDetailsForDoctorList> mainList, PatientListAdapter adapter) {
        this.mainList = mainList;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults results = new FilterResults();

        //filtered list
        List<PatientDetailsForDoctorList> filteredList = new ArrayList<>();

        if (constraint != null) {
            String filterString = constraint.toString().toLowerCase();

            for (int i = 0; i < mainList.size(); i++) {
                if (mainList.get(i).getName().toLowerCase().startsWith(filterString)) {
                    filteredList.add(mainList.get(i));
                }
            }

            //updating the result with the filtered list
            results.count = filteredList.size();
            results.values = filteredList;
        } else {

            //if nothing is filtered then set the results to initial value
            results.count = mainList.size();
            results.values = mainList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        //update the adapter with modified list and notify the adapter about it
        adapter.mPatientArrayList = (List<PatientDetailsForDoctorList>) results.values;
        adapter.notifyDataSetChanged();
    }
}
