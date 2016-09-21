package com.example.shaafi.mydoctor.doctor.listClasses;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaafi.mydoctor.R;
import com.example.shaafi.mydoctor.doctor.PatientDetailsForDoctorList;
import com.example.shaafi.mydoctor.patient.PatientHomeForDoctor;
import com.example.shaafi.mydoctor.utilities.ImageHandler;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Asif Imtiaz Shaafi, on 10-Jul-16.
 * Email: a15shaafi.209@gmail.com
 */
class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.ViewHolder> implements Filterable {

    private PatientDetailsForDoctorList[] mPatientList;
    List<PatientDetailsForDoctorList> mPatientArrayList;
    private ListActivity mListActivity;
    private CustomSearchForRecyclerView customSearchForRecyclerView;

    PatientListAdapter(ListActivity activity, PatientDetailsForDoctorList[] patientList) {
        mPatientList = patientList;
        mListActivity = activity;
        mPatientArrayList = Arrays.asList(mPatientList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_list_row_view, parent, false);

        return new ViewHolder(mListActivity, view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.bindViews(mPatientArrayList.get(position));

        if (ListActivity.contextMoodOn) {
            holder.mPatientCheckBox.setVisibility(View.VISIBLE);
            holder.mPatientCheckBox.setChecked(false);
        } else {
            holder.mPatientCheckBox.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mPatientArrayList.size();
    }

    @Override
    public Filter getFilter() {

        if (customSearchForRecyclerView == null) {
            customSearchForRecyclerView = new CustomSearchForRecyclerView(mPatientArrayList, this);
        }

        return customSearchForRecyclerView;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        @BindView(R.id.listPatientNameTextView)
        TextView mPatientName;
        @BindView(R.id.listPatientUserIdTextView)
        TextView mPatientUserId;
        @BindView(R.id.listPatientAgeTextView)
        TextView mPatientAge;
        @BindView(R.id.patientListCheckBox)
        CheckBox mPatientCheckBox;
        @BindView(R.id.patientListImage)
        ImageView mPatientImage;
        @BindView(R.id.patientListImageProgressBar)
        ProgressBar mPatientListImageProgressBar;
        @BindView(R.id.patientListCardView)
        CardView mPatientCardView;

        ViewHolder(ListActivity activity, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            if (ListActivity.contextMoodOn) {
                mPatientCheckBox.setVisibility(View.VISIBLE);
            } else {
                mPatientCheckBox.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(this);
            mPatientCardView.setOnLongClickListener(activity);
            mPatientCardView.setOnClickListener(this);
            mPatientCheckBox.setOnClickListener(this);
        }

        void bindViews(PatientDetailsForDoctorList patient) {
            mPatientName.setText(patient.getName());
            mPatientUserId.setText(patient.getUserID());
            mPatientAge.setText(patient.getAge());

            /*
                1st check if the image of the patient is stored in the cache memory
                if not then calls the image_loader class to get the image from server and set+save it
                and if found then set the image into the image view
             */

            Bitmap bitmap = ImageHandler.imageCache.get(patient.getUserID());

            if (bitmap != null) {
                mPatientImage.setImageBitmap(bitmap);
            } else {
                ImageHandler.PatientNView mPatientNView =
                        new ImageHandler.PatientNView(patient.getUserID(), mPatientListImageProgressBar, mPatientImage);
                ImageHandler.ImageLoader loader = new ImageHandler.ImageLoader(mPatientNView);
                loader.execute(mPatientNView);
            }
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mListActivity, "clicked", Toast.LENGTH_SHORT).show();
            if (ListActivity.contextMoodOn && v.getId() == R.id.patientListCheckBox) {
                mListActivity.contextModeWork(v, getAdapterPosition());
            } else if (v.getId() == R.id.patientListCardView) {
                Toast.makeText(mListActivity, "clicked2", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mListActivity, PatientHomeForDoctor.class);
                intent.putExtra("patient_clicked", mPatientList[getAdapterPosition()].getUserID());
                mListActivity.startActivity(intent);
            }
        }
    }
}
