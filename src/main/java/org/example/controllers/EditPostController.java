package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.models.post;
import org.example.services.postService;

import java.sql.SQLException;

public class EditPostController {

    @FXML private TextField titreField;
    @FXML private TextArea descriptionField;
    @FXML private TextField createurField;
    @FXML private TextField imageField;
    @FXML private Button saveButton;

    private final postService service;
    private post currentPost;

    public EditPostController() throws SQLException {
        service = new postService();
    }

    public void setPost(post p) {
        this.currentPost = p;
        titreField.setText(p.getTitre());
        descriptionField.setText(p.getDescription());
        createurField.setText(p.getCreateur());
        imageField.setText(p.getImage());
    }

    @FXML
    private void saveChanges() {
        if (currentPost != null) {
            currentPost.setTitre(titreField.getText());
            currentPost.setDescription(descriptionField.getText());
            currentPost.setCreateur(createurField.getText());
            currentPost.setImage(imageField.getText());

            try {
                service.updatePost(currentPost);
                showAlert("Succès", "Le post a été mis à jour !");
                closeWindow();
            } catch (SQLException e) {
                showAlert("Erreur", "Mise à jour échouée : " + e.getMessage());
            }
        }
    }

    @FXML
    private void cancelEdit() {
        closeWindow();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // ou ERROR selon besoin
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleUpdate(ActionEvent actionEvent) {
        // Logique de mise à jour, cette méthode pourrait être appelée pour effectuer des vérifications avant de sauvegarder
        String titre = titreField.getText().trim();
        String createur = createurField.getText().trim();
        String description = descriptionField.getText().trim();
        String image = imageField.getText().trim();

        // Vérification des champs
        if (titre.isEmpty() || createur.isEmpty() || description.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        // Mettre à jour l'objet post
        currentPost.setTitre(titre);
        currentPost.setDescription(description);
        currentPost.setCreateur(createur);
        currentPost.setImage(image);

        try {
            // Appeler la méthode de service pour mettre à jour le post dans la base de données
            service.updatePost(currentPost);
            showAlert("Succès", "Le post a été mis à jour avec succès !");
            closeWindow();  // Ferme la fenêtre après mise à jour
        } catch (SQLException e) {
            showAlert("Erreur", "Échec de la mise à jour du post : " + e.getMessage());
        }
    }

}
