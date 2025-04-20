package controllers;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.evenement;
import services.evenementService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import models.user;//teb3 sesion user

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



    @FXML private Label userNameLabel;// teba3 user
    @FXML private Label userEmailLabel;// teba3 user
    private user loggedInUser; // teba3 user
    private evenementService service;



    public listEventFrontController() throws SQLException {
        service = new evenementService();
    }

    //Fonction teba3 user
    public void setLoggedInUser(user user) {
        this.loggedInUser = user;
        if (userNameLabel != null && userEmailLabel != null) {
            userNameLabel.setText(user.getName());
            userEmailLabel.setText(user.getEmail());
        }
    }

    public void initData( user loggedInUser) {
        // Stocker les données
        this.loggedInUser = loggedInUser;

        // Afficher le nom de l'utilisateur connecté
        if (loggedInUser != null && userNameLabel != null && userEmailLabel != null) {
            userNameLabel.setText(loggedInUser.getName());
            userEmailLabel.setText(loggedInUser.getEmail());
        } else {
            System.err.println("Erreur: Utilisateur non connecté ou label non initialisé");
        }
    }

    @FXML
    public void initialize() {
        loadEvents();
        //teba3 user
        if (loggedInUser != null && userNameLabel != null  && userEmailLabel != null) {
            userNameLabel.setText(loggedInUser.getName());
            userEmailLabel.setText(loggedInUser.getEmail());
        }
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
    /*@FXML
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
    }*/

    // bech ne5ou el Name fil detaile
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
    private void navigateToUserEvents() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListUserEvent.fxml"));
            Parent root = loader.load();

            ListUserEventController controller = loader.getController();
            if (controller == null) {
                throw new IOException("Le contrôleur n'a pas été initialisé dans le FXML");
            }

            // Passer à la fois l'événement et l'utilisateur connecté
            controller.initData( loggedInUser);

            Stage stage = (Stage) eventsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Mes Événements");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page des événements utilisateur");
        }
    }
   /*
    // el historique mta3 el participant
    @FXML
    private void navigateToUserEvents() {
        if (loggedInUser != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListUserEvent.fxml"));
                Parent root = loader.load();

                ListUserEventController controller = loader.getController();
                controller.initData(loggedInUser.getEmail()); // Passer l'email de l'utilisateur

                Stage stage = (Stage) eventsContainer.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Mes Événements");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible de charger la page des événements utilisateur");
            }
        }
    }*/

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


    /*//Nzidhom ya fil login wala tamplate controller :
    @FXML
    private void handleLoginAction() {
        resetError();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (!validateInputs(email, password)) {
            return;
        }

        if (userService.validateCredentials(email, password)) {
            user loggedInUser = userService.getUserByEmail(email);
            navigateToProfilePage(loggedInUser);
        } else {
            showError("Incorrect email or password.");
            shakeField(emailField);
        }
    }

    private void navigateToProfilePage(user loggedInUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/listEventFront.fxml"));
            Parent root = loader.load();

            listEventFrontController controller = loader.getController();
            controller.setLoggedInUser(loggedInUser);

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Back Office - User Management");
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load the user management page. Please try again.");
        }
    }
*/
}