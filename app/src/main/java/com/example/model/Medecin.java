package com.example.model;

public class Medecin {

    private  String nom;
    private String prenom;
    private String specialite;
    private String adresse;
    private String tel;
    private String email;

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
}
