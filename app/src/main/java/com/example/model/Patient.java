package com.example.model;

import java.util.Date;

public class Patient {


        private  String nom;
        private String prenom;
        private Date date_naissance;
        private String situation_familiale;
        private String  email;
        private String tel;
        private  String adresse;

        public Patient(String nom,String prenom, Date date_naissance,String situation_familiale, String email, String tel, String adresse) {
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

    public Date getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance(Date date_naissance) {
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


