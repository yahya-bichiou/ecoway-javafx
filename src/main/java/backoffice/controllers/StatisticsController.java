package backoffice.controllers;

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
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.user;
import services.userService;

import java.io.IOException;
import java.util.List;

public class StatisticsController {

    @FXML
    private ImageView profileImageView;
    @FXML
    private Label adminNameLabel;
    @FXML
    private MenuButton adminMenu;
    @FXML
    private Button closeButton;

    @FXML
    private PieChart userStatusChart;

    @FXML
    public void initialize() {
        loadUserStatusChart();
    }

    private void loadUserStatusChart() {
        int blockedCount = 0;
        int activeCount = 0;

        userService service = new userService();
        List<user> users = service.getAll();

        for (user u : users) {
            if (u.isBlocked()) {
                blockedCount++;
            } else {
                activeCount++;
            }
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Blocked", blockedCount),
                new PieChart.Data("Not Blocked", activeCount)
        );

        userStatusChart.setData(pieChartData);
        userStatusChart.setLabelsVisible(true);
    }

    @FXML
    void closeApp() {
        Platform.exit();
    }

    @FXML
    public void goToStatistics(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/backofice/fxml/Statistics.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void handleLogout() {
        UserSession.clearSession();
        redirectToLogin();
    }

    private void redirectToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontoffices/fxml/login.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();


            Stage current = (Stage) closeButton.getScene().getWindow();
            current.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}