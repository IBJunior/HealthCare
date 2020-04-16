package com.example.model;

public class Medecin {

    private  String nom;
    private String email;
    private String specialite;
    private String addresse;
    private String tel;

    public Medecin(String nom, String email, String specialite, String addresse, String tel) {
        this.nom = nom;
        this.email = email;
        this.specialite = specialite;
        this.addresse = addresse;
        this.tel = tel;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getAddresse() {
        return addresse;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
