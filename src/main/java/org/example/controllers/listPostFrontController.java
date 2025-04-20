package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.models.post;
import org.example.services.postService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class listPostFrontController {

    @FXML
    private FlowPane postsContainer;

    @FXML
    private TextField searchField;

    private postService service;

    public listPostFrontController() throws SQLException {
        service = new postService();
    }

    @FXML
    public void initialize() {
        loadPosts(null);
    }

    private void loadPosts(String filter) {
        postsContainer.getChildren().clear();
        try {
            List<post> posts = service.getAllPosts();

            if (filter != null && !filter.isEmpty()) {
                String lowerFilter = filter.toLowerCase();
                posts = posts.stream()
                        .filter(p -> p.getTitre().toLowerCase().contains(lowerFilter))
                        .collect(Collectors.toList());
            }

            for (post post : posts) {
                postsContainer.getChildren().add(createPostCard(post));
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Échec du chargement des posts: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim();
        loadPosts(keyword);
    }

    private VBox createPostCard(post post) {
        VBox card = new VBox();
        card.setSpacing(10);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: white; -fx-border-color: #DDD; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        card.setPrefWidth(350);

        ImageView imageView = new ImageView();
        try {
            File file = new File(post.getImage());
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
                imageView.setFitWidth(340);
                imageView.setFitHeight(180);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
            }
        } catch (Exception e) {
            System.out.println("Erreur image : " + e.getMessage());
        }

        Label title = new Label(post.getTitre());
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2E7D32;");

        Label desc = new Label(post.getDescription());
        desc.setWrapText(true);
        desc.setStyle("-fx-font-size: 13px;");

        Label creator = new Label("Créateur : " + post.getCreateur());
        creator.setStyle("-fx-font-size: 12px; -fx-text-fill: #777;");

        Button detailBtn = new Button("Lire l'article");
        detailBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        detailBtn.setOnAction(e -> showPostDetails(post));

        VBox.setMargin(detailBtn, new Insets(10, 0, 0, 0));

        card.getChildren().addAll(imageView, title, desc, creator, detailBtn);
        return card;
    }

    private void showPostDetails(post post) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailFrontPost.fxml"));
            Parent root = loader.load();

            DetailFrontPostController controller = loader.getController();
            controller.initData(post);

            Stage currentStage = (Stage) postsContainer.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'afficher les détails: " + e.getMessage());
        }
    }

    private void showAddCommentForm(post post) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddCommentaireFront.fxml"));
            Parent root = loader.load();

            AddCommentaireController controller = loader.getController();
            controller.setPostId(post.getId());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Ajouter un Commentaire");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ajouter un commentaire: " + e.getMessage());
        }
    }

    @FXML
    private void showAddPostPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddPostFront.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Ajouter un Post");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadPosts(null); // Réinitialise les résultats après ajout
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ajouter un post: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
