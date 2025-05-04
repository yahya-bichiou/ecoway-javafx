package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
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
import models.Livraisons;
import services.CommandeService;
import services.LivraisonService;

import java.io.IOException;
import java.time.LocalDate;

public class AddLivraison {

    @FXML
    public Button closeButton;
    @FXML
    public Label errorLabel;
    @FXML
    public TextField modeTextField;
    @FXML
    public TextField statusTextField;
    @FXML
    public TextField adresseTextField;
    @FXML
    public TextField livreurTextField;
    @FXML
    public DatePicker dateDatePicker;

    private Commandes commande;

    public void setCommande(Commandes commande) {
        this.commande = commande;
    }

    public void ajouterLivraisonAction() {

        // Check livreur
        if (livreurTextField.getText().isEmpty()) {
            errorLabel.setText("Le champ Livreur est vide !");
            return;
        }

        // Check Adresse
        if (adresseTextField.getText().isEmpty()) {
            errorLabel.setText("Le champ Adresse est vide !");
            return;
        }

        // Check Status
        if (statusTextField.getText().isEmpty()) {
            errorLabel.setText("Le champ Status est vide !");
            return;
        }

        // Check Date
        if (dateDatePicker.getValue() == null) {
            errorLabel.setText("La date est obligatoire !");
            return;
        }
        if (dateDatePicker.getValue().isBefore(LocalDate.now())) {
            errorLabel.setText("La date ne peut pas être dans le passé !");
            return;
        }

        // Check Mode
        if (modeTextField.getText().isEmpty()) {
            errorLabel.setText("Le champ Mode est vide !");
            return;
        }

        String mode = modeTextField.getText();
        Livraisons l = new Livraisons(livreurTextField.getText(), adresseTextField.getText(), statusTextField.getText(), mode,
                commande.getPrix(), dateDatePicker.getValue(), commande.getId());
        LivraisonService ls = new LivraisonService();
        ls.add(l);

        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

    }

    public void closeApp() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
