package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.example.model.evenement;

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
    }

    public void initData(evenement event) {
        this.currentEvent = event; // Stocker l'événement
        titreField.setText(event.getTitre());
        descriptionField.setText(event.getDescription());
        contactField.setText(event.getContact());
        localisationField.setText(event.getLocalisation());
        datePicker.setValue(event.getDate_d());
        recomponseField.setText(String.valueOf(event.getRecomponse()));
    }

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