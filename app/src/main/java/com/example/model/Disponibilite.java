package com.example.model;

public class Disponibilite {

    String heure1;
    String heure2;
    String jour;



    public Disponibilite(String heure1, String heure2, String jour) {
        this.heure1 = heure1;
        this.heure2 = heure2;
        this.jour =jour;
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
    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }
}
