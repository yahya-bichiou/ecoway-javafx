package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.evenement;
import services.evenementService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddEventBackController {

    @FXML private TextField titreField;
    @FXML private TextArea descriptionField;
    @FXML private TextField contactField;
    @FXML private TextField localisationField;
    @FXML private DatePicker datePicker;
    @FXML private TextField recomponseField;
    @FXML private Button addButton;

    private evenementService service;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private Runnable onAddSuccessCallback;

    public AddEventBackController() throws SQLException {
        service = new evenementService();
    }

    public void setOnAddSuccessCallback(Runnable callback) {
        this.onAddSuccessCallback = callback;
    }

    @FXML
    public void initialize() {
        datePicker.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                try {
                    return string != null && !string.isEmpty()
                            ? LocalDate.parse(string, dateFormatter)
                            : null;
                } catch (DateTimeParseException e) {
                    return null;
                }
            }
        });
    }

    @FXML
    private void handleAdd() {
        if (validateFields()) {
            evenement event = new evenement(
                    titreField.getText().trim(),
                    descriptionField.getText().trim(),
                    contactField.getText().trim(),
                    localisationField.getText().trim(),
                    datePicker.getValue(),
                    Integer.parseInt(recomponseField.getText().trim())
            );

            try {
                service.createEvenement(event);
                clearFields();
                showSuccessAlert("Événement ajouté avec succès !");

                // Fermer la fenêtre d'ajout
                Stage stage = (Stage) titreField.getScene().getWindow();
                stage.close();

                // Notifier le parent pour actualiser la liste
                if (onAddSuccessCallback != null) {
                    onAddSuccessCallback.run();
                }

            } catch (SQLException e) {
                showAlert("Erreur SQL", e.getMessage());
                e.printStackTrace();
            } catch (NumberFormatException e) {
                showAlert("Erreur", "La récompense doit être un nombre valide");
            }
        }
    }

    @FXML
    private void handleClear() {
        clearFields();
    }

    private boolean validateFields() {
        StringBuilder errorMessage = new StringBuilder();

        if (titreField.getText().trim().isEmpty()) {
            errorMessage.append("- Le titre est requis.\n");
        }
        if (descriptionField.getText().trim().isEmpty()) {
            errorMessage.append("- La description est requise.\n");
        }
        if (contactField.getText().trim().isEmpty()) {
            errorMessage.append("- Le contact est requis.\n");
        }
        if (localisationField.getText().trim().isEmpty()) {
            errorMessage.append("- La localisation est requise.\n");
        }
        if (datePicker.getValue() == null) {
            errorMessage.append("- La date est requise.\n");
        } else {
            // Vérifier que la date est supérieure à la date actuelle
            if (datePicker.getValue().isBefore(LocalDate.now())) {
                errorMessage.append("- La date doit être supérieure à la date actuelle.\n");
            }
        }
        if (recomponseField.getText().trim().isEmpty()) {
            errorMessage.append("- La récompense est requise.\n");
        } else {
            try {
                Integer.parseInt(recomponseField.getText().trim());
            } catch (NumberFormatException e) {
                errorMessage.append("- La récompense doit être un nombre valide.\n");
            }
        }

        if (errorMessage.length() > 0) {
            showAlert("Erreur de validation", errorMessage.toString());
            return false;
        }

        return true;
    }
    private void clearFields() {
        titreField.clear();
        descriptionField.clear();
        contactField.clear();
        localisationField.clear();
        datePicker.setValue(null);
        recomponseField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode non utilisée (peut être supprimée si inutile)
    private void fillFields(evenement event) {}
}