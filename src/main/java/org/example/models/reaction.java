package org.example.models;

public class reaction {
    private int id;
    private int postId;
    private String type;
    private String utilisateur;

    // Constructeur avec id (pour les lectures depuis la base)
    public reaction(int id, int postId, String type, String utilisateur) {
        this.id = id;
        this.postId = postId;
        this.type = type;
        this.utilisateur = utilisateur;
    }

    // Constructeur sans id (pour insertion)
    public reaction(int postId, String type, String utilisateur) {
        this.postId = postId;
        this.type = type;
        this.utilisateur = utilisateur;
    }

    // GETTERS
    public int getId() {
        return id;
    }

    public int getPostId() {
        return postId;
    }

    public String getType() {
        return type;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    // SETTERS
    public void setId(int id) {
        this.id = id;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }
}
