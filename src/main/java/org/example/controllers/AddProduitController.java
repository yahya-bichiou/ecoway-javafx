package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.example.models.categorie;
import org.example.models.produit;
import org.example.services.categorieservice;
import org.example.services.produitservice;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public class AddProduitController {

    @FXML private TextField nomTextField;
    @FXML private TextField descriptionTextField;
    @FXML private ComboBox<String> qualiteComboBox;
    @FXML private TextField quantiteTextField;
    @FXML private TextField prixTextField;
    @FXML private DatePicker dateAjoutDatePicker;
    @FXML private TextField imageTextField;
    @FXML private ComboBox<categorie> categorieComboBox;
    @FXML private Button addButton;

    private final produitservice produitService;
    private final categorieservice categorieService;

    public AddProduitController() throws SQLException {
        produitService = new produitservice();
        categorieService = new categorieservice();
    }

    @FXML
    public void initialize() {
        qualiteComboBox.getItems().addAll("Standard", "Premium", "Bio");
        try {
            List<categorie> categories = categorieService.getAll();
            categorieComboBox.getItems().addAll(categories);
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les catégories : " + e.getMessage());
        }
    }

    @FXML
    private void handleAdd() {
        if (!validateFields()) return;

        produit prod = new produit(
                categorieComboBox.getValue().getId(),
                nomTextField.getText().trim(),
                descriptionTextField.getText().trim(),
                qualiteComboBox.getValue(),
                Integer.parseInt(quantiteTextField.getText().trim()),
                Double.parseDouble(prixTextField.getText().trim()),
                dateAjoutDatePicker.getValue().atStartOfDay(),
                imageTextField.getText().trim()
        );

        try {
            produitService.addproduit(prod);
            clearFields();
            showSuccessAlert("Produit ajouté avec succès !");
        } catch (Exception e) {
            showAlert("Erreur SQL", e.getMessage());
        }
    }

    @FXML
    private void choisirImageAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imageTextField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void handleClear() {
        clearFields();
    }

    private void clearFields() {
        categorieComboBox.setValue(null);
        nomTextField.clear();
        descriptionTextField.clear();
        qualiteComboBox.setValue(null);
        quantiteTextField.clear();
        prixTextField.clear();
        dateAjoutDatePicker.setValue(null);
        imageTextField.clear();

    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        if (nomTextField.getText().trim().isEmpty()) errors.append("- Le nom est requis.\n");
        if (descriptionTextField.getText().trim().isEmpty()) errors.append("- La description est requise.\n");
        if (qualiteComboBox.getValue() == null) errors.append("- La qualité est requise.\n");
        if (categorieComboBox.getValue() == null) errors.append("- La catégorie est requise.\n");
        if (dateAjoutDatePicker.getValue() == null) errors.append("- La date d'ajout est requise.\n");

        try {
            Integer.parseInt(quantiteTextField.getText().trim());
        } catch (NumberFormatException e) {
            errors.append("- Quantité invalide.\n");
        }

        try {
            Double.parseDouble(prixTextField.getText().trim());
        } catch (NumberFormatException e) {
            errors.append("- Prix invalide.\n");
        }

        if (errors.length() > 0) {
            showAlert("Erreur de validation", errors.toString());
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
