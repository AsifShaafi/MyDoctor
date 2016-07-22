package com.example.shaafi.mydoctor.doctor;

/**
 * Created by Shaafi on 10-Jul-16.
 */
public class PatientDetailsForDoctorList {

    /*
            A class that holds the patient details for each patient in a row in
             the patient list of the doctor
     */

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
}
