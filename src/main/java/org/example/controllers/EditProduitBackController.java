/*package org.example.controllers;

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
import java.util.List;

public class EditProduitBackController {

    @FXML private TextField nomField;
    @FXML private TextField descriptionArea;
    @FXML private TextField quantiteTextField;
    @FXML private ComboBox<String> qualiteComboBox;
    @FXML private ComboBox<String> categorieComboBox;
    @FXML private TextField prixField;
    @FXML private DatePicker dateAjoutPicker;
    @FXML private TextField imageTextField;

    private produit currentProduit;
    private List<categorie> allCategories;

    public void setProduitData(produit p) {
        this.currentProduit = p;

        // Remplir les champs avec les données du produit
        nomField.setText(p.getNom());
        descriptionArea.setText(p.getDescription());
        quantiteTextField.setText(String.valueOf(p.getQuantiteDisponible()));
        prixField.setText(String.valueOf(p.getPrix()));
        dateAjoutPicker.setValue(p.getDateAjout().toLocalDate());
        imageTextField.setText(p.getImage());

        // Qualité : exemple avec quelques valeurs prédéfinies
        qualiteComboBox.setItems(FXCollections.observableArrayList("Haute", "Moyenne", "Basse"));
        qualiteComboBox.setValue(p.getQualite());

        // Récupérer et afficher toutes les catégories
        categorieservice categorieService = new categorieservice();
        allCategories = categorieService.getAll();

        List<String> categoryNames = allCategories.stream().map(categorie::getNom).toList();
        categorieComboBox.setItems(FXCollections.observableArrayList(categoryNames));

        if (p.getCategorie() != null) {
            categorieComboBox.setValue(p.getCategorie());
        }
    }

    @FXML
    void choisirImageAction() {
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
            currentProduit.setNom(nomField.getText());
            currentProduit.setDescription(descriptionArea.getText());
            currentProduit.setQualite(qualiteComboBox.getValue());
            currentProduit.setQuantiteDisponible(Integer.parseInt(quantiteTextField.getText()));
            currentProduit.setPrix(Double.parseDouble(prixField.getText()));
            currentProduit.setDateAjout(dateAjoutPicker.getValue().atStartOfDay());
            currentProduit.setImage(imageTextField.getText());

            // Trouver la catégorie par son nom
            String selectedCategoryName = categorieComboBox.getValue();
            categorie selectedCategorie = allCategories.stream()
                    .filter(cat -> cat.getNom().equals(selectedCategoryName))
                    .findFirst().orElse(null);

            if (selectedCategorie != null) {
                currentProduit.setCategorie(selectedCategorie);
            } else {
                System.out.println("Catégorie non trouvée !");
            }

            // Sauvegarde
            produitservice service = new produitservice();
            service.updateproduit(currentProduit);

            // Fermer la fenêtre
            ((Stage) nomField.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            // Afficher une alerte si besoin
        }
    }

    @FXML
    private void cancel() {
        ((Stage) nomField.getScene().getWindow()).close();
    }
}
*/