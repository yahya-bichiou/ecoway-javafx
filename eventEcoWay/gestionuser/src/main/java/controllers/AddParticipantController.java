package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.participant;
import models.user;
import services.participantService;
import javafx.scene.control.Alert.AlertType;

import java.sql.SQLException;

public class AddParticipantController {
    @FXML private TextField nomField;
    @FXML private TextField ageField;
    @FXML private TextField emailField;
    @FXML private TextField telephoneField;
    @FXML private TextField envIdField;
    @FXML private Button addButton;
    @FXML private VBox addSection;

    private Runnable onAddSuccessCallback;
    private participantService service;

    // teb3in el User
    @FXML private Label userNameLabel;
    @FXML private Label userEmailLabel;// teba3 user
    private user loggedInUser;





    public AddParticipantController() throws SQLException {
        service = new participantService();
    }

    @FXML
    public void initialize() {
        // Initialisation si nécessaire
    }

    public void setOnAddSuccessCallback(Runnable callback) {
        this.onAddSuccessCallback = callback;
    }

    //heti teb3a user :
    public void initData(user loggedInUser) {
        // Stocker les données
        this.loggedInUser = loggedInUser;

        // Afficher le nom de l'utilisateur connecté
        if (loggedInUser != null) {
            if (userNameLabel != null && userEmailLabel != null) {
                userNameLabel.setText(loggedInUser.getName());
                userEmailLabel.setText(loggedInUser.getEmail());
            }
            // Remplir automatiquement le champ nom avec le nom de l'utilisateur
            if (nomField != null && emailField != null) {
                nomField.setText(loggedInUser.getName());
                nomField.setDisable(true);

                emailField.setText(loggedInUser.getEmail());
                emailField.setDisable(true);
            }
        } else {
            System.err.println("Erreur: Utilisateur non connecté ou label non initialisé");
        }
    }
    @FXML
    private void handleAdd() {
        try {
            if (validateFields()) {
                participant participant = new participant(
                        nomField.getText().trim(),
                        Integer.parseInt(ageField.getText().trim()),
                        emailField.getText().trim(),
                        telephoneField.getText().trim(),
                        Integer.parseInt(envIdField.getText().trim())
                );

                service.createParticipant(participant);
                clearFields();

                Stage stage = (Stage) nomField.getScene().getWindow();
                stage.close();

                if (onAddSuccessCallback != null) {
                    onAddSuccessCallback.run();
                }

                showAlert("Succès", "Participant ajouté avec succès", AlertType.INFORMATION);
            }
        } catch (SQLException e) {
            showAlert("Erreur SQL", "Erreur lors de l'ajout: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez vérifier les champs numériques", AlertType.ERROR);
        }
    }
    @FXML
    private void handleClear() {
        clearFields();
        addButton.setDisable(false);
    }

    private boolean validateFields() throws SQLException {
        StringBuilder errorMessage = new StringBuilder();
        participantService service = new participantService();

        if (nomField.getText().trim().isEmpty()) {
            errorMessage.append("- Le nom est requis.\n");
        }

        if (ageField.getText().trim().isEmpty()) {
            errorMessage.append("- L'âge est requis.\n");
        } else {
            try {
                int age = Integer.parseInt(ageField.getText().trim());
                if (age <= 0 || age > 120) {
                    errorMessage.append("- L'âge doit être entre 1 et 120 ans.\n");
                }
            } catch (NumberFormatException e) {
                errorMessage.append("- L'âge doit être un nombre valide.\n");
            }
        }
        String email = emailField.getText().trim();
        if (emailField.getText().trim().isEmpty()) {
            errorMessage.append("- L'email est requis.\n");
        } else if (!emailField.getText().trim().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errorMessage.append("- Format d'email invalide.\n");
        } else if (service.EmailExists(email)) {
        errorMessage.append("- Ce numéro de téléphone est déjà utilisé.\n");
        }
        String telephone = telephoneField.getText().trim();
        if (telephone.isEmpty()) {
            errorMessage.append("- Le téléphone est requis.\n");
        } else if (!telephone.matches("^[0-9]{8,15}$")) {
            errorMessage.append("- Le téléphone doit contenir entre 8 et 15 chiffres.\n");
        } else if (service.telephoneExists(telephone)) {
            errorMessage.append("- Ce numéro de téléphone est déjà utilisé.\n");
        }
        if (envIdField.getText().trim().isEmpty()) {
            errorMessage.append("- L'événement est requis.\n");
        } else if (!envIdField.getText().trim().matches("\\d+")) {
            errorMessage.append("- L'ID événement doit être un nombre valide.\n");
        }
        if (errorMessage.length() > 0) {
            showAlert("Erreur de validation", errorMessage.toString(), AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void clearFields() {
        nomField.clear();
        ageField.clear();
        emailField.clear();
        telephoneField.clear();
        envIdField.clear();
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthodes non utilisées (peuvent être supprimées si inutiles)
    @FXML private void showAddSection() {}
    @FXML private void showListSection() {}
    private void fillFields(participant participant) {}


    public void setEventId(int eventId) {
        envIdField.setText(String.valueOf(eventId));
        envIdField.setDisable(true); // Optionnel: empêcher la modification
    }

    @FXML
    private void handleClose(ActionEvent event) {
        // Récupérer la fenêtre actuelle à partir du bouton ou d'un autre nœud
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}