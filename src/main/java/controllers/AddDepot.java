package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Depots;
import services.DepotsServices;

import java.io.IOException;
import java.time.LocalDate;

public class AddDepot {

    @FXML
    private TextField nomTextField;
    @FXML
    private TextField adresseTextField;
    @FXML
    private TextField capaciteTextField;
    @FXML
    private TextField imageTextField;
    @FXML
    private Button closeButton;

    @FXML
    void closeApp() {
        Platform.exit();
    }

    @FXML
    void canncelAddDepot() {
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
    private Label nomErrorLabel;
    @FXML
    private Label adresseErrorLabel;
    @FXML
    private Label capaciteErrorLabel;
    @FXML
    private Label imageErrorLabel;

    @FXML
    void ajouterDepotAction() {
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

        if (hasError) {
            return;
        }

        // Continue if no errors
        int capacite = Integer.parseInt(capaciteText);
        Depots d = new Depots(capacite, nom, adresse, image);
        DepotsServices cs = new DepotsServices();
        cs.add(d);

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
}
