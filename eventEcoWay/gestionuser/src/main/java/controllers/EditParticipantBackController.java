package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.participant;
import services.participantService;

import java.sql.SQLException;
import java.util.Optional;

public class EditParticipantBackController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField telephoneField;
    @FXML
    private TextField envIdField;

    private participant currentParticipant;

    public void setParticipantData(participant participant) {
        this.currentParticipant = participant;
        nomField.setText(participant.getNom());
        ageField.setText(String.valueOf(participant.getAge()));
        emailField.setText(participant.getEmail());
        telephoneField.setText(participant.getTelephone());
        envIdField.setText(String.valueOf(participant.getEnv_id()));
    }

    @FXML
    private void saveChanges() {
        try {
            // Validation des champs
            if (nomField.getText().isEmpty() || emailField.getText().isEmpty()) {
                showAlert("Erreur", "Champs requis", "Le nom et l'email sont obligatoires");
                return;
            }

            // Mise à jour de l'objet participant
            currentParticipant.setNom(nomField.getText());
            currentParticipant.setAge(Integer.parseInt(ageField.getText()));
            currentParticipant.setEmail(emailField.getText());
            currentParticipant.setTelephone(telephoneField.getText());
            currentParticipant.setEnv_id(Integer.parseInt(envIdField.getText()));

            // Sauvegarde en base de données
            participantService service = new participantService();
            service.updateParticipant(currentParticipant);

            // Fermeture de la fenêtre
            closeWindow();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Format invalide", "L'âge et l'ID environnement doivent être des nombres");
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur base de données", "Impossible de mettre à jour le participant: " + e.getMessage());
        } catch (Exception e) {
            showAlert("Erreur", "Erreur inattendue", e.getMessage());
        }
    }

    @FXML
    private void cancel() {
        if (showConfirmation("Annuler", "Annuler les modifications",
                "Voulez-vous vraiment annuler les modifications ?")) {
            closeWindow();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean showConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}