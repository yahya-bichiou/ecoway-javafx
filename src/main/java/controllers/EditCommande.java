package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Commandes;
import services.CommandeService;

import java.io.IOException;
import java.time.LocalDate;

public class EditCommande {

    private Commandes commande;
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
        commande.setStatus(statusTextField.getText());
        commande.setMode_paiement(mode_paiementTextField.getText());
        commande.setDate(dateDatePicker.getValue());

        try {
            commande.setPrix(Float.parseFloat(prixTextField.getText()));
        } catch (NumberFormatException e) {
            System.out.println("Invalid price input!");
            return;
        }

        commande.setProduits(prixTextField.getText());

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
