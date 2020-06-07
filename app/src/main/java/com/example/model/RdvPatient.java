package com.example.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RdvPatient implements Parcelable {

    String date;
    String heure;
    Medecin medecin;

    public RdvPatient(String date, String heure, Medecin medecin) {
        this.date = date;
        this.heure = heure;
        this.medecin = medecin;
    }
    public RdvPatient() {
        this.date = "date";
        this.heure = "heure";
        Medecin medecin = new Medecin();
        medecin.setEmail("ali@gmail.com");
        this.medecin = medecin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
