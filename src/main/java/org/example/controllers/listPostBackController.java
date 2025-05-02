package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.models.post;
import org.example.services.postService;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class listPostBackController {

    @FXML
    private TableView<post> postTable;
    @FXML
    private TableColumn<post, Integer> colId;
    @FXML
    private TableColumn<post, String> colTitre;
    @FXML
    private TableColumn<post, String> colDescription;
    @FXML
    private TableColumn<post, String> colCreateur;
    @FXML
    private TableColumn<post, String> colImage;
    @FXML
    private TableColumn<post, Void> colActions;

    @FXML
    private VBox blogSubmenu;
    @FXML
    private Button blogDashboardBtn;

    private boolean isSubmenuVisible = false;

    private final ObservableList<post> postList = FXCollections.observableArrayList();
    private final postService service;

    public listPostBackController() throws SQLException {
        this.service = new postService();
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCreateur.setCellValueFactory(new PropertyValueFactory<>("createur"));

        colImage.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitHeight(40);
                imageView.setFitWidth(60);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null || imagePath.trim().isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        File file = new File(imagePath);
                        if (file.exists()) {
                            Image image = new Image(file.toURI().toString());
                            imageView.setImage(image);
                            setGraphic(imageView);
                        } else {
                            setGraphic(new Label("Image introuvable"));
                        }
                    } catch (Exception e) {
                        setGraphic(new Label("Erreur image"));
                    }
                }
            }
        });

        addActionsColumn();
        loadPosts();
    }

    public void loadPosts() {
        try {
            postList.clear();
            List<post> posts = service.getAllPosts();
            postList.addAll(posts);
            postTable.setItems(postList);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des posts: " + e.getMessage());
        }
    }

    private void addActionsColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button viewButton = new Button();
            private final Button editButton = new Button();
            private final Button deleteButton = new Button();
            private final HBox container = new HBox(10, viewButton, editButton, deleteButton);

            {
                ImageView viewIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/eye.png")));
                viewIcon.setFitHeight(16);
                viewIcon.setFitWidth(16);
                viewButton.setGraphic(viewIcon);
                viewButton.setStyle("-fx-background-color: transparent;");
                viewButton.setOnAction(event -> {
                    post selected = getTableView().getItems().get(getIndex());
                    openCommentairesForPost(selected.getId());
                });

                ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/edit.png")));
                editIcon.setFitHeight(16);
                editIcon.setFitWidth(16);
                editButton.setGraphic(editIcon);
                editButton.setStyle("-fx-background-color: transparent;");
                editButton.setOnAction(event -> {
                    post selected = getTableView().getItems().get(getIndex());
                    showEditPopup(selected);
                });

                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/trash.png")));
                deleteIcon.setFitHeight(16);
                deleteIcon.setFitWidth(16);
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setStyle("-fx-background-color: transparent;");
                deleteButton.setOnAction(event -> {
                    post selected = getTableView().getItems().get(getIndex());
                    try {
                        service.deletePost(selected.getId());
                        loadPosts();
                        showAlert("Succès", "Post supprimé avec succès.");
                    } catch (SQLException e) {
                        showAlert("Erreur", "Erreur lors de la suppression du post: " + e.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });
    }

    private void openCommentairesForPost(int postId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/listCommentaireBack.fxml"));
            Parent root = loader.load();

            listCommentaireBackController controller = loader.getController();
            controller.loadCommentairesForPost(postId);

            Stage stage = new Stage();
            stage.setTitle("Commentaires du Post");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException | SQLException e) {
            showAlert("Erreur", "Impossible d'afficher les commentaires : " + e.getMessage());
        }
    }

    private void showEditPopup(post postToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/editPost.fxml"));
            Parent root = loader.load();
            EditPostController controller = loader.getController();
            controller.setPost(postToEdit);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            applyBackCSS(scene);
            stage.setScene(scene);
            stage.setTitle("Modifier un Post");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification: " + e.getMessage());
        }
    }

    private void applyBackCSS(Scene scene) {
        URL css = getClass().getResource("/back.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        } else {
            System.err.println("CSS not found: /back.css");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void handleAddPost() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addPostBack.fxml"));
            Parent root = loader.load();

            AddPostBackController addPostController = loader.getController();
            addPostController.setParentController(this);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Ajouter un nouveau Post");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de création du post: " + e.getMessage());
        }
    }

    @FXML
    private void navigateToPosts() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/listPostBack.fxml"));
            Parent root = loader.load();
            postTable.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void navigateToCommentaires() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/listCommentaireBack.fxml"));
            Parent root = loader.load();
            postTable.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void toggleBlogMenu() {
        isSubmenuVisible = !isSubmenuVisible;
        blogSubmenu.setVisible(isSubmenuVisible);
        blogSubmenu.setManaged(isSubmenuVisible);
        blogDashboardBtn.setText(isSubmenuVisible ? "  Blog Dashboard ▲" : "  Blog Dashboard ▼");
    }
}
