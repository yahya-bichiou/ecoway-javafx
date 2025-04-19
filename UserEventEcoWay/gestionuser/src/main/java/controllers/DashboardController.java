package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Label welcomeMessage;

    public void setWelcomeMessage(String message) {
        welcomeMessage.setText(message);
    }

    @FXML
    private void openProfile() {
        System.out.println("Navigating to Profile...");
        // Logic to switch to Profile page
    }

    @FXML
    private void openSettings() {
        System.out.println("Navigating to Settings...");
        // Logic to switch to Settings page
    }

    @FXML
    private void logout() {
        System.out.println("Logging out...");
        // Logic to return to Login page
    }
}
