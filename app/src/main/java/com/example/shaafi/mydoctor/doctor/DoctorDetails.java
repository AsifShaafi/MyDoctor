package com.example.shaafi.mydoctor.doctor;

import java.util.List;

/**
 * Created by Shaafi on 08-Jul-16.
 */
public class DoctorDetails {

    private int id;
    private String full_name;
    private String username;
    private String sectors;
    private PatientDetailsForDoctorList[] patientList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSectors() {
        return sectors;
    }

    public void setSectors(String sectors) {
        this.sectors = sectors;
    }

    public PatientDetailsForDoctorList[] getPatientList() {
        return patientList;
    }

    public void setPatientList(PatientDetailsForDoctorList[] patientList) {
        this.patientList = patientList;
    }
}
