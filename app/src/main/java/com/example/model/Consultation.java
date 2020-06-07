package com.example.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Consultation implements Parcelable {

    String issue;
    String NomPatient;
    String NomMedecin;

    public Consultation(String issue, String nomPatient, String nomMedecin) {
        this.issue = issue;
        NomPatient = nomPatient;
        NomMedecin = nomMedecin;
    }

    public Consultation() {
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getNomPatient() {
        return NomPatient;
    }

    public void setNomPatient(String nomPatient) {
        NomPatient = nomPatient;
    }

    public String getNomMedecin() {
        return NomMedecin;
    }

    public void setNomMedecin(String nomMedecin) {
        NomMedecin = nomMedecin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
