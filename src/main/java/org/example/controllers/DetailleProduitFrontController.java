package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.models.produit;

public class DetailleProduitFrontController {

    @FXML private ImageView productImage;
    @FXML private Label productName;
    @FXML private Label productDescription;
    @FXML private Label productPrice;

    // Méthode pour initialiser les données du produit dans le popup
    public void initData(produit produit) {
        // Initialiser les données du produit
        productName.setText(produit.getNom());
        productDescription.setText(produit.getDescription());
        productPrice.setText(String.format("%.2f €", produit.getPrix()));

        try {
            // Charger l'image du produit
            Image image = new Image(getClass().getResourceAsStream("/images/" + produit.getImage()));
            productImage.setImage(image);
        } catch (Exception e) {
            productImage.setImage(new Image(getClass().getResourceAsStream("/images/compost.jpeg")));
        }
    }

    // Méthode pour fermer le popup
    @FXML
    private void closePopup() {
        Stage stage = (Stage) productName.getScene().getWindow();
        stage.close();
    }
}
