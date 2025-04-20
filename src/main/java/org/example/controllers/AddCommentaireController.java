package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.models.commentaire;
import org.example.services.commentaireService;

import java.sql.SQLException;

public class AddCommentaireController {

    @FXML
    private TextField auteurField;

    @FXML
    private TextArea messageField;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    private int currentPostId;
    private commentaireService service;

    public AddCommentaireController() throws SQLException {
        service = new commentaireService();
    }

    public void setPostId(int postId) {
        this.currentPostId = postId;
    }

    @FXML
    public void handleAdd() {
        if (validateFields()) {
            commentaire newCommentaire = new commentaire(
                    messageField.getText().trim(),
                    auteurField.getText().trim(),
                    currentPostId
            );

            try {
                service.createCommentaire(newCommentaire);
                showSuccessAlert("Commentaire ajouté avec succès !");
                closeWindow();
            } catch (SQLException e) {
                showAlert("Erreur SQL", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleCancel() {
        closeWindow();
    }

    private boolean validateFields() {
        StringBuilder errorMessage = new StringBuilder();

        if (auteurField.getText().trim().isEmpty()) {
            errorMessage.append("- L'auteur est requis.\n");
        }
        if (messageField.getText().trim().isEmpty()) {
            errorMessage.append("- Le message est requis.\n");
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

    private void closeWindow() {
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleClear() {
        // Fermer la fenêtre via n’importe quel composant de la scène (auteurField ici)
        Stage stage = (Stage) auteurField.getScene().getWindow();
        stage.close();
    }


}
