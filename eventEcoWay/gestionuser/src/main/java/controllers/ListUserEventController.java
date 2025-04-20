package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.evenement;
import models.user;
import services.evenementService;
import services.participantService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListUserEventController {
    /*@FXML private FlowPane eventsContainer;
    @FXML private FlowPane userEventsContainer;
    @FXML private Label userEmailLabel;

    private evenementService eventService = new evenementService();
    private participantService participantService = new participantService();
    @FXML private Label userNameLabel;// teba3 user
    private user loggedInUser;


    public ListUserEventController() throws SQLException {
        eventService = new evenementService();}

    public void initData(String userEmail) {
        if (userEmailLabel != null) {
            userEmailLabel.setText("Événements de: " + userEmail);
        }
        loadUserEvents(userEmail);
    }

    private void loadUserEvents(String userEmail) {
        try {
            // 1. Récupérer les IDs des événements où l'utilisateur est participant
            List<Integer> eventIds = participantService.getEventIdsByUserEmail(userEmail);

            // 2. Récupérer les événements correspondants
            List<evenement> userEvents = eventService.getEventsByIds(eventIds);

            // 3. Afficher les événements (similaire à listEventFrontController)
            displayEvents(userEvents);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'erreur
        }
    }

    private void displayEvents(List<evenement> events) {
        userEventsContainer.getChildren().clear();
        try {
            //events = eventService.getAllHistorique(String.valueOf(userNameLabel));
            for (evenement event : events) {
                eventsContainer.getChildren().add(createEventCard(event));
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Échec du chargement des événements: " + e.getMessage());
        }
    }

    /*private void displayEvents(List<evenement> events) {
        userEventsContainer.getChildren().clear(); // Utiliser userEventsContainer ici
        for (evenement event : events) {
            userEventsContainer.getChildren().add(createEventCard(event));
        }
    }

    //card
    private VBox createEventCard(evenement event) {
        VBox card = new VBox();
        card.setSpacing(8);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-border-color: #4CAF50; -fx-border-width: 1px; -fx-border-radius: 5px;");
        card.setPrefWidth(300);

        // Titre
        Text title = new Text(event.getTitre());
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // localisation
        HBox addressBox = new HBox(5);
        Text addressLabel = new Text("Adresse: ");
        addressLabel.setStyle("-fx-font-weight: bold;");
        Text addressValue = new Text(event.getLocalisation());
        addressBox.getChildren().addAll(addressLabel, addressValue);

        // Récompense
        HBox rewardBox = new HBox(5);
        Text rewardLabel = new Text("Récompense: ");
        rewardLabel.setStyle("-fx-font-weight: bold;");
        Text rewardValue = new Text(String.valueOf(event.getRecomponse()));
        rewardBox.getChildren().addAll(rewardLabel, rewardValue);

        //détail button
        Button detailButton = new Button("Voir détail");
        detailButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        detailButton.setOnAction(e -> showEventDetails(event));

        card.getChildren().addAll(title, addressBox, rewardBox, detailButton);
        return card;
    }

    @FXML
    private void showEventDetails(evenement event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailFrontEvent.fxml"));
            Parent root = loader.load();

            DetailEventFrontController controller = loader.getController();
            if (controller == null) {
                throw new IOException("Le contrôleur n'a pas été initialisé dans le FXML");
            }

            // Passer à la fois l'événement et l'utilisateur connecté
            controller.initData(event, loggedInUser);

            // Créer une nouvelle fenêtre au lieu d'utiliser la fenêtre actuelle
            Stage detailStage = new Stage();
            detailStage.setScene(new Scene(root));
            detailStage.setTitle("Détails de l'événement");

            // Optionnel: définir le propriétaire pour positionnement relatif
            detailStage.initOwner(eventsContainer.getScene().getWindow());

            // Empêcher la fermeture de la fenêtre principale
            detailStage.initModality(Modality.WINDOW_MODAL); // ou Modality.NONE pour une interaction complète

            detailStage.show();

        } catch (IOException e) {
            System.err.println("Échec du chargement de la vue de détail:");
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de navigation");
            alert.setHeaderText("Impossible d'afficher les détails");
            alert.setContentText("Erreur technique: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        // Récupérer la fenêtre actuelle à partir du bouton ou d'un autre nœud
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }*/


}