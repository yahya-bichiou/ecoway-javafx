package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Collectes;
import models.Depots;
import services.CollectesServices;
import services.DepotsServices;

import java.io.IOException;

public class EditDepot {

    private Depots depots;
    private Collectes collectes;

    @FXML
    private TextField nomTextField;
    @FXML
    private TextField adresseTextField;
    @FXML
    private TextField capaciteTextField;
    @FXML
    private TextField imageTextField;

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

    // Labels pour les erreurs
    @FXML
    private Label nomErrorLabel;
    @FXML
    private Label adresseErrorLabel;
    @FXML
    private Label capaciteErrorLabel;
    @FXML
    private Label imageErrorLabel;


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
    void canncelEditDepot() {
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
        nomErrorLabel.setText("");
        adresseErrorLabel.setText("");
        capaciteErrorLabel.setText("");
        imageErrorLabel.setText("");

        boolean hasError = false;

        String nom = nomTextField.getText().trim();
        String adresse = adresseTextField.getText().trim();
        String capaciteText = capaciteTextField.getText().trim();
        String image = imageTextField.getText().trim();

        if (nom.isEmpty()) {
            nomErrorLabel.setText("Le nom est requis.");
            hasError = true;
        }
        if (adresse.isEmpty()) {
            adresseErrorLabel.setText("L'adresse est requise.");
            hasError = true;
        }
        if (capaciteText.isEmpty()) {
            capaciteErrorLabel.setText("La capacité est requise.");
            hasError = true;
        } else {
            try {
                int capacite = Integer.parseInt(capaciteText);
                if (capacite <= 0) {
                    capaciteErrorLabel.setText("La capacité doit être positive.");
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                capaciteErrorLabel.setText("Entrez un nombre valide.");
                hasError = true;
            }
        }
        if (image.isEmpty()) {
            imageErrorLabel.setText("L'image est requise.");
            hasError = true;
        }

        if (hasError) return;

        // If all valid, save the updated depot
        depots.setNom(nom);
        depots.setAdresse(adresse);
        depots.setCapacite(Integer.parseInt(capaciteText));
        depots.setImage(image);

        DepotsServices service = new DepotsServices();
        service.update(depots);

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

    public void setDepots(Depots commande) {
        this.depots = commande;
        nomTextField.setText(depots.getNom());
        adresseTextField.setText(depots.getAdresse());
        capaciteTextField.setText(String.valueOf(depots.getCapacite()));
        imageTextField.setText(depots.getImage());
    }



}
