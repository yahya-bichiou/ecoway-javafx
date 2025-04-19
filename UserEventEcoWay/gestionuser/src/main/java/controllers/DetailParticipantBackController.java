package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        private void handleBack() {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/listEventBack.fxml"));
                Stage stage = (Stage) nomField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                System.err.println("Erreur lors du retour: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
