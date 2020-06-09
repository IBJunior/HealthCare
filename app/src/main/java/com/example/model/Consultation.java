package com.example.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Consultation implements Parcelable {

    String issue;
    String NomPatient;
    String NomMedecin;
    String date;


    public Consultation(String issue, String nomPatient, String nomMedecin,String date) {
        this.issue = issue;
        NomPatient = nomPatient;
        NomMedecin = nomMedecin;
        this.date = date;
    }


    public Consultation() {
    }

    protected Consultation(Parcel in) {
        issue = in.readString();
        NomPatient = in.readString();
        NomMedecin = in.readString();
        date = in.readString();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static final Creator<Consultation> CREATOR = new Creator<Consultation>() {
        @Override
        public Consultation createFromParcel(Parcel in) {
            return new Consultation(in);
        }

        @Override
        public Consultation[] newArray(int size) {
            return new Consultation[size];
        }
    };

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
        dest.writeString(issue);
        dest.writeString(NomPatient);
        dest.writeString(NomMedecin);
        dest.writeString(date);
    }
}
