package models;

import java.time.LocalDate;

public class evenement {
     private int id,recomponse;
     private String titre;
     private String description;
     private String localisation;
     private String contact;
    private LocalDate date_d;

    public evenement(String trim, String trimmed, String contact, String localisation, String format, int recomponse){}
    public evenement( String titre, String description , String contact, String localisation, LocalDate date_d,int recomponse) {
        this.titre = titre;
        this.description = description;
        this.contact = contact;
        this.localisation = localisation;
        this.date_d = date_d;
        this.recomponse = recomponse;
    }

    public evenement(String titre, String description , String contact, String localisation, LocalDate date_d,int recomponse ,int id ) {
        this.titre = titre;
        this.description = description;
        this.contact = contact;
        this.localisation = localisation;
        this.date_d = date_d;
        this.recomponse = recomponse;
        this.id = id;
    }


    public LocalDate getDate_d() {
        return date_d;
    }

    public void setDate_d(LocalDate date_d) {
        this.date_d = date_d;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecomponse() {
        return recomponse;
    }

    public void setRecomponse(int recomponse) {
        this.recomponse = recomponse;
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

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }

    }