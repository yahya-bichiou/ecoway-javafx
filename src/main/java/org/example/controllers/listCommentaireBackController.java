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
import org.example.models.commentaire;
import org.example.services.commentaireService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class listCommentaireBackController {

    @FXML private TableView<commentaire> commentaireTable;
    @FXML private TableColumn<commentaire, Integer> colId;
    @FXML private TableColumn<commentaire, String> colMessage;
    @FXML private TableColumn<commentaire, String> colAuteur;
    @FXML private TableColumn<commentaire, Integer> colPostId;
    @FXML private TableColumn<commentaire, Void> colActions;

    @FXML private VBox blogSubmenu;
    @FXML private Button blogDashboardBtn;

    private boolean isSubmenuVisible = false;

    private final ObservableList<commentaire> commentaireList = FXCollections.observableArrayList();
    private final commentaireService service;

    public listCommentaireBackController() throws SQLException {
        this.service = new commentaireService();
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
        colAuteur.setCellValueFactory(new PropertyValueFactory<>("auteur"));

        colPostId.setCellValueFactory(new PropertyValueFactory<>("postId"));
        colPostId.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer postId, boolean empty) {
                super.updateItem(postId, empty);
                if (empty || postId == null) {
                    setText(null);
                } else {
                    try {
                        String titre = service.getPostTitleById(postId);
                        setText(titre);
                    } catch (SQLException e) {
                        setText("Erreur titre");
                    }
                }
            }
        });

        addActionsColumn();
        loadCommentaires();
    }

    public void loadCommentaires() {
        try {
            commentaireList.clear();
            commentaireList.addAll(service.getAllCommentaires());
            commentaireTable.setItems(commentaireList);
        } catch (SQLException e) {
            showAlert("Erreur", "Chargement des commentaires a échoué: " + e.getMessage());
        }
    }

    private void addActionsColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button();
            private final Button deleteButton = new Button();
            private final HBox container = new HBox(10, editButton, deleteButton);

            {
                ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/edit.png")));
                editIcon.setFitHeight(16);
                editIcon.setFitWidth(16);
                editButton.setGraphic(editIcon);
                editButton.setStyle("-fx-background-color: transparent;");

                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/trash.png")));
                deleteIcon.setFitHeight(16);
                deleteIcon.setFitWidth(16);
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setStyle("-fx-background-color: transparent;");

                editButton.setOnAction(e -> {
                    commentaire selected = getTableView().getItems().get(getIndex());
                    showEditPopup(selected);
                });

                deleteButton.setOnAction(e -> {
                    commentaire selected = getTableView().getItems().get(getIndex());
                    try {
                        service.deleteCommentaire(selected.getId());
                        loadCommentaires();
                    } catch (SQLException ex) {
                        showAlert("Erreur", "Suppression impossible: " + ex.getMessage());
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

    private void showEditPopup(commentaire com) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/editCommentaire.fxml"));
            Parent root = loader.load();
            EditCommentaireController controller = loader.getController();
            controller.setCommentaire(com);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Commentaire");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadCommentaires();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur ouverture popup: " + e.getMessage());
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
    public void handleAddCommentaire() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addCommentaireBack.fxml"));
            Parent root = loader.load();

            AddCommentaireBackController controller = loader.getController();
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Ajouter un Commentaire");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadCommentaires();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire : " + e.getMessage());
        }
    }

    @FXML
    private void navigateToPosts() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/listPostBack.fxml"));
            Parent root = loader.load();
            commentaireTable.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void navigateToCommentaires() {
    }


    @FXML
    private void toggleBlogMenu() {
        isSubmenuVisible = !isSubmenuVisible;
        blogSubmenu.setVisible(isSubmenuVisible);
        blogSubmenu.setManaged(isSubmenuVisible);
        blogDashboardBtn.setText(isSubmenuVisible ? "  Blog Dashboard ▲" : "  Blog Dashboard ▼");
    }
}
