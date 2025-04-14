package org.example.controller;

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
import org.example.model.evenement;
import org.example.service.evenementService;
import org.example.model.participant;
import org.example.service.participantService;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class listEventBackController {

    // Composants TableViewEvent
    @FXML
    private TableView<evenement> evenementTable;
    @FXML
    private TableColumn<evenement, Integer> idCol;
    @FXML
    private TableColumn<evenement, String> titreCol;
    @FXML
    private TableColumn<evenement, String> descriptionCol;
    @FXML
    private TableColumn<evenement, String> contactCol;
    @FXML
    private TableColumn<evenement, String> localisationCol;
    @FXML
    private TableColumn<evenement, String> date_dCol;
    @FXML
    private TableColumn<evenement, Integer> recomponseCol;
    @FXML
    private TableColumn<evenement, Void> actionCol;

    // Composants TableViewParticipant
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
    private TableColumn<participant, Integer> envIdCol;
    @FXML
    private TableColumn<participant, Void> actionCol2;

    @FXML
    private Button closeButton;

    @FXML
    void closeApp() {
        Platform.exit();
    }

    @FXML
    void addevenement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddEventBack.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addParticipant() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddParticipantBack.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void viewDetails(evenement event) {
        if (event != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailEventBack.fxml"));
                Parent root = loader.load();

                DetailEventBackController detailsController = loader.getController();
                detailsController.initData(event);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Détails de l'événement");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir les détails", e.getMessage());
            }
        }
    }

    @FXML
    void deleteItem(evenement event) {
        if (event != null) {
            try {
                if (showConfirmation("Confirmer la suppression",
                        "Supprimer l'événement",
                        "Êtes-vous sûr de vouloir supprimer l'événement '" + event.getTitre() + "' ?")) {

                    evenementService eventService = new evenementService();
                    eventService.deleteEvenement(event.getId());
                    refreshEventTable();

                    showAlert("Succès", "Événement supprimé", "L'événement a été supprimé avec succès.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur de suppression", "Impossible de supprimer l'événement: " + e.getMessage());
            }
        }
    }

    @FXML
    void viewDetailsParticipant(participant participant) {
        if (participant != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ParticipantDetails.fxml"));
                Parent root = loader.load();

                DetailParticipantBackController detailsController = loader.getController();
                detailsController.initData(participant);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Détails du participant");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir les détails", e.getMessage());
            }
        }
    }

    @FXML
    void deleteParticipant(participant participant) {
        if (participant != null) {
            try {
                if (showConfirmation("Confirmer la suppression",
                        "Supprimer le participant",
                        "Êtes-vous sûr de vouloir supprimer le participant '" + participant.getNom() + "' ?")) {

                    participantService service = new participantService();
                    service.deleteParticipant(participant.getId());
                    refreshParticipantTable();

                    showAlert("Succès", "Participant supprimé", "Le participant a été supprimé avec succès.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur de suppression", "Impossible de supprimer le participant: " + e.getMessage());
            }
        }
    }

    private void refreshEventTable() {
        try {
            evenementService eventService = new evenementService();
            List<evenement> evenements = eventService.getAllevenement();
            ObservableList<evenement> observableList = FXCollections.observableArrayList(evenements);
            evenementTable.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshParticipantTable() {
        try {
            participantService participantService = new participantService();
            List<participant> participants = participantService.getAllparticipant();
            ObservableList<participant> observableList = FXCollections.observableArrayList(participants);
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
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
            localisationCol.setCellValueFactory(new PropertyValueFactory<>("localisation"));
            date_dCol.setCellValueFactory(new PropertyValueFactory<>("date_d"));
            recomponseCol.setCellValueFactory(new PropertyValueFactory<>("recomponse"));

            // Set up columns participant
            nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
            ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
            emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
            telephoneCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));
            envIdCol.setCellValueFactory(new PropertyValueFactory<>("env_id"));

            // Setup action columns
            setupActionColumns();

            // Load data evenement
            refreshEventTable();
            System.out.println("Event table initialized successfully.");

            // Load data participant
            refreshParticipantTable();
            System.out.println("Participant table initialized successfully.");

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
                ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/edit.png")));
                editIcon.setFitHeight(16);
                editIcon.setFitWidth(16);
                editBtn.setGraphic(editIcon);
                editBtn.setStyle("-fx-background-color: transparent;");
                editBtn.setOnAction(event -> {
                    evenement eventData = getTableView().getItems().get(getIndex());
                    editItem(eventData);
                });

                // Delete button setup
                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/trash.png")));
                deleteIcon.setFitHeight(16);
                deleteIcon.setFitWidth(16);
                deleteBtn.setGraphic(deleteIcon);
                deleteBtn.setStyle("-fx-background-color: transparent;");
                deleteBtn.setOnAction(event -> {
                    evenement eventData = getTableView().getItems().get(getIndex());
                    deleteItem(eventData);
                });

                // View button setup
                ImageView viewIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/eye.png")));
                viewIcon.setFitHeight(16);
                viewIcon.setFitWidth(16);
                viewBtn.setGraphic(viewIcon);
                viewBtn.setStyle("-fx-background-color: transparent;");
                viewBtn.setOnAction(event -> {
                    evenement eventData = getTableView().getItems().get(getIndex());
                    viewDetails(eventData);
                });

                buttons.getChildren().addAll(viewBtn, editBtn, deleteBtn);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });

        // Setup for participant table
        actionCol2.setCellFactory(param -> new TableCell<>() {
            private final HBox buttons = new HBox(5);
            private final Button editBtn = new Button();
            private final Button deleteBtn = new Button();
            private final Button viewBtn = new Button();

            {
                // Styling buttons
                buttons.setAlignment(Pos.CENTER);

                // Edit button setup
                ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/edit.png")));
                editIcon.setFitHeight(16);
                editIcon.setFitWidth(16);
                editBtn.setGraphic(editIcon);
                editBtn.setStyle("-fx-background-color: transparent;");
                editBtn.setOnAction(event -> {
                    participant participantData = getTableView().getItems().get(getIndex());
                    editParticipant(participantData);
                });

                // Delete button setup
                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/trash.png")));
                deleteIcon.setFitHeight(16);
                deleteIcon.setFitWidth(16);
                deleteBtn.setGraphic(deleteIcon);
                deleteBtn.setStyle("-fx-background-color: transparent;");
                deleteBtn.setOnAction(event -> {
                    participant participantData = getTableView().getItems().get(getIndex());
                    deleteParticipant(participantData);
                });

                // View button setup
                ImageView viewIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/eye.png")));
                viewIcon.setFitHeight(16);
                viewIcon.setFitWidth(16);
                viewBtn.setGraphic(viewIcon);
                viewBtn.setStyle("-fx-background-color: transparent;");
                viewBtn.setOnAction(event -> {
                    participant participantData = getTableView().getItems().get(getIndex());
                    viewDetailsParticipant(participantData);
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
    void editItem(evenement event) {
        if (event != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditEventBack.fxml"));
                Parent root = loader.load();

                EditEventBackController editController = loader.getController();
                editController.setEventData(event);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Événement");
                stage.showAndWait();

                refreshEventTable();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification", e.getMessage());
            }
        } else {
            showAlert("Aucune sélection", "Aucun événement sélectionné", "Veuillez sélectionner un événement à modifier.");
        }
    }

    @FXML
    void editParticipant(participant participant) {
        if (participant != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditParticipantBack.fxml"));
                Parent root = loader.load();

                EditParticipantBackController editController = loader.getController();
                editController.setParticipantData(participant);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Participant");
                stage.showAndWait();

                refreshParticipantTable();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification", e.getMessage());
            }
        } else {
            showAlert("Aucune sélection", "Aucun participant sélectionné", "Veuillez sélectionner un participant à modifier.");
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
}