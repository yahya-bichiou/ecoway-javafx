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

        int clientId = Integer.parseInt(client_idTextField.getText());
        String status = statusTextField.getText();
        String modeDePaiement = mode_paiementTextField.getText();
        LocalDate date = dateDatePicker.getValue();
        float prix = Float.parseFloat(prixTextField.getText());
        String produits = produitsTextField.getText();

        Commandes c = new Commandes(clientId,status,modeDePaiement,date,prix,produits);
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
}
