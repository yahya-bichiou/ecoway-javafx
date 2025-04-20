package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.models.commentaire;
import org.example.services.commentaireService;
import org.example.services.postService;

import java.sql.SQLException;

public class EditCommentaireController {

    @FXML
    private TextField auteurField;

    @FXML
    private TextArea messageField;

    @FXML
    private Label postTitleLabel;

    private commentaire currentCommentaire;
    private final commentaireService service;

    public EditCommentaireController() throws SQLException {
        this.service = new commentaireService();
    }

    public void setCommentaire(commentaire c) {
        this.currentCommentaire = c;
        auteurField.setText(c.getAuteur());
        messageField.setText(c.getMessage());

        // Affiche le titre du post associé (lecture seule)
        try {
            postService postService = new postService();
            String titre = postService.getPostTitleById(c.getPostId());
            postTitleLabel.setText(titre);
        } catch (SQLException e) {
            postTitleLabel.setText("Titre introuvable");
        }
    }

    @FXML
    private void handleUpdate() {
        String message = messageField.getText().trim();
        String auteur = auteurField.getText().trim();

        if (message.isEmpty() || auteur.isEmpty()) {
            showAlert("Champs vides", "Tous les champs doivent être remplis.");
            return;
        }

        try {
            currentCommentaire.setMessage(message);
            currentCommentaire.setAuteur(auteur);

            service.updateCommentaire(currentCommentaire);
            showAlert("Succès", "Commentaire mis à jour avec succès.");
            closeWindow();
        } catch (SQLException e) {
            showAlert("Erreur SQL", e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) auteurField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
}
