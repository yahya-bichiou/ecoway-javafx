package controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.user;

import java.io.IOException;

public class FrontTamplateController {


    @FXML private Label userNameLabel;// teba3 user
    @FXML private Label userEmailLabel;// teba3 user
    private user loggedInUser;

    //Fonction teba3 user
    public void setLoggedInUser(user user) {
        this.loggedInUser = user;
        if (userNameLabel != null && userEmailLabel != null) {
            userNameLabel.setText(user.getName());
            userEmailLabel.setText(user.getEmail());
        }
    }
    @FXML
    public void initialize() {
        //teba3 user
        if (loggedInUser != null && userNameLabel != null  && userEmailLabel != null) {
            userNameLabel.setText(loggedInUser.getName());
            userEmailLabel.setText(loggedInUser.getEmail());
        }
    }
    @FXML
        private void NavigateToEvent(ActionEvent event) { // Add ActionEvent parameter
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/listEventFront.fxml"));
            Parent root = loader.load();

            listEventFrontController controller = loader.getController();
            if (controller == null) {
                throw new IOException("Le contrôleur n'a pas été initialisé dans le FXML");
            }

            // Passer à la fois l'événement et l'utilisateur connecté
            controller.initData( loggedInUser);

            // Créer une nouvelle fenêtre au lieu d'utiliser la fenêtre actuelle
            Stage detailStage = new Stage();
            detailStage.setScene(new Scene(root));
            detailStage.setTitle("Détails de l'événement");


            // Empêcher la fermeture de la fenêtre principale
            detailStage.initModality(Modality.WINDOW_MODAL); // ou Modality.NONE pour une interaction complète

            detailStage.show();

        } catch (IOException e) {
            System.err.println("Échec du chargement de la vue de détail:");
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de navigation");
            alert.setHeaderText("Impossible d'afficher les détails");
            alert.setContentText("Erreur technique: " + e.getMessage());
            alert.showAndWait();
        }}}
