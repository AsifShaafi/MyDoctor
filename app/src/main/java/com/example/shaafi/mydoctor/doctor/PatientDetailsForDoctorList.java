package com.example.shaafi.mydoctor.doctor;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Shaafi on 10-Jul-16.
 */
public class PatientDetailsForDoctorList implements Parcelable {

    private String name;
    private String userID;
    private String age;

    public PatientDetailsForDoctorList() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    private PatientDetailsForDoctorList(Parcel parcel){

        name = parcel.readString();
        userID = parcel.readString();
        age = parcel.readString();
    }

    public static final Creator<PatientDetailsForDoctorList> CREATOR = new Creator<PatientDetailsForDoctorList>() {
        @Override
        public PatientDetailsForDoctorList createFromParcel(Parcel in) {
            return new PatientDetailsForDoctorList(in);
        }

        @Override
        public PatientDetailsForDoctorList[] newArray(int size) {
            return new PatientDetailsForDoctorList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(userID);
        dest.writeString(age);
    }
}
