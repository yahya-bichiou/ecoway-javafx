package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
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
import org.example.services.reactionService;
import org.example.models.reaction;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class listPostFrontController {

    @FXML
    private VBox postsContainer;


    private final String currentUser = "visiteur_temp"; // utilisateur fictif

    @FXML
    private TextField searchField;

    private postService service;
    private reactionService reactionService;

    public listPostFrontController() throws SQLException {
        service = new postService();
        reactionService = new reactionService();
    }

    @FXML
    public void initialize() {
        loadPosts(null); // charge tout au début

        // 🔄 Recherche dynamique : à chaque frappe dans le champ de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadPosts(newValue);
        });
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
        card.setPadding(new Insets(15));
        card.getStyleClass().add("post-card");
        card.setMaxWidth(700);
        card.setPrefWidth(Region.USE_COMPUTED_SIZE);

        ImageView imageView = new ImageView();
        try {
            File file = new File(post.getImage());
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
                imageView.setFitWidth(680);
                imageView.setFitHeight(300);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
            }
        } catch (Exception e) {
            System.out.println("Erreur image : " + e.getMessage());
        }

        Label title = new Label(post.getTitre());
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2E7D32;");

        Label desc = new Label(post.getDescription());
        desc.setWrapText(true);
        desc.setMaxWidth(680);
        desc.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        Label creator = new Label("Créateur : " + post.getCreateur());
        creator.setStyle("-fx-font-size: 12px; -fx-text-fill: #777;");

        Button detailBtn = new Button("Lire l'article");
        detailBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        detailBtn.setOnAction(e -> showPostDetails(post));
        VBox.setMargin(detailBtn, new Insets(10, 0, 0, 0));

        try {
            Label likeCount = new Label(String.valueOf(reactionService.countReactionsByType(post.getId(), "like")));
            likeCount.getStyleClass().add("reaction-count");

            Label dislikeCount = new Label(String.valueOf(reactionService.countReactionsByType(post.getId(), "dislike")));
            dislikeCount.getStyleClass().add("reaction-count");

            Button likeBtn = new Button("👍");
            likeBtn.getStyleClass().add("like-button");

            Button dislikeBtn = new Button("👎");
            dislikeBtn.getStyleClass().add("dislike-button");

            likeBtn.setOnAction(e -> {
                try {
                    if (!reactionService.hasUserReacted(post.getId(), currentUser)) {
                        reactionService.addReaction(new reaction(post.getId(), "like", currentUser));
                    } else {
                        reactionService.updateReaction(post.getId(), currentUser, "like");
                    }
                    likeCount.setText(String.valueOf(reactionService.countReactionsByType(post.getId(), "like")));
                    dislikeCount.setText(String.valueOf(reactionService.countReactionsByType(post.getId(), "dislike")));
                } catch (SQLException ex) {
                    showAlert("Erreur", "Impossible d'ajouter/modifier le like : " + ex.getMessage());
                }
            });

            dislikeBtn.setOnAction(e -> {
                try {
                    if (!reactionService.hasUserReacted(post.getId(), currentUser)) {
                        reactionService.addReaction(new reaction(post.getId(), "dislike", currentUser));
                    } else {
                        reactionService.updateReaction(post.getId(), currentUser, "dislike");
                    }
                    likeCount.setText(String.valueOf(reactionService.countReactionsByType(post.getId(), "like")));
                    dislikeCount.setText(String.valueOf(reactionService.countReactionsByType(post.getId(), "dislike")));
                } catch (SQLException ex) {
                    showAlert("Erreur", "Impossible d'ajouter/modifier le dislike : " + ex.getMessage());
                }
            });

            VBox likeSection = new VBox(5, likeBtn, likeCount);
            likeSection.setAlignment(Pos.CENTER);
            likeSection.getStyleClass().add("reaction-section");

            VBox dislikeSection = new VBox(5, dislikeBtn, dislikeCount);
            dislikeSection.setAlignment(Pos.CENTER);
            dislikeSection.getStyleClass().add("reaction-section");

            HBox reactionBox = new HBox(20, likeSection, dislikeSection);
            reactionBox.setAlignment(Pos.CENTER_LEFT);
            reactionBox.getStyleClass().add("reaction-box");

            card.getChildren().addAll(imageView, title, desc, creator, detailBtn, reactionBox);

        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des réactions : " + e.getMessage());
            card.getChildren().addAll(imageView, title, desc, creator, detailBtn);
        }

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

            loadPosts(null);
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
