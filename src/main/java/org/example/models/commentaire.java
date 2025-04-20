package org.example.models;

public class commentaire {
    private int id;
    private String message;
    private String auteur;
    private int postId; // Utilisation de camelCase pour respecter la convention Java
    private int is_inappropriate; // Matching the database column name

    // Constructeurs
    public commentaire() {
        this.is_inappropriate = 0; // Default value
    }

    public commentaire(String message, String auteur, int postId) {
        this.message = message;
        this.auteur = auteur;
        this.postId = postId; // Utilisation de camelCase
        this.is_inappropriate = 0; // Default value
    }

    public commentaire(int id, String message, String auteur, int postId, int is_inappropriate) {
        this.id = id;
        this.message = message;
        this.auteur = auteur;
        this.postId = postId; // Utilisation de camelCase
        this.is_inappropriate = is_inappropriate;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public int getPostId() {
        return postId; // Le nom du getter correspond maintenant à la variable
    }

    public void setPostId(int postId) {
        this.postId = postId; // Le nom du setter correspond maintenant à la variable
    }

    public int getIsInappropriate() {
        return is_inappropriate;
    }

    public void setIsInappropriate(int is_inappropriate) {
        this.is_inappropriate = is_inappropriate;
    }

    // toString() method for debugging
    @Override
    public String toString() {
        return "Commentaire{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", auteur='" + auteur + '\'' +
                ", postId=" + postId + // Mise à jour de post_id en postId
                ", is_inappropriate=" + is_inappropriate +
                '}';
    }
}
