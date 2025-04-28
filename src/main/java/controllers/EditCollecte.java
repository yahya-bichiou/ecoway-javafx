package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Collectes;
import services.CollectesServices;


import java.io.IOException;

public class EditCollecte {

    private Collectes collectes;
    @FXML
    private TextField depot_idTextField;
    @FXML
    private TextField quantiteTextField;
    @FXML
    private DatePicker dateDatePicker;
    @FXML
    private TextField responsableTextField;


    @FXML
    private Button closeButton;

    @FXML
    private Label depot_idErrorLabel;
    @FXML
    private Label quantiteErrorLabel;
    @FXML
    private Label dateErrorLabel;
    @FXML
    private Label responsableErrorLabel;

    @FXML
    void closeApp() {
        Platform.exit();
    }

    @FXML
    void canncelEditCollecte() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DepotsBack.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void saveEdit() {
        // Reset errors
        depot_idErrorLabel.setText("");
        quantiteErrorLabel.setText("");
        dateErrorLabel.setText("");
        responsableErrorLabel.setText("");

        boolean hasError = false;

        String depot_id = depot_idTextField.getText().trim();
        String quantiteText = quantiteTextField.getText().trim();
        String responsable = responsableTextField.getText().trim();
        java.time.LocalDate dateValue = dateDatePicker.getValue();

        if (depot_id.isEmpty()) {
            depot_idErrorLabel.setText("Le DEPOT_ID est requis.");
            hasError = true;
        }

        if (dateValue == null) {
            dateErrorLabel.setText("La date est requise.");
            hasError = true;
        }

        if (quantiteText.isEmpty()) {
            quantiteErrorLabel.setText("La quantité est requise.");
            hasError = true;
        } else {
            try {
                double quantite = Double.parseDouble(quantiteText);
                if (quantite <= 0) {
                    quantiteErrorLabel.setText("La quantité doit être positive.");
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                quantiteErrorLabel.setText("Entrez un nombre valide.");
                hasError = true;
            }
        }

        if (responsable.isEmpty()) {
            responsableErrorLabel.setText("Le responsable est requis.");
            hasError = true;
        }

        if (hasError) return;

        // All valid: update collecte
        collectes.setDepot_id(Integer.parseInt(depot_id));
        collectes.setQuantite(Double.parseDouble(quantiteText));
        collectes.setDate(dateValue);
        collectes.setResponsable(responsable);

        CollectesServices service = new CollectesServices();
        service.update(collectes);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DepotsBack.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setCollectes(Collectes commande) {
        this.collectes = commande;
        depot_idTextField.setText(String.valueOf(collectes.getDepot_id()));
        quantiteTextField.setText(String.valueOf(collectes.getQuantite()));
        responsableTextField.setText(String.valueOf(collectes.getResponsable()));
        dateDatePicker.setValue(collectes.getDate());
    }

}
