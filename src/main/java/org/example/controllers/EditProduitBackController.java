package org.example.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.models.produit;
import org.example.models.categorie;
import org.example.services.produitservice;
import org.example.services.categorieservice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class EditProduitBackController {

    @FXML private TextField nomField;
    @FXML private TextField descriptionArea;
    @FXML private ComboBox<String> qualiteComboBox;
    @FXML private ComboBox<categorie> categorieComboBox;
    @FXML private TextField prixField;
    @FXML private DatePicker dateAjoutPicker;
    @FXML private TextField imageTextField;

    private produit currentProduit;
    private List<categorie> allCategories;
    private final produitservice produitService = new produitservice();
    private final categorieservice categorieService = new categorieservice();

    @FXML
    public void initialize() {
        // Initialisation de la ComboBox qualité
        qualiteComboBox.setItems(FXCollections.observableArrayList(
                "Standard", "Premium", "Bio"
        ));

        // Chargement des catégories
        try {
            allCategories = categorieService.getAll();
            categorieComboBox.getItems().setAll(allCategories);
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les catégories: " + e.getMessage());
        }
    }

    public void setProduitData(produit prod) {
        this.currentProduit = prod;

        Platform.runLater(() -> {
            nomField.setText(prod.getNom());
            descriptionArea.setText(prod.getDescription());
            qualiteComboBox.setValue(prod.getQualite());
            prixField.setText(String.valueOf(prod.getPrix()));

            // Conversion LocalDateTime -> LocalDate
            if (prod.getDateAjout() != null) {
                dateAjoutPicker.setValue(prod.getDateAjout().toLocalDate());
            }

            // Sélection de la catégorie
            allCategories.stream()
                    .filter(cat -> cat.getId() == prod.getCategorieId())
                    .findFirst()
                    .ifPresent(categorieComboBox::setValue);

            imageTextField.setText(prod.getImage());
        });
    }

    @FXML
    private void handleAddImages() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imageTextField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void saveChanges() {
        try {
            // Validation des champs
            if (nomField.getText().isEmpty() || descriptionArea.getText().isEmpty() ||
                    qualiteComboBox.getValue() == null || categorieComboBox.getValue() == null ||
                    prixField.getText().isEmpty() || dateAjoutPicker.getValue() == null) {

                showAlert("Erreur", "Veuillez remplir tous les champs obligatoires");
                return;
            }

            // Mise à jour du produit
            currentProduit.setNom(nomField.getText());
            currentProduit.setDescription(descriptionArea.getText());
            currentProduit.setQualite(qualiteComboBox.getValue());
            currentProduit.setPrix(Double.parseDouble(prixField.getText()));
            currentProduit.setDateAjout(dateAjoutPicker.getValue().atStartOfDay());
            currentProduit.setCategorieId(categorieComboBox.getValue().getId());

            // Gestion de l'image
            if (!imageTextField.getText().isEmpty()) {
                currentProduit.setImage(imageTextField.getText());
            }

            // Sauvegarde
            produitService.updateproduit(currentProduit);

            // Fermeture de la fenêtre
            ((Stage) nomField.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la sauvegarde: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void cancel() {
        ((Stage) nomField.getScene().getWindow()).close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}