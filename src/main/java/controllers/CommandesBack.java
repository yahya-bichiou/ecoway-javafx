package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Commandes;
import services.CommandeService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CommandesBack {

    @FXML
    private TableView<Commandes> commandeTableView;
    @FXML
    private TableColumn<Commandes, Integer> idCol;
    @FXML
    private TableColumn<Commandes, Integer> clientIdCol;
    @FXML
    private TableColumn<Commandes, String> statusCol;
    @FXML
    private TableColumn<Commandes, String> modeCol;
    @FXML
    private TableColumn<Commandes, LocalDate> dateCol;
    @FXML
    private TableColumn<Commandes, Float> prixCol;
    @FXML
    private TableColumn<Commandes, String> produitsCol;
    @FXML
    private Button closeButton;

    @FXML
    void closeApp() {
        Platform.exit();
    }

    @FXML
    void addCommande() {
        try {
            // Load the AddCommande FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddCommande.fxml"));
            Parent root = loader.load();

            // Create a new Scene with the loaded root node
            Scene scene = new Scene(root);

            // Get the current stage (window)
            Stage stage = (Stage) closeButton.getScene().getWindow();

            // Set the new scene
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        System.out.println("Initializing table...");

        try {
            // Set up columns
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            clientIdCol.setCellValueFactory(new PropertyValueFactory<>("client_id"));
            statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
            modeCol.setCellValueFactory(new PropertyValueFactory<>("mode_paiement"));
            dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));
            produitsCol.setCellValueFactory(new PropertyValueFactory<>("produits"));

            // Load data
            CommandeService cs = new CommandeService();
            List<Commandes> commandes = cs.select();
            ObservableList<Commandes> observableList = FXCollections.observableArrayList(commandes);
            commandeTableView.setItems(observableList);

            System.out.println("Table initialized successfully.");

        } catch (Exception e) {
            System.out.println("ERROR during table initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
