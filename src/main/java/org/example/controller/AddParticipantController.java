package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.model.participant;
import org.example.service.participantService;

import java.sql.SQLException;
import java.util.List;

public class AddParticipantController {
    // Composants TableView
   @FXML
    private TableView<participant> participantTable;

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



    // Boutons
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;

    // Autres composants
    @FXML
    private ListView<participant> participantListView;
    @FXML
    private VBox addSection;
    @FXML
    private VBox listSection;

    private participantService service;
    private ObservableList<participant> participants = FXCollections.observableArrayList();

    public AddParticipantController() throws SQLException {
        service = new participantService();
    }



    @FXML
    public void initialize() {
        loadparticipant();

        // Initialisation des sections
        if (listSection != null && addSection != null) {
            listSection.setVisible(true);
            addSection.setVisible(false);
        }
    }

        public void setEventId(int eventId) {
            envIdField.setText(String.valueOf(eventId));
            envIdField.setDisable(true); // Optionnel: empêcher la modification
        }


    private void loadparticipant() {
        participants.clear();
        try {
            List<participant> allparticipants = service.getAllparticipant();
            if (allparticipants == null || allparticipants.isEmpty()) {
                showAlert("Information", "Aucun événement à afficher");
            } else {
                participants.addAll(allparticipants);
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Échec du chargement des événements : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void showAddSection() {
        clearFields();
        addSection.setVisible(true);
        listSection.setVisible(false);
        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    @FXML
    private void showListSection() {
        addSection.setVisible(false);
        listSection.setVisible(true);
        loadparticipant();
    }

    @FXML
    private void handleAdd() {
        if (validateFields()) {
            participant participant = new participant(
                    nomField.getText().trim(),
                    Integer.parseInt(ageField.getText().trim()),
                    emailField.getText().trim(),
                    telephoneField.getText().trim(),
                    Integer.parseInt(envIdField.getText().trim())
            );

            try {
                service.createParticipant(participant);
                clearFields();
                showSuccessAlert("participant ajouté avec succès !");
                loadparticipant();
                showListSection();
            } catch (SQLException e) {
                showAlert("Erreur SQL", e.getMessage());
                e.printStackTrace();
            } catch (NumberFormatException e) {
                showAlert("Erreur", "La récompense doit être un nombre valide");
            }
        }
    }

    @FXML
    private void handleUpdate() {
        participant selected = participantTable.getSelectionModel().getSelectedItem();
        if (selected != null && validateFields()) {
            selected.setNom(nomField.getText().trim());
            selected.setAge(Integer.parseInt(ageField.getText().trim()));
            selected.setEmail(emailField.getText().trim());
            selected.setTelephone(telephoneField.getText().trim());
            selected.setEnv_id(Integer.parseInt(envIdField.getText().trim()));
            try {
                service.updateParticipant(selected);
                showSuccessAlert("Participant mis à jour avec succès !");
                loadparticipant();
                showListSection();
            } catch (SQLException e) {
                showAlert("Erreur", "Échec de la mise à jour : " + e.getMessage());
                e.printStackTrace();
            } catch (NumberFormatException e) {
                showAlert("Erreur", "La récompense doit être un nombre valide");
            }
        }
    }

    @FXML
    private void handleDelete() {
        participant selected = participantTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation de suppression");
            confirmAlert.setHeaderText("Supprimer le participant");
            confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet participant ?");

            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        service.deleteParticipant(selected.getId());
                        showSuccessAlert("participant supprimé avec succès !");
                        loadparticipant();
                    } catch (SQLException e) {
                        showAlert("Erreur", "Échec de la suppression : " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        } else {
            showAlert("Erreur", "Veuillez sélectionner un participant à supprimer.");
        }
    }

    @FXML
    private void handleClear() {
        clearFields(); // Utilise la méthode existante qui vide les champs
        participantTable.getSelectionModel().clearSelection(); // Désélectionne dans la table
        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        showListSection(); // Retour à la vue liste
    }
    @FXML
    private void handleTableSelection() {
        participant selected = participantTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            fillFields(selected);
            addSection.setVisible(true);
            listSection.setVisible(false);
            addButton.setDisable(true);
            updateButton.setDisable(false);
            deleteButton.setDisable(false);
        }
    }

    private void fillFields(participant participant) {
        nomField.setText(participant.getNom());
        ageField.setText(String.valueOf(participant.getAge()));
        emailField.setText(participant.getEmail());
        telephoneField.setText(participant.getTelephone());
        envIdField.setText(String.valueOf(participant.getEnv_id()));

    }

    private void clearFields() {
        nomField.clear();
        ageField.clear();
        emailField.clear();
        telephoneField.clear();
        envIdField.clear();
    }

    private boolean validateFields() {
        StringBuilder errorMessage = new StringBuilder();

        if (nomField.getText().trim().isEmpty()) {
            errorMessage.append("- Le nom est requis.\n");
        }
        if (ageField.getText().trim().isEmpty()) {
            errorMessage.append("- L' age est requise.\n");
        } else {
            try {
                Integer.parseInt(ageField.getText().trim());
            } catch (NumberFormatException e) {
                errorMessage.append("- L' age doit être un nombre valide.\n");
            }

            if (emailField.getText().trim().isEmpty()) {
                errorMessage.append("- L'email est requis.\n");
            }
            if (telephoneField.getText().trim().isEmpty()) {
                errorMessage.append("- Le telephone  est requise.\n");
            }
            if (envIdField.getText().trim().isEmpty()) {
                errorMessage.append("- L'evenement est requise.\n");
            } else {
                try {
                    Integer.parseInt(envIdField.getText().trim());
                } catch (NumberFormatException e) {
                    errorMessage.append("- La récompense doit être un nombre valide.\n");
                }

            }
          }

        if (errorMessage.length() > 0) {
            showAlert("Erreur de validation", errorMessage.toString());
            return false;
        }

        return true;
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
}