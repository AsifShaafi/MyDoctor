package com.example.shaafi.mydoctor.doctor.listClasses;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shaafi.mydoctor.R;
import com.example.shaafi.mydoctor.doctor.PatientDetailsForDoctorList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shaafi on 10-Jul-16.
 */
public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.ViewHolder> {

    PatientDetailsForDoctorList[] mPatientList;
    Context mContext;

    public PatientListAdapter(Context context, PatientDetailsForDoctorList[] patientList) {
        mContext = context;
        mPatientList = patientList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_list_row_view, parent, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.bindViews(mPatientList[position]);
    }

    @Override
    public int getItemCount() {
        return mPatientList.length;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.lsitPatientNameTextView)
        TextView mPatientName;
        @BindView(R.id.listPatientUserIdTextView)
        TextView mPatientUserId;
        @BindView(R.id.listPatientAgeTextView)
        TextView mPatientAge;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        protected void bindViews(PatientDetailsForDoctorList patient){
            mPatientName.setText(patient.getName());
            mPatientUserId.setText(patient.getUserID());
            mPatientAge.setText(patient.getAge());
        }

        @Override
        public void onClick(View v) {

        }
    }
}
