package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.evenement;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import javafx.scene.Node;
import javafx.scene.control.Alert;

public class DetailEventBackController {

    @FXML
    private TextField titreField;
    @FXML
    private TextArea descriptionField;
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
    private void handleBack(ActionEvent event) {
        // Fermer la fenêtre actuelle
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    }
