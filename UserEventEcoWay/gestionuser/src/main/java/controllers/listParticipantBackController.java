package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.participant;
import services.participantService;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
public class listParticipantBackController {// Composants TableViewEvent
    @FXML
    private TableView<participant> participantTable;
    @FXML
    private TableColumn<participant, String> nomCol;
    @FXML
    private TableColumn<participant, Integer> ageCol;
    @FXML
    private TableColumn<participant, String> emailCol;
    @FXML
    private TableColumn<participant, String> telephoneCol;
    @FXML
    private TableColumn<participant, Integer> env_idCol;
    @FXML
    private TableColumn<participant, Void> actionCol;

    @FXML
    private Button closeButton;

    @FXML
    void closeApp() {
        Platform.exit();
    }

    /*@FXML
    void addparticipant() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddParticipantBack.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Participant");
            stage.show();

            refreshParticipantTable();;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
/*
    @FXML
    void viewParticipantDetails(participant participant) {
        if (participant != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailBackParticipant.fxml"));
                Parent root = loader.load();

                DetailEventBackController detailsController = loader.getController();
                detailsController.initData(participant);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Détails de participant");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir les détails", e.getMessage());
            }
        }
    }*/
@FXML
void showParticipants() throws IOException {
    // Fermer la fenêtre actuelle
    Stage currentStage = (Stage) participantTable.getScene().getWindow();

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/listParticipantBack.fxml"));
    Parent root = loader.load();

    Stage stage = new Stage();
    stage.setScene(new Scene(root));
    stage.setTitle("Liste des participants");
    stage.show();

    currentStage.close();
}

    @FXML
    void showEvents() throws IOException {
        // Fermer la fenêtre actuelle
        Stage currentStage = (Stage) participantTable.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/listEventBack.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Liste des événements");
        stage.show();

        currentStage.close();
    }
    @FXML
    void deleteItem(participant participant) {
        if (participant != null) {
            try {
                if (showConfirmation("Confirmer la suppression",
                        "Supprimer l'événement",
                        "Êtes-vous sûr de vouloir supprimer l'événement '" + participant.getNom() + "' ?")) {

                    participantService participantService = new participantService();
                    participantService.deleteParticipant(participant.getId());
                    refreshParticipantTable();

                    showAlert("Succès", "Événement supprimé", "L'événement a été supprimé avec succès.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur de suppression", "Impossible de supprimer l'événement: " + e.getMessage());
            }
        }
    }

    private void refreshParticipantTable() {
        try {
            participantService participantService = new participantService();
            List<participant> paerticipants = participantService.getAllparticipant();
            ObservableList<participant> observableList = FXCollections.observableArrayList(paerticipants);
            participantTable.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        System.out.println("Initializing table...");

        try {
            // Set up columns evenement
            nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
            ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
            emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
            telephoneCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));
            //env_idCol.setCellValueFactory(new PropertyValueFactory<>("env_id"));

            // Load data evenement
            refreshParticipantTable();

            // Setup action columns
            setupActionColumns();

        } catch (Exception e) {
            System.out.println("ERROR during table initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void setupActionColumns() {
        // Setup for evenement table
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
                    participant  participantData = getTableView().getItems().get(getIndex());
                    editItem(participantData);
                });

                // Delete button setup
                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/assets/trash.png")));
                deleteIcon.setFitHeight(16);
                deleteIcon.setFitWidth(16);
                deleteBtn.setGraphic(deleteIcon);
                deleteBtn.setStyle("-fx-background-color: transparent;");
                deleteBtn.setOnAction(event -> {
                    participant participantData = getTableView().getItems().get(getIndex());
                    deleteItem(participantData);
                });

                // View button setup
                ImageView viewIcon = new ImageView(new Image(getClass().getResourceAsStream("/assets/eye.png")));
                viewIcon.setFitHeight(16);
                viewIcon.setFitWidth(16);
                viewBtn.setGraphic(viewIcon);
                viewBtn.setStyle("-fx-background-color: transparent;");
                viewBtn.setOnAction(event -> {
                    participant participantData = getTableView().getItems().get(getIndex());
                    //viewDetails(participantData);
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
    void editItem(participant participant) {
        if (participant != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditParticipantBack.fxml"));
                Parent root = loader.load();

                EditParticipantBackController editController = loader.getController();
                editController.setParticipantData(participant);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier participant");
                stage.showAndWait();

                refreshParticipantTable();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification", e.getMessage());
            }
        } else {
            showAlert("Aucune sélection", "Aucun événement sélectionné", "Veuillez sélectionner un événement à modifier.");
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

    public void UserData(String userName, String email) {
    }
}
