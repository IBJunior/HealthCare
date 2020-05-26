package com.example.model;

public class RdvPatient {

    String date;
    String heure;
    Medecin medecin;

    public RdvPatient(String date, String heure, Medecin medecin) {
        this.date = date;
        this.heure = heure;
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
}
