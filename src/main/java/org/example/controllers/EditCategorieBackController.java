package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.models.categorie;
import org.example.services.categorieservice;

public class EditCategorieBackController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField descriptionField;

    private categorie currentCategorie;

    // Setter utilisé pour pré-remplir les champs lors du chargement de la catégorie sélectionnée
    public void setCategorieData(categorie c) {
        this.currentCategorie = c;
        nomField.setText(c.getNom());
        descriptionField.setText(c.getDescription());
    }

    @FXML
    private void saveChanges() {
        try {
            currentCategorie.setNom(nomField.getText());
            currentCategorie.setDescription(descriptionField.getText());

            categorieservice service = new categorieservice();
            service.updatecategorie(currentCategorie);

            // Fermer la fenêtre
            ((Stage) nomField.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Erreur", "Une erreur est survenue lors de la modification de la catégorie.");
        }
    }

    @FXML
    private void cancel() {
        ((Stage) nomField.getScene().getWindow()).close();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
