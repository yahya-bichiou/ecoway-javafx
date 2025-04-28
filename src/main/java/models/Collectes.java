package models;

import java.time.LocalDate;

public class Collectes {

    int id,depot_id;
    double quantite;
    LocalDate date;
    String responsable;

    public Collectes(int id, int depot_id, double quantite, LocalDate date, String responsable) {
        this.id = id;
        this.depot_id = depot_id;
        this.quantite = quantite;
        this.date = date;
        this.responsable = responsable;
    }

    public Collectes(int depot_id, double quantite, LocalDate date, String responsable) {
        this.depot_id = depot_id;
        this.quantite = quantite;
        this.date = date;
        this.responsable = responsable;
    }

    public Collectes() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDepot_id() {
        return depot_id;
    }

    public void setDepot_id(int depot_id) {
        this.depot_id = depot_id;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }
}
