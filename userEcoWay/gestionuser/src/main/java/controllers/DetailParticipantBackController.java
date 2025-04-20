package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.participant;

import java.io.IOException;



public class DetailParticipantBackController {

        @FXML
        private TextField nomField;
        @FXML
        private TextField ageField;
        @FXML
        private TextField emailField;
        @FXML
        private TextField telephoneField;

        @FXML
        public void initialize() {}



        public void initData(participant participant) {
            nomField.setText(participant.getNom());
            ageField.setText(String.valueOf(participant.getAge()));
            emailField.setText(participant.getEmail());
            telephoneField.setText(participant.getTelephone());

        }

            @FXML
            private void handleBack(ActionEvent event) {
                // Récupérer la fenêtre actuelle à partir du bouton ou d'un autre nœud
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.close();
            }
    }
