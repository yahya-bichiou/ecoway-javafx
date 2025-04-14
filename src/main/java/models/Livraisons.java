package models;

import java.time.LocalDate;

public class Livraisons {

    private int id,commande_id;
    private String livreur, adresse, status, mode;
    private float prix;
    private LocalDate date;

    public Livraisons() {}

    public Livraisons(String livreur, String adresse, String status, String mode, float prix, LocalDate date, int commande_id) {
        this.livreur = livreur;
        this.adresse = adresse;
        this.status = status;
        this.mode = mode;
        this.prix = prix;
        this.date = date;
        this.commande_id = commande_id;
    }

    public Livraisons(int id, String livreur, String adresse, String status, String mode, float prix, LocalDate date, int commande_id) {
        this.id = id;
        this.livreur = livreur;
        this.adresse = adresse;
        this.status = status;
        this.mode = mode;
        this.prix = prix;
        this.date = date;
        this.commande_id = commande_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLivreur() {
        return livreur;
    }

    public void setLivreur(String livreur) {
        this.livreur = livreur;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getCommande_id() {
        return commande_id;
    }

    public void setCommande_id(int commande_id) {
        this.commande_id = commande_id;
    }
}
