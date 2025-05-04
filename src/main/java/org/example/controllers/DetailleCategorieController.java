package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.models.categorie;

import java.io.IOException;

public class DetailleCategorieController {

    @FXML
    private TextField nomTextField;

    @FXML
    private TextField descriptionTextField;

    private categorie currentCategorie; // Pour stocker la catégorie courante

    // Initialisation des données dans les champs
    public void initData(categorie cat) {
        this.currentCategorie = cat;
        nomTextField.setText(cat.getNom());
        descriptionTextField.setText(cat.getDescription());
    }

    @FXML
    private void handleBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/listCategorieBack.fxml"));
            Stage stage = (Stage) nomTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du retour : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Getters (optionnel, si besoin ailleurs)
    public TextField getNomTextField() {
        return nomTextField;
    }

    public TextField getDescriptionTextField() {
        return descriptionTextField;
    }
}
