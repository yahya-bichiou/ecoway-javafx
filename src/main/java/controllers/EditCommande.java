package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Commandes;
import services.CommandeService;

import java.io.IOException;
import java.time.LocalDate;

public class EditCommande {

    private Commandes commande;
    @FXML
    public Label errorLabel;
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
    private Button closeButton;

    @FXML
    void closeApp() {
        Platform.exit();
    }

    @FXML
    void canncelEditCommande() {
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
    void saveEdit() {

        errorLabel.setText("");  // Reset error label

        // Validate Client Id
        if (client_idTextField.getText().isEmpty()) {
            errorLabel.setText("Le champ Client Id est vide !");
            return;
        }
        int client_id;
        try {
            client_id = Integer.parseInt(client_idTextField.getText());
        } catch (NumberFormatException e) {
            errorLabel.setText("Client Id doit être un nombre valide !");
            return;
        }

        // Validate Status
        if (statusTextField.getText().isEmpty()) {
            errorLabel.setText("Le champ Status est vide !");
            return;
        }

        // Validate Mode de Paiement
        if (mode_paiementTextField.getText().isEmpty()) {
            errorLabel.setText("Le champ Mode de Paiement est vide !");
            return;
        }

        // Validate Date
        if (dateDatePicker.getValue() == null) {
            errorLabel.setText("La date est obligatoire !");
            return;
        }
        if (dateDatePicker.getValue().isAfter(LocalDate.now())) {
            errorLabel.setText("La date ne peut pas être dans le futur !");
            return;
        }

        // Validate Prix
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

        // Validate Produits
        if (produitsTextField.getText().isEmpty()) {
            errorLabel.setText("Le champ Produits est vide !");
            return;
        }

        // If all validations pass, update the commande
        commande.setClient_id(client_id);
        commande.setStatus(statusTextField.getText());
        commande.setMode_paiement(mode_paiementTextField.getText());
        commande.setDate(dateDatePicker.getValue());
        commande.setPrix(prix);
        commande.setProduits(produitsTextField.getText());

        CommandeService service = new CommandeService();
        service.update(commande);

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

    public void setCommande(Commandes commande) {
        this.commande = commande;
        client_idTextField.setText(String.valueOf(commande.getClient_id()));
        statusTextField.setText(commande.getStatus());
        mode_paiementTextField.setText(commande.getMode_paiement());
        dateDatePicker.setValue(commande.getDate());
        prixTextField.setText(String.valueOf(commande.getPrix()));
        produitsTextField.setText(commande.getProduits());
    }
}
