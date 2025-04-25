package backoffice.controllers;

import backoffice.controllers.EditUserListController;
import frontoffice.controllers.UserSession;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import models.user;
import services.userService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserListController {

    @FXML
    private TableView<user> userTable;
    @FXML
    private TableColumn<user, String> nameCol;
    @FXML
    private TableColumn<user, String> emailCol;
    @FXML
    private TableColumn<user, String> rolesCol;
    @FXML
    private TableColumn<user, Void> actionCol;
    @FXML
    private ImageView profileImageView;
    @FXML
    private Label adminNameLabel;
    @FXML
    private MenuButton adminMenu;

    @FXML
    private MenuItem profileMenuItem;

    @FXML
    private MenuItem logoutMenuItem;


    @FXML
    private Button closeButton;

    @FXML
    void closeApp() {
        Platform.exit();
    }

    @FXML
    void addUser(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/backofice/fxml/AddUserBack.fxml"));
            Parent root = loader.load();
            Stage addUserStage = new Stage();
            addUserStage.initModality(Modality.APPLICATION_MODAL);
            addUserStage.initOwner(((Node)event.getSource()).getScene().getWindow());

            // Set up the scene and show the popup
            Scene scene = new Scene(root);
            addUserStage.setScene(scene);
            addUserStage.setTitle("Ajouter un Utilisateur");
            addUserStage.showAndWait();


            refreshUserTable();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre d'ajout", e.getMessage());
        }
    }


    @FXML
    void viewDetails(user user) {
        if (user != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/backofice/fxml/DetailUserBack.fxml"));
                Parent root = loader.load();

                DetailUserListController detailsController = loader.getController();
                detailsController.initData(user);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Détails de l'utilisateur");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir les détails", e.getMessage());
            }
        }
    }

    @FXML
    void deleteItem(user user) {
        if (user != null) {
            try {
                if (showConfirmation("Confirmer la suppression",
                        "Supprimer l'utilisateur",
                        "Êtes-vous sûr de vouloir supprimer l'utilisateur '" + user.getName() + "' ?")) {

                    userService service = new userService();
                    service.delete(user);
                    refreshUserTable();

                    showAlert("Succès", "Utilisateur supprimé", "L'utilisateur a été supprimé avec succès.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur de suppression", "Impossible de supprimer l'utilisateur: " + e.getMessage());
            }
        }
    }

    private void refreshUserTable() {
        try {
            userService service = new userService();
            List<user> users = service.getAll();
            ObservableList<user> observableList = FXCollections.observableArrayList(users);
            userTable.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleLogout() {
        UserSession.clearSession();
        redirectToLogin();
    }


    @FXML
    public void initialize() {
        System.out.println("Initializing user table...");

        // Check if the user is authenticated
        if (!UserSession.isAuthenticated()) {
            Platform.runLater(this::redirectToLogin);
            return;
        }

        // Access current user from the UserSession
        user currentUser = UserSession.getInstance().getUser();

        // Set admin name label
        String adminName = currentUser.getName();
        adminNameLabel.setText(adminName);

        // Set profile image if available
        String profileImagePath = currentUser.getImageProfile();
        if (profileImagePath != null && !profileImagePath.isEmpty()) {
            profileImageView.setImage(new Image("file:" + profileImagePath));
        }

        try {
            // Set up table columns
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
            rolesCol.setCellValueFactory(new PropertyValueFactory<>("roles"));

            // Set up action column
            setupActionColumn();

            // Refresh user table
            refreshUserTable();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error initializing table: " + e.getMessage());
        }
    }



    private void redirectToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontoffices/fxml/login.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();


            Stage current = (Stage) closeButton.getScene().getWindow();
            current.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    private void setupActionColumn() {
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final HBox buttons = new HBox(5);
            private final Button editBtn = new Button();
            private final Button deleteBtn = new Button();
            private final Button viewBtn = new Button();

            {
                // Styling buttons
                buttons.setAlignment(Pos.CENTER);

                // Edit button setup
                ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/assets/edit.png")));
                editIcon.setFitHeight(16);
                editIcon.setFitWidth(16);
                editBtn.setGraphic(editIcon);
                editBtn.setStyle("-fx-background-color: transparent;");
                editBtn.setOnAction(event -> {
                    user userData = getTableView().getItems().get(getIndex());
                    editItem(userData);
                });

                // Delete button setup
                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/assets/trash.png")));
                deleteIcon.setFitHeight(16);
                deleteIcon.setFitWidth(16);
                deleteBtn.setGraphic(deleteIcon);
                deleteBtn.setStyle("-fx-background-color: transparent;");
                deleteBtn.setOnAction(event -> {
                    user userData = getTableView().getItems().get(getIndex());
                    deleteItem(userData);
                });

                // View button setup
                ImageView viewIcon = new ImageView(new Image(getClass().getResourceAsStream("/assets/eye.png")));
                viewIcon.setFitHeight(16);
                viewIcon.setFitWidth(16);
                viewBtn.setGraphic(viewIcon);
                viewBtn.setStyle("-fx-background-color: transparent;");
                viewBtn.setOnAction(event -> {
                    user userData = getTableView().getItems().get(getIndex());
                    viewDetails(userData);
                });

                buttons.getChildren().addAll(viewBtn, editBtn, deleteBtn);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
    }

    @FXML
    void editItem(user user) {
        if (user != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/backofice/fxml/EditUserBack.fxml"));
                Parent root = loader.load();

                EditUserListController editController = loader.getController();
                editController.setUserData(user);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Utilisateur");
                stage.showAndWait();

                refreshUserTable();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification", e.getMessage());
            }
        } else {
            showAlert("Aucune sélection", "Aucun utilisateur sélectionné", "Veuillez sélectionner un utilisateur à modifier.");
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean showConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    @FXML
    private void handleProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/backofice/fxml/AdminProfile.fxml"));
            Parent profileRoot = loader.load();
            adminMenu.getScene().setRoot(profileRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void handleEditProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/backofice/fxml/EditAdminProfile.fxml"));
            Parent root = loader.load();

            // Pass current user to EditProfileController
            EditAdminProfileController EditAdminProfileController = loader.getController();
            EditAdminProfileController.initData(UserSession.getInstance().getUser());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier le profil");
            stage.showAndWait();

            // Optional: refresh UI elements after editing
            user updatedUser = UserSession.getInstance().getUser();
            adminNameLabel.setText(updatedUser.getName());
            if (updatedUser.getImageProfile() != null && !updatedUser.getImageProfile().isEmpty()) {
                profileImageView.setImage(new Image("file:" + updatedUser.getImageProfile()));
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la page de modification de profil", e.getMessage());
        }
    }



}