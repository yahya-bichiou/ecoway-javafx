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
import models.evenement;
import services.evenementService;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class listEventBackController {

    @FXML private TableView<evenement> evenementTable;
    @FXML private TableColumn<evenement, Integer> idCol;
    @FXML private TableColumn<evenement, String> titreCol;
    @FXML private TableColumn<evenement, String> descriptionCol;
    @FXML private TableColumn<evenement, String> contactCol;
    @FXML private TableColumn<evenement, String> localisationCol;
    @FXML private TableColumn<evenement, String> date_dCol;
    @FXML private TableColumn<evenement, Integer> recomponseCol;
    @FXML private TableColumn<evenement, Void> actionCol;
    @FXML private Button closeButton;
    @FXML private TextField searchField;
    @FXML private Button sortDateAscBtn;
    @FXML private Button sortDateDescBtn;

    @FXML
    public void initialize() {
        try {
            // Configuration des colonnes
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
            localisationCol.setCellValueFactory(new PropertyValueFactory<>("localisation"));
            date_dCol.setCellValueFactory(new PropertyValueFactory<>("date_d"));
            recomponseCol.setCellValueFactory(new PropertyValueFactory<>("recomponse"));

            // Configuration de la recherche
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                handleSearch();
            });

            // Configuration des boutons de tri
            sortDateAscBtn.setOnAction(e -> sortByDateAscending());
            sortDateDescBtn.setOnAction(e -> sortByDateDescending());

            // Configuration du tri sur la colonne date
            /*date_dCol.setSortable(true);
            date_dCol.setComparator(LocalDate::compareTo);*/

            // Chargement des données
            refreshEventTable();

            // Configuration des boutons d'action
            setupActionColumns();

        } catch (Exception e) {
            showAlert("Erreur", "Erreur d'initialisation: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void sortByDateAscending() {
        evenementTable.getSortOrder().clear();
        date_dCol.setSortType(TableColumn.SortType.ASCENDING);
        evenementTable.getSortOrder().add(date_dCol);
        evenementTable.sort();
    }

    @FXML
    private void sortByDateDescending() {
        evenementTable.getSortOrder().clear();
        date_dCol.setSortType(TableColumn.SortType.DESCENDING);
        evenementTable.getSortOrder().add(date_dCol);
        evenementTable.sort();
    }

    @FXML
    void closeApp() {
        Platform.exit();
    }

    @FXML
    void addevenement() {
        try {
            /*// Fermer la fenêtre actuelle
            Stage currentStage = (Stage) evenementTable.getScene().getWindow();*/

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddEventBack.fxml"));
            Parent root = loader.load();

            AddEventBackController addController = loader.getController();
            addController.setOnAddSuccessCallback(() -> {
                refreshEventTable();
            });

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Événement");
            stage.show();

            //currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir le formulaire d'ajout", AlertType.ERROR);
        }
    }

    @FXML
    void viewDetails(evenement event) {
        if (event != null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailBackEvent.fxml"));
                Parent root = loader.load();

                DetailEventBackController detailsController = loader.getController();
                detailsController.initData(event);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Détails de l'événement");
                stage.show();


            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir les détails", AlertType.ERROR);
            }
        }
    }

    @FXML
    void showParticipants() throws IOException {
        // Fermer la fenêtre actuelle
        Stage currentStage = (Stage) evenementTable.getScene().getWindow();

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
        Stage currentStage = (Stage) evenementTable.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/listEventBack.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Liste des événements");
        stage.show();

        currentStage.close();
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

                    showAlert("Succès", "Événement supprimé avec succès", AlertType.INFORMATION);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur de suppression: " + e.getMessage(), AlertType.ERROR);
            }
        }
    }

    private void refreshEventTable() {
        try {
            evenementService eventService = new evenementService();
            List<evenement> evenements = eventService.getAllevenement();
            ObservableList<evenement> observableList = FXCollections.observableArrayList(evenements);
            evenementTable.setItems(observableList);
            evenementTable.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les événements: " + e.getMessage(), AlertType.ERROR);
        }
    }




    private void setupActionColumns() {
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final HBox buttons = new HBox(5);
            private final Button editBtn = new Button();
            private final Button deleteBtn = new Button();
            private final Button viewBtn = new Button();

            {
                buttons.setAlignment(Pos.CENTER);
                buttons.setSpacing(5);

                // Configuration du bouton Modifier
                configureActionButton(editBtn, "/assets/edit.png", event -> {
                    evenement eventData = getTableView().getItems().get(getIndex());
                    editItem(eventData);
                });

                // Configuration du bouton Supprimer
                configureActionButton(deleteBtn, "/assets/trash.png", event -> {
                    evenement eventData = getTableView().getItems().get(getIndex());
                    deleteItem(eventData);
                });

                // Configuration du bouton Voir
                configureActionButton(viewBtn, "/assets/eye.png", event -> {
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
    }

    private void configureActionButton(Button button, String iconPath, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        icon.setFitHeight(16);
        icon.setFitWidth(16);
        button.setGraphic(icon);
        button.setStyle("-fx-background-color: transparent;");
        button.setOnAction(handler);
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
                stage.show();



                // Rafraîchir après modification
                stage.setOnHidden(e -> refreshEventTable());

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir l'éditeur: " + e.getMessage(), AlertType.ERROR);
            }
        } else {
            showAlert("Aucune sélection", "Veuillez sélectionner un événement à modifier", AlertType.WARNING);
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
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

    //rechercher :

    // Ajoutez cette méthode pour gérer la recherche
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim();
        try {
            evenementService eventService = new evenementService();
            List<evenement> events;

            if (searchText.isEmpty()) {
                events = eventService.getAllevenement();
            } else {
                events = eventService.searchByTitre(searchText);
            }

            ObservableList<evenement> observableList = FXCollections.observableArrayList(events);
            evenementTable.setItems(observableList);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la recherche: " + e.getMessage(), AlertType.ERROR);
        }
    }


    private void setupSearchListener() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                refreshSearchResults();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de la recherche", AlertType.ERROR);
            }
        });
    }

    private void refreshSearchResults() throws SQLException {
        String searchText = searchField.getText().trim();
        evenementService eventService = new evenementService();
        List<evenement> events;

        if (searchText.isEmpty()) {
            events = eventService.getAllevenement();
        } else {
            events = eventService.searchByTitre(searchText);
        }

        ObservableList<evenement> observableList = FXCollections.observableArrayList(events);
        evenementTable.setItems(observableList);
        evenementTable.refresh(); // Ajout important pour forcer le rafraîchissement
    }
    //tri
}