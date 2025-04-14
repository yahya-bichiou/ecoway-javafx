package controllers;

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
import models.Livraisons;
import services.CommandeService;
import services.LivraisonService;

import java.io.IOException;
import java.time.LocalDate;

public class EditLivraison {

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
    @FXML
    public Button closeButton;
    @FXML
    public Label errorLabel;

    private Livraisons livraison;

    public void setLivraison(Livraisons livraison) {
        this.livraison = livraison;
        modeTextField.setText(livraison.getMode());
        statusTextField.setText(livraison.getStatus());
        adresseTextField.setText(livraison.getAdresse());
        dateDatePicker.setValue(livraison.getDate());
        livreurTextField.setText(livraison.getLivreur());
    }

    @FXML
    public void modifierLivraisonAction() {

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

        // If all validations pass, update the livraison
        livraison.setLivreur(livreurTextField.getText());
        livraison.setStatus(statusTextField.getText());
        livraison.setAdresse(adresseTextField.getText());
        livraison.setDate(dateDatePicker.getValue());
        livraison.setMode(modeTextField.getText());

        LivraisonService service= new LivraisonService();
        service.update(livraison);
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void closeApp() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
