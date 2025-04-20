package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.models.commentaire;
import org.example.models.post;
import org.example.services.commentaireService;
import org.example.services.postService;

import java.sql.SQLException;
import java.util.List;

public class AddCommentaireBackController {

    @FXML
    private TextArea messageField;

    @FXML
    private TextField auteurField;

    @FXML
    private ComboBox<post> postComboBox;

    private final commentaireService commentaireService;
    private final postService postService;

    private listCommentaireBackController parentController;

    public AddCommentaireBackController() throws SQLException {
        this.commentaireService = new commentaireService();
        this.postService = new postService();
    }

    public void setParentController(listCommentaireBackController controller) {
        this.parentController = controller;
    }

    @FXML
    public void initialize() {
        try {
            List<post> posts = postService.getAllPosts();
            postComboBox.getItems().addAll(posts);
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les posts : " + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        String message = messageField.getText().trim();
        String auteur = auteurField.getText().trim();
        post selectedPost = postComboBox.getValue();

        if (message.isEmpty() || auteur.isEmpty() || selectedPost == null) {
            showAlert("Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            commentaire newCommentaire = new commentaire(message, auteur, selectedPost.getId());
            commentaireService.createCommentaire(newCommentaire);
            showAlert("Succès", "Commentaire ajouté avec succès.");

            if (parentController != null) {
                parentController.loadCommentaires();
            }

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
        Stage stage = (Stage) messageField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
