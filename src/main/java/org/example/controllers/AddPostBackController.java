package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.models.post;
import org.example.services.postService;

import java.io.File;
import java.sql.SQLException;

public class AddPostBackController {

    @FXML
    private TextField titreField;
    @FXML
    private TextField createurField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField imageField;

    private final postService service;

    // Référence au contrôleur parent pour pouvoir rafraîchir la TableView
    private listPostBackController parentController;

    public AddPostBackController() throws SQLException {
        this.service = new postService();
    }

    // Cette méthode va être appelée par le contrôleur parent pour lier l'instance
    public void setParentController(listPostBackController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void handleAddPost() {
        String titre = titreField.getText().trim();
        String createur = createurField.getText().trim();
        String description = descriptionField.getText().trim();
        String image = imageField.getText().trim();

        if (titre.isEmpty() || createur.isEmpty() || description.isEmpty()) {
            showAlert("Erreur de validation", "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        post newPost = new post(titre, description, createur, image);
        try {
            service.createPost(newPost);
            showAlert("Succès", "Post ajouté avec succès.");

            // Rafraîchir la table dans le contrôleur parent
            if (parentController != null) {
                parentController.loadPosts();
            }

            closeWindow();
        } catch (SQLException e) {
            showAlert("Erreur SQL", "Impossible d'ajouter le post: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    @FXML
    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imageField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
