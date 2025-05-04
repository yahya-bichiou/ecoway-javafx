package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.models.categorie;
import org.example.services.categorieservice;

import java.sql.SQLException;

public class AddCategorieController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField descriptionField;

    @FXML
    private Button addButton;

    private categorieservice service;

    public AddCategorieController() {
        try {
            service = new categorieservice();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'initialisation du service : " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        // Tu peux ajouter une logique ici si nécessaire
    }

    @FXML
    private void handleAdd() {
        if (!validateFields()) return;  // Validation des champs

        // Création de la nouvelle catégorie
        categorie cat = new categorie(
                nomField.getText().trim(),
                descriptionField.getText().trim()
        );

        try {
            service.addcategorie(cat);
            showSuccessAlert("Catégorie ajoutée avec succès !");

            // Fermer la fenêtre après ajout réussi
            ((Stage) nomField.getScene().getWindow()).close();

        } catch (Exception e) {
            showAlert("Erreur SQL", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClear() {
        // Fermer directement la fenêtre sans sauvegarde
        ((Stage) nomField.getScene().getWindow()).close();
    }

    private void fillFields(categorie c) {
        nomField.setText(c.getNom());
        descriptionField.setText(c.getDescription());
    }

    private void clearFields() {
        nomField.clear();
        descriptionField.clear();
    }

    private boolean validateFields() {
        StringBuilder errorMessage = new StringBuilder();

        if (nomField.getText().trim().isEmpty()) {
            errorMessage.append("- Le nom est requis.\n");
        }
        if (descriptionField.getText().trim().isEmpty()) {
            errorMessage.append("- La description est requise.\n");
        }

        if (errorMessage.length() > 0) {
            showAlert("Erreur de validation", errorMessage.toString());
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
