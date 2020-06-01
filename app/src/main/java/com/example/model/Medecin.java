package com.example.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Medecin implements Parcelable {

    private  String nom;
    private String prenom;
    private String specialite;
    private String adresse;
    private String tel;
    private String email;
    private String photoUrl;


    public Medecin(String nom, String prenom, String specialite, String adresse, String tel, String mail) {
        this.nom = nom;
     this.prenom  = prenom;
        this.email = mail;
        this.specialite = specialite;
        this.adresse = adresse;
        this.tel = tel;
    }
    public Medecin(){

    }

    protected Medecin(Parcel in) {
        nom = in.readString();
        prenom = in.readString();
        specialite = in.readString();
        adresse = in.readString();
        tel = in.readString();
        email = in.readString();
    }

    public static final Creator<Medecin> CREATOR = new Creator<Medecin>() {
        @Override
        public Medecin createFromParcel(Parcel in) {
            return new Medecin(in);
        }

        @Override
        public Medecin[] newArray(int size) {
            return new Medecin[size];
        }
    };

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String addresse) {
        this.adresse = addresse;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public  String toString(){
        return " Adresse" + adresse;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeString(prenom);
        dest.writeString(specialite);
        dest.writeString(adresse);
        dest.writeString(tel);
        dest.writeString(email);
    }


    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
