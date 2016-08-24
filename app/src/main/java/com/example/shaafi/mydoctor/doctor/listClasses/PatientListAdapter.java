package com.example.shaafi.mydoctor.doctor.listClasses;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.shaafi.mydoctor.R;
import com.example.shaafi.mydoctor.doctor.PatientDetailsForDoctorList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Asif Imiaz Shaafi, on 10-Jul-16.
 * Email: a15shaafi.209@gmail.com
 */
public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.ViewHolder> {

    PatientDetailsForDoctorList[] mPatientList;
    Context mContext;
    ListActivity mListActivity;

    public PatientListAdapter(Context context,ListActivity activity, PatientDetailsForDoctorList[] patientList) {
        mContext = context;
        mPatientList = patientList;
        mListActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_list_row_view, parent, false);

        ViewHolder holder = new ViewHolder(mListActivity,view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.bindViews(mPatientList[position]);

        if (ListActivity.contextMoodOn) {
            holder.mPatientCheckBox.setVisibility(View.VISIBLE);
            holder.mPatientCheckBox.setChecked(false);
        } else {
            holder.mPatientCheckBox.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mPatientList.length;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        @BindView(R.id.lsitPatientNameTextView)
        TextView mPatientName;
        @BindView(R.id.listPatientUserIdTextView)
        TextView mPatientUserId;
        @BindView(R.id.listPatientAgeTextView)
        TextView mPatientAge;
        @BindView(R.id.patientLsitCheckBox)
        CheckBox mPatientCheckBox;
        @BindView(R.id.patientListCardView)
        CardView mPatientCardView;

        public ViewHolder(ListActivity activity, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            if (ListActivity.contextMoodOn) {
                mPatientCheckBox.setVisibility(View.VISIBLE);
            }
            else {
                mPatientCheckBox.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(this);
            mPatientCardView.setOnLongClickListener(activity);
            mPatientCheckBox.setOnClickListener(this);
        }

        protected void bindViews(PatientDetailsForDoctorList patient){
            mPatientName.setText(patient.getName());
            mPatientUserId.setText(patient.getUserID());
            mPatientAge.setText(patient.getAge());
        }

        @Override
        public void onClick(View v) {
            if (ListActivity.contextMoodOn && v.getId() == R.id.patientLsitCheckBox) {
                mListActivity.contextModeWork(v, getAdapterPosition());
            }
        }
    }
}
