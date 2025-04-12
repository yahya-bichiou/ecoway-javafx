package models;

import java.time.LocalDateTime;

public class Livraisons {

    private int id,commandeId;
    private String livreur, adresse, status, mode;
    private float prix;
    private LocalDateTime date;

    public Livraisons() {}

    public Livraisons(String livreur, String adresse, String status, String mode, float prix, LocalDateTime date, int commandeId) {
        this.livreur = livreur;
        this.adresse = adresse;
        this.status = status;
        this.mode = mode;
        this.prix = prix;
        this.date = date;
        this.commandeId = commandeId;
    }

    public Livraisons(int id, String livreur, String adresse, String status, String mode, float prix, LocalDateTime date, int commandeId) {
        this.id = id;
        this.livreur = livreur;
        this.adresse = adresse;
        this.status = status;
        this.mode = mode;
        this.prix = prix;
        this.date = date;
        this.commandeId = commandeId;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(int commandeId) {
        this.commandeId = commandeId;
    }
}
