package org.example.controller;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import org.example.model.evenement;
import org.example.service.evenementService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class listEventFrontController {
    @FXML private FlowPane eventsContainer;
    @FXML private VBox addForm;
    @FXML private TextField titreField;
    @FXML private TextField descriptionField;
    @FXML private TextField contactField;
    @FXML private TextField localisationField;
    @FXML private TextField date_dField;
    @FXML private TextField idField;
    @FXML private TextField recomponseField;

    private evenementService service;



    public listEventFrontController() throws SQLException {
        service = new evenementService();
    }

    @FXML
    public void initialize() {
        loadEvents();
    }

    private void loadEvents() {
        eventsContainer.getChildren().clear();
        try {
            List<evenement> events = service.getAllevenement();
            for (evenement event : events) {
                eventsContainer.getChildren().add(createEventCard(event));
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Échec du chargement des événements: " + e.getMessage());
        }
    }
    @FXML
    private void showEventDetails(evenement event) {
        try {

            // 2. Chargement du FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailFrontEvent.fxml"));
            Parent root = loader.load();

            // 3. Initialisation du contrôleur
            DetailEventFrontController controller = loader.getController();
            if (controller == null) {
                throw new IOException("Le contrôleur n'a pas été initialisé dans le FXML");
            }

            // 4. Passage des données - version corrigée avec un seul argument
            controller.initData(event);

            // 5. Changement de vue
            Stage currentStage = (Stage) eventsContainer.getScene().getWindow();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.sizeToScene();

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
    private void hideAddForm() {
        addForm.setVisible(false);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}