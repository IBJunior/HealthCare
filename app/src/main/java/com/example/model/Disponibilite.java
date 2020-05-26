package com.example.model;

public class Disponibilite {

    String heure1;
    String heure2;

    public Disponibilite(String heure1, String heure2) {
        this.heure1 = heure1;
        this.heure2 = heure2;
    }
    public Disponibilite() {
        this.heure1 = "";
        this.heure2 = "";
    }

    public String getHeure1() {
        return heure1;
    }

    public void setHeure1(String heure1) {
        this.heure1 = heure1;
    }

    public String getHeure2() {
        return heure2;
    }

    public void setHeure2(String heure2) {
        this.heure2 = heure2;
    }
}
