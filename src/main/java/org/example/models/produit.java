package org.example.models;

import java.time.LocalDateTime;

public class produit {
    private int id;
    private String nom;
    private String description;
    private String qualite;
    private int quantite_disponible;
    private double prix;
    private LocalDateTime dateAjout;
    private String image;
    private int categorieId; // Ajout du champ pour la catégorie

    // Constructeur sans ID (ex: pour insertion)
    public produit(String nom, String description, String qualite, int quantiteDisponible,
                   double prix, LocalDateTime dateAjout, String image, int categorieId) {
        this.nom = nom;
        this.description = description;
        this.qualite = qualite;
        this.quantite_disponible = quantiteDisponible;
        this.prix = prix;
        this.dateAjout = dateAjout;
        this.image = image;
        this.categorieId = categorieId;
    }

    // Constructeur avec ID
    public produit(int id, String nom, String description, String qualite, int quantiteDisponible,
                   double prix, LocalDateTime dateAjout, String image, int categorieId) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.qualite = qualite;
        this.quantite_disponible = quantiteDisponible;
        this.prix = prix;
        this.dateAjout = dateAjout;
        this.image = image;
        this.categorieId = categorieId;
    }

    public produit() {

    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQualite() {
        return qualite;
    }

    public void setQualite(String qualite) {
        this.qualite = qualite;
    }

    public int getQuantiteDisponible() {
        return quantite_disponible;
    }

    public void setQuantiteDisponible(int quantiteDisponible) {
        this.quantite_disponible = quantiteDisponible;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public LocalDateTime getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(LocalDateTime dateAjout) {
        this.dateAjout = dateAjout;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(int categorieId) {
        this.categorieId = categorieId;
    }
}