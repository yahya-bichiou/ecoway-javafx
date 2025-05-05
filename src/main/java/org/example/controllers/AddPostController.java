package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.models.post;
import org.example.services.postService;

import java.io.File;
import java.sql.SQLException;
import org.example.services.TwilioService;

public class AddPostController {

    @FXML
    private TextField titreField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField createurField;

    @FXML
    private TextField imageField;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button browseButton;



    private postService service;

    public AddPostController() throws SQLException {
        service = new postService();
    }

    @FXML
    public void initialize() {
        browseButton.setOnAction(e -> openFileChooser());
    }

    @FXML
    public void handleAdd() {
        if (validateFields()) {
            post newPost = new post(
                    titreField.getText().trim(),
                    descriptionField.getText().trim(),
                    createurField.getText().trim(),
                    imageField.getText().trim()
            );


            try {
                service.createPost(newPost);
                showSuccessAlert("Post ajouté avec succès !");

                // 🔔 ENVOI DE SMS
                String numeroDestinataire = "+21628313700"; // Remplace par le numéro que tu veux
                String message = "Nouveau post ajouté : " + newPost.getTitre();
                TwilioService.sendSMS(numeroDestinataire, message);

                closeWindow();
            } catch (SQLException e) {
                showAlert("Erreur SQL", e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                showAlert("Erreur lors de l'envoi du SMS", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleCancel() {
        closeWindow();
    }

    public void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imageField.setText(selectedFile.getAbsolutePath());
        }
    }

    private boolean validateFields() {
        StringBuilder errorMessage = new StringBuilder();

        if (titreField.getText().trim().isEmpty()) {
            errorMessage.append("- Le titre est requis.\n");
        }
        if (descriptionField.getText().trim().isEmpty()) {
            errorMessage.append("- La description est requise.\n");
        }
        if (createurField.getText().trim().isEmpty()) {
            errorMessage.append("- Le créateur est requis.\n");
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
        // Fermer la fenêtre
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }


}