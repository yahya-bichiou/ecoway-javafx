package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.evenement;
import models.user;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DetailEventFrontController {

    @FXML
    private TextField titreField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField contactField;
    @FXML
    private TextField localisationField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField recomponseField;

    // teb3in el User
    @FXML private Label userNameLabel;
    @FXML private Label userEmailLabel;// teba3 user
    private user loggedInUser;


    private evenement currentEvent; // Ajout pour stocker l'événement courant
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");




    @FXML
    public void initialize() {
        // Configuration du DatePicker
        datePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }

                //teba3 user
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        //teba3 user
        if (loggedInUser != null && userNameLabel != null) {
            userNameLabel.setText(loggedInUser.getName());
        }
    }

    //heti teb3a user :
    public void initData(evenement event, user loggedInUser) {
        // Stocker les données
        this.currentEvent = event;
        this.loggedInUser = loggedInUser;

        // Afficher les données de l'événement
        titreField.setText(event.getTitre());
        descriptionField.setText(event.getDescription());
        contactField.setText(event.getContact());
        localisationField.setText(event.getLocalisation());
        datePicker.setValue(event.getDate_d());
        recomponseField.setText(String.valueOf(event.getRecomponse()));

        // Afficher le nom de l'utilisateur connecté
        if (loggedInUser != null && userNameLabel != null && userEmailLabel != null) {
            userNameLabel.setText(loggedInUser.getName());
            userEmailLabel.setText(loggedInUser.getEmail());
        } else {
            System.err.println("Erreur: Utilisateur non connecté ou label non initialisé");
        }
    }

    /*public void initData(evenement event, user loggedInUser) {
        this.currentEvent = event; // Stocker l'événement
        titreField.setText(event.getTitre());
        descriptionField.setText(event.getDescription());
        contactField.setText(event.getContact());
        localisationField.setText(event.getLocalisation());
        datePicker.setValue(event.getDate_d());
        recomponseField.setText(String.valueOf(event.getRecomponse()));
    }*/

    @FXML
    private void handleBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/listEventFront.fxml"));
            Stage stage = (Stage) recomponseField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du retour: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void AddParticipant(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addParticipantFront.fxml"));
            Parent root = loader.load();

           // Récupérer le contrôleur et passer l'ID de l'événement
            AddParticipantController controller = loader.getController();
            controller.setEventId(currentEvent.getId());

            // Passer à la fois l'événement et l'utilisateur connecté
            controller.initData( loggedInUser);



            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Participant");
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors de l'ouverture du formulaire participant: " + e.getMessage());
            e.printStackTrace();
        }
    }
}