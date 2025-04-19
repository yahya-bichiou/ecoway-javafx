package controllers;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.evenement;
import models.participant;
import services.evenementService;


public class EditEventBackController {
    @FXML private TextField titreField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField contactField;
    @FXML private TextField localisationField;
    @FXML private DatePicker datePicker;
    @FXML private TextField recomponseField;

    private evenement currentEvent;

    public void setEventData(evenement event) {
        this.currentEvent = event;
        titreField.setText(event.getTitre());
        descriptionArea.setText(event.getDescription());
        contactField.setText(event.getContact());
        localisationField.setText(event.getLocalisation());
        datePicker.setValue(event.getDate_d());
        recomponseField.setText(String.valueOf(event.getRecomponse()));
    }

    @FXML
    private void saveChanges() {
        try {
            currentEvent.setTitre(titreField.getText());
            currentEvent.setDescription(descriptionArea.getText());
            currentEvent.setContact(contactField.getText());
            currentEvent.setLocalisation(localisationField.getText());
            currentEvent.setDate_d(datePicker.getValue());
            currentEvent.setRecomponse(Integer.parseInt(recomponseField.getText()));

            evenementService service = new evenementService();
            service.updateEvenement(currentEvent);

            // Fermer la fenêtre
            ((Stage) titreField.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            // Afficher un message d'erreur
        }
    }

    @FXML
    private void cancel() {
        ((Stage) titreField.getScene().getWindow()).close();
    }
}
