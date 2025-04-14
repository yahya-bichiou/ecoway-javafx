package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Commandes;
import services.CommandeService;

import java.io.IOException;
import java.time.LocalDate;

public class AddCommande {

    @FXML
    private TextField client_idTextField;
    @FXML
    private TextField statusTextField;
    @FXML
    private TextField mode_paiementTextField;
    @FXML
    private DatePicker dateDatePicker;
    @FXML
    private TextField prixTextField;
    @FXML
    private TextField produitsTextField;
    @FXML
    private Label errorLabel;

    @FXML
    private Button closeButton;

    @FXML
    void closeApp() {
        Platform.exit();
    }

    @FXML
    void canncelAddCommande() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CommandesBack.fxml"));
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
    void ajouterCommandeAction() {

        errorLabel.setText("");

        // Check Client ID
        if (client_idTextField.getText().isEmpty()) {
            errorLabel.setText("Le champ Client ID est vide !");
            return;
        }
        int clientId;
        try {
            clientId = Integer.parseInt(client_idTextField.getText());
        } catch (NumberFormatException e) {
            errorLabel.setText("Client ID doit être un nombre !");
            return;
        }

        // Check Status
        if (statusTextField.getText().isEmpty()) {
            errorLabel.setText("Le champ Status est vide !");
            return;
        }

        // Check Mode de Paiement
        if (mode_paiementTextField.getText().isEmpty()) {
            errorLabel.setText("Le champ Mode de Paiement est vide !");
            return;
        }

        // Check Date
        if (dateDatePicker.getValue() == null) {
            errorLabel.setText("La date est obligatoire !");
            return;
        }
        if (dateDatePicker.getValue().isAfter(LocalDate.now())) {
            errorLabel.setText("La date ne peut pas être dans le futur !");
            return;
        }

        // Check Prix
        if (prixTextField.getText().isEmpty()) {
            errorLabel.setText("Le champ Prix est vide !");
            return;
        }
        float prix;
        try {
            prix = Float.parseFloat(prixTextField.getText());
        } catch (NumberFormatException e) {
            errorLabel.setText("Prix doit être un nombre valide !");
            return;
        }

        // Check Produits
        if (produitsTextField.getText().isEmpty()) {
            errorLabel.setText("Le champ Produits est vide !");
            return;
        }

        // If all checks pass
        Commandes c = new Commandes(clientId, statusTextField.getText(), mode_paiementTextField.getText(),
                dateDatePicker.getValue(), prix, produitsTextField.getText());
        CommandeService cs = new CommandeService();
        cs.add(c);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CommandesBack.fxml"));
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
    public void initialize() {
        errorLabel.setText("");
    }
}
