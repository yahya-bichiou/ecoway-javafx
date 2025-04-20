package org.example.models;

public class post {
    private int id;
    private String titre;
    private String description;
    private String createur;
    private String image;

    public post() {}

    public post(String titre, String description, String createur, String image) {
        this.titre = titre;
        this.description = description;
        this.createur = createur;
        this.image = image;
    }

    public post(int id, String titre, String description, String createur, String image) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.createur = createur;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateur() {
        return createur;
    }

    public void setCreateur(String createur) {
        this.createur = createur;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // ✅ Affichage simplifié pour ComboBox (titre uniquement)
    @Override
    public String toString() {
        return titre;
    }
}
