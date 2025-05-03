package org.example.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.models.commentaire;
import org.example.services.SightengineService;
import org.example.services.commentaireService;

import java.sql.SQLException;
//tracking
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
            String message = messageField.getText().trim();
            String auteur = auteurField.getText().trim();

            try {
                // Appel API Sightengine pour analyser le texte
                String jsonResponse = SightengineService.checkComment(message, "fr");SightengineService.checkComment(message, "auto"); // détecte automatiquement/
                System.out.println("Réponse API : " + jsonResponse);

                // ✅ Parser la réponse JSON avec Gson
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

                boolean hasBadMatches = false;

                if (jsonObject.has("profanity")) {
                    JsonObject profanity = jsonObject.getAsJsonObject("profanity");
                    if (profanity.has("matches") && profanity.getAsJsonArray("matches").size() > 0) {
                        hasBadMatches = true;
                    }
                }

                if (jsonObject.has("personal")) {
                    JsonObject personal = jsonObject.getAsJsonObject("personal");
                    if (personal.has("matches") && personal.getAsJsonArray("matches").size() > 0) {
                        hasBadMatches = true;
                    }
                }

                if (hasBadMatches) {
                    showAlert("Commentaire inapproprié", "Votre message contient du contenu interdit. Merci de reformuler.");
                } else {
                    // ✅ Aucun problème => ajouter le commentaire
                    commentaire newCommentaire = new commentaire(message, auteur, currentPostId);
                    service.createCommentaire(newCommentaire);
                    showSuccessAlert("Commentaire ajouté avec succès !");
                    closeWindow();
                }

            } catch (Exception e) {
                showAlert("Erreur API", "Impossible de vérifier le commentaire : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }



    private String detectLanguage(String text) {
        // Si le texte contient des caractères accentués → on considère que c'est du français
        if (text.matches(".*[àâäéèêëîïôöùûüç].*")) {
            return "fr"; // Français
        } else {
            return "en"; // Sinon Anglais
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
