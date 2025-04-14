package models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Commandes {

    private int id, client_id;
    private String status, mode_paiement, produits;
    private LocalDate date;
    private float prix;

    public Commandes() {}

    public Commandes(int client_id, String status, String mode_paiement, LocalDate date, float prix, String produits) {
        this.client_id = client_id;
        this.status = status;
        this.mode_paiement = mode_paiement;
        this.date = date;
        this.prix = prix;
        this.produits = produits;
    }

    public Commandes(int id, int client_id, String status, String mode_paiement, LocalDate date, float prix, String produits) {
        this.id = id;
        this.client_id = client_id;
        this.status = status;
        this.mode_paiement = mode_paiement;
        this.date = date;
        this.prix = prix;
        this.produits = produits;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getMode_paiement() {
        return mode_paiement;
    }

    public void setMode_paiement(String mode_paiement) {
        this.mode_paiement = mode_paiement;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getProduits() {
        return produits;
    }

    public void setProduits(String produits) {
        this.produits = produits;
    }
}
