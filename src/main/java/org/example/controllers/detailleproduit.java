package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.models.produit;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class detailleproduit {

    @FXML
    private TextField nomTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private ComboBox<String> qualiteComboBox;
    @FXML
    private TextField quantiteTextField;
    @FXML
    private TextField prixTextField;
    @FXML
    private TextField dateAjoutTextField;
    @FXML
    private TextField imageTextField;

    private produit currentProduit; // Pour stocker le produit courant
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    public void initialize() {
        // Initialisation de la ComboBox
        qualiteComboBox.getItems().addAll("Standard", "Premium", "Luxe");

        // Configuration du format de date si nécessaire
        dateAjoutTextField.setDisable(true); // Empêche la modification de la date
    }

    // Méthode pour initialiser les données avec un objet produit
    public void initData(produit produit) {
        this.currentProduit = produit; // Stocker le produit

        nomTextField.setText(produit.getNom());
        descriptionTextField.setText(produit.getDescription());
        qualiteComboBox.setValue(produit.getQualite());
        quantiteTextField.setText(String.valueOf(produit.getQuantiteDisponible()));
        prixTextField.setText(String.valueOf(produit.getPrix()));

        if (produit.getDateAjout() != null) {
            dateAjoutTextField.setText(produit.getDateAjout().format(dateFormatter));
        }

        imageTextField.setText(produit.getImage());
    }

    // Méthode de retour
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListeProduitBack.fxml"));
            Stage stage = (Stage) imageTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du retour: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Getters et Setters
    public TextField getNomTextField() {
        return nomTextField;
    }

    public void setNomTextField(String nom) {
        this.nomTextField.setText(nom);
    }

    public TextField getDescriptionTextField() {
        return descriptionTextField;
    }

    public void setDescriptionTextField(String description) {
        this.descriptionTextField.setText(description);
    }

    public ComboBox<String> getQualiteComboBox() {
        return qualiteComboBox;
    }

    public void setQualiteComboBox(String qualite) {
        this.qualiteComboBox.setValue(qualite);
    }

    public TextField getQuantiteTextField() {
        return quantiteTextField;
    }

    public void setQuantiteTextField(String quantite) {
        this.quantiteTextField.setText(quantite);
    }

    public TextField getPrixTextField() {
        return prixTextField;
    }

    public void setPrixTextField(String prix) {
        this.prixTextField.setText(prix);
    }

    public TextField getDateAjoutTextField() {
        return dateAjoutTextField;
    }

    public void setDateAjoutTextField(String dateAjout) {
        this.dateAjoutTextField.setText(dateAjout);
    }

    public TextField getImageTextField() {
        return imageTextField;
    }

    public void setImageTextField(String image) {
        this.imageTextField.setText(image);
    }

    public produit getCurrentProduit() {
        return currentProduit;
    }

    public void setCurrentProduit(produit currentProduit) {
        this.currentProduit = currentProduit;
    }
}