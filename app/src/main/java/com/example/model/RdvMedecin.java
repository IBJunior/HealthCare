package com.example.model;

public class RdvMedecin {

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
}
