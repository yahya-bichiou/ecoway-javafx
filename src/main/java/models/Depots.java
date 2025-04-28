package models;

public class Depots {
    int id, capacite;
    String nom, adresse, image;

    public Depots(int id, int capacite, String nom, String adresse, String image) {
        this.id = id;
        this.capacite = capacite;
        this.nom = nom;
        this.adresse = adresse;
        this.image = image;
    }

    public Depots(int capacite, String nom, String adresse, String image) {
        this.capacite = capacite;
        this.nom = nom;
        this.adresse = adresse;
        this.image = image;
    }

    public Depots() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
