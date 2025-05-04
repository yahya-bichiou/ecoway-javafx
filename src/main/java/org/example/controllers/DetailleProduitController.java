package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.models.categorie;
import org.example.models.produit;
import org.example.services.categorieservice;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DetailleProduitController {

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
    @FXML
    private ComboBox<categorie> categorieComboBox;

    private produit currentProduit;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    public void initialize() {
        qualiteComboBox.getItems().addAll("Standard", "Premium", "Luxe");
        dateAjoutTextField.setDisable(true);

        // Charger les catégories depuis la base de données
        categorieservice categorieService = new categorieservice();
        List<categorie> categories = categorieService.getAll(); // suppose que tu as une méthode getAll()
        categorieComboBox.getItems().addAll(categories);
    }

    public void initData(produit produit) {
        this.currentProduit = produit;
        nomTextField.setText(produit.getNom());
        descriptionTextField.setText(produit.getDescription());
        qualiteComboBox.setValue(produit.getQualite());
        quantiteTextField.setText(String.valueOf(produit.getQuantiteDisponible()));
        prixTextField.setText(String.valueOf(produit.getPrix()));

        if (produit.getDateAjout() != null) {
            dateAjoutTextField.setText(produit.getDateAjout().format(dateFormatter));
        }

        imageTextField.setText(produit.getImage());

       /* if (produit.getCategorie() != null) {
            categorieComboBox.setValue(produit.getCategorie());
        }*/
    }

    @FXML
    private void handleBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListeProduitBack.fxml"));
            Stage stage = (Stage) nomTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du retour : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
