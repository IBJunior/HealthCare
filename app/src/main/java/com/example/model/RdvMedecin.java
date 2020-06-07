package com.example.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RdvMedecin implements Parcelable {

    String heure;
    String date;
    Patient patient;

    public RdvMedecin(String heure, String date, Patient patient) {
        this.heure = heure;
        this.date = date;
        this.patient = patient;
    }
    public RdvMedecin() {
        this.heure = "heure";
        this.date = "date";
        Patient patient_ = new Patient();
        patient_.setNom("");
        patient_.setEmail("justine@gmail.com");
        this.patient = patient_;


    }

    protected RdvMedecin(Parcel in) {
        heure = in.readString();
        date = in.readString();
        patient = in.readParcelable(Patient.class.getClassLoader());
    }

    public static final Creator<RdvMedecin> CREATOR = new Creator<RdvMedecin>() {
        @Override
        public RdvMedecin createFromParcel(Parcel in) {
            return new RdvMedecin(in);
        }

        @Override
        public RdvMedecin[] newArray(int size) {
            return new RdvMedecin[size];
        }
    };

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(heure);
        dest.writeString(date);
        dest.writeParcelable(patient, flags);
    }
}
