package org.example.models;

public class categorie {
        private int id;
        private String nom;
        private String description;

        // Constructeur par défaut
        public categorie() {}

        // Constructeur sans ID
        public categorie(String nom, String description) {
            this.nom = nom;
            this.description = description;
        }

        // Constructeur avec ID
        public categorie(int id, String nom, String description) {
            this.id = id;
            this.nom = nom;
            this.description = description;
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
    }


