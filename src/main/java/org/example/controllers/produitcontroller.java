package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.controllers.detailleproduit;
import org.example.models.produit;
import org.example.models.categorie;
import org.example.services.produitservice;
import org.example.services.categorieservice;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class produitcontroller {

    @FXML
    private TableColumn<produit, Integer> idCol;
    @FXML
    private TableColumn<produit, String> NomCol;
    @FXML
    private TableColumn<produit, String> DescriptionCol;
    @FXML
    private TableColumn<produit, String> QualiteCol;
    @FXML
    private TableColumn<produit, Integer> QuantiteCol;
    @FXML
    private TableColumn<produit, Double> PrixCol;
    @FXML
    private TableColumn<produit, LocalDateTime> DateAjoutCol;
    @FXML
    private TableColumn<produit, String> imageCol;

    @FXML
    private TableView<produit> produitTableView;

    @FXML
    private TextField nomTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private ComboBox<String> qualiteComboBox;
    @FXML
    private ComboBox<String> categorieComboBox;
    @FXML
    private TextField quantiteTextField;
    @FXML
    private TextField prixTextField;
    @FXML
    private DatePicker dateAjoutDatePicker;
    @FXML
    private TextField imageTextField;
    @FXML
    private Button closeButton;

    private Map<String, Integer> categorieMap = new HashMap<>();

    @FXML
    void initializeb() {
        // Initialisation ComboBox Qualité
        qualiteComboBox.getItems().addAll("Standard", "Premium", "Bio");
        qualiteComboBox.setValue("choisir une qualité");

        // Initialisation ComboBox Catégorie
        try {
            categorieservice catService = new categorieservice();
            List<categorie> categories = catService.getAll();
            for (categorie c : categories) {
                categorieComboBox.getItems().add(c.getNom());
                categorieMap.put(c.getNom(), c.getId());
            }
            categorieComboBox.setValue("Choisir une catégorie");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ajouterProduitAction(ActionEvent event) {
        try {
            String nom = nomTextField.getText();
            String description = descriptionTextField.getText();
            String qualite = qualiteComboBox.getValue();
            int quantite_disponible = Integer.parseInt(quantiteTextField.getText());
            double prix = Double.parseDouble(prixTextField.getText());
            String image = imageTextField.getText();
            String nomCategorie = categorieComboBox.getValue();
            Integer idCategorie = categorieMap.get(nomCategorie);

            if (nom.isEmpty() || description.isEmpty() || qualite == null || image.isEmpty() || idCategorie == null) {
                showAlert("Champs manquants", "Veuillez remplir tous les champs !");
                return;
            }

            LocalDate dateAjout = dateAjoutDatePicker.getValue();
            if (dateAjout == null) {
                dateAjout = LocalDate.now();
            }

            produit p = new produit(nom, description, qualite, quantite_disponible, prix, dateAjout.atStartOfDay(), image, idCategorie);
            produitservice service = new produitservice();
            service.addproduit(p);

            showInfo("Produit ajouté", "Produit ajouté avec succès !");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailProduit.fxml"));
            Parent root = loader.load();
            detailleproduit detail = loader.getController();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            detail.setNomTextField(dateAjout.format(formatter));
            detail.setNomTextField(nom);
            detail.setDescriptionTextField(description);
            detail.setQualiteComboBox(qualite);
            detail.setQuantiteTextField(String.valueOf(quantite_disponible));
            detail.setPrixTextField(String.valueOf(prix));
            detail.setImageTextField(image);

            nomTextField.getScene().setRoot(root);

        } catch (NumberFormatException e) {
            showAlert("Erreur de format", "Quantité ou prix invalide !");
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la vue détails.");
        }
    }

    @FXML
    void choisirImageAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            imageTextField.setText(file.getAbsolutePath());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

}
