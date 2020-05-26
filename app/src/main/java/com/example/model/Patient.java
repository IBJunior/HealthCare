package com.example.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Patient implements Parcelable {


        private  String nom;
        private String prenom;
        private String date_naissance;
        private String situation_familiale;
        private String  email;
        private String tel;
        private  String adresse;

        public Patient(String nom,String prenom, String date_naissance,String situation_familiale, String email, String tel, String adresse) {
            this.nom = nom;
            this.prenom = prenom;
            this.date_naissance = date_naissance;
            this.situation_familiale = situation_familiale;
            this.email = email;
            this.tel = tel;
            this.adresse = adresse;

        }
    public Patient(){

    }

    protected Patient(Parcel in) {
        nom = in.readString();
        prenom = in.readString();
        situation_familiale = in.readString();
        email = in.readString();
        tel = in.readString();
        adresse = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeString(prenom);
        dest.writeString(situation_familiale);
        dest.writeString(email);
        dest.writeString(tel);
        dest.writeString(adresse);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Patient> CREATOR = new Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String  getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance(String  date_naissance) {
        this.date_naissance = date_naissance;
    }

    public String getSituation_familiale() {
        return situation_familiale;
    }

    public void setSituation_familiale(String situation_familiale) {
        this.situation_familiale = situation_familiale;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}


