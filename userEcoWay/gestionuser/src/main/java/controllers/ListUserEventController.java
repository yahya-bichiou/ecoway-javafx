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
import javafx.stage.Stage;
import models.evenement;
import models.user;
import services.evenementService;
import services.participantService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListUserEventController {
    @FXML private FlowPane eventsContainer;
    @FXML private Label userNameLabel;
    @FXML private Label userEmailLabel;

    private user loggedInUser;
    private evenementService eventService;
    private participantService participantService;

    public ListUserEventController() throws SQLException {
        eventService = new evenementService();
        participantService = new participantService();
    }

    public void initData(user user) {
        this.loggedInUser = user;
        if (userNameLabel != null && userEmailLabel != null) {
            userNameLabel.setText(user.getName());
            userEmailLabel.setText(user.getEmail());
        }
        loadUserEvents();
    }

    private void loadUserEvents() {
        eventsContainer.getChildren().clear();
        try {
            // 1. Récupérer les IDs des événements où l'utilisateur est participant
            List<Integer> eventIds = participantService.getEventIdsByUserEmail(loggedInUser.getEmail());

            // 2. Récupérer les événements complets correspondants
            List<evenement> userEvents = eventService.getEventsByIds(eventIds);

            // 3. Afficher les événements (utilisez votre méthode createEventCard existante)
            for (evenement event : userEvents) {
                eventsContainer.getChildren().add(createEventCard(event));
            }

            if (userEvents.isEmpty()) {
                showAlert("Information", "Vous n'êtes inscrit à aucun événement pour le moment.");
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Échec du chargement des événements: " + e.getMessage());
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

        /*//détail button
        Button detailButton = new Button("Voir détail");
        detailButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        detailButton.setOnAction(e -> showEventDetails(event));*/

        card.getChildren().addAll(title, addressBox, rewardBox); //detailButton);
        return card;
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
    }
}