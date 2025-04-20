package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.models.commentaire;
import org.example.models.post;
import org.example.services.commentaireService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DetailFrontPostController {

    @FXML private ImageView postImage;
    @FXML private Label titreLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label createurLabel;
    @FXML private VBox commentList;

    private post currentPost;

    private final commentaireService commentaireService;

    public DetailFrontPostController() throws SQLException {
        commentaireService = new commentaireService();
    }

    public void initData(post post) {
        this.currentPost = post;

        // Affichage des infos
        titreLabel.setText(post.getTitre());
        descriptionLabel.setText(post.getDescription());
        createurLabel.setText(post.getCreateur());

        try {
            Image image = new Image("file:" + post.getImage());
            postImage.setImage(image);
        } catch (Exception e) {
            System.err.println("Erreur chargement image: " + e.getMessage());
        }

        loadCommentaires();
    }

    private void loadCommentaires() {
        commentList.getChildren().clear();
        try {
            List<commentaire> commentaires = commentaireService.getCommentairesByPostId(currentPost.getId());

            if (commentaires.isEmpty()) {
                Label noComment = new Label("Aucun commentaire pour ce post.");
                noComment.setStyle("-fx-text-fill: #777;");
                commentList.getChildren().add(noComment);
            } else {
                for (commentaire c : commentaires) {
                    VBox card = new VBox(5);
                    card.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #DDD; -fx-padding: 10; -fx-background-radius: 5;");
                    Label auteur = new Label(c.getAuteur());
                    auteur.setStyle("-fx-font-weight: bold; -fx-text-fill: #2196F3;");
                    Label message = new Label(c.getMessage());
                    message.setWrapText(true);

                    card.getChildren().addAll(auteur, message);
                    commentList.getChildren().add(card);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur chargement commentaires : " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/listPostFront.fxml"));
            Stage stage = (Stage) postImage.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur navigation retour : " + e.getMessage());
        }
    }

    @FXML
    private void handleAddCommentaire() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddCommentaireFront.fxml"));
            Parent root = loader.load();

            AddCommentaireController controller = loader.getController();
            controller.setPostId(currentPost.getId());

            Stage stage = new Stage();
            stage.setTitle("Ajouter un commentaire");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadCommentaires(); // Rafraîchir la liste après ajout
        } catch (IOException e) {
            System.err.println("Erreur ajout commentaire : " + e.getMessage());
        }
    }

}
