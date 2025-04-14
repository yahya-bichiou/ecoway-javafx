package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import models.Livraisons;
import services.CommandeService;
import services.LivraisonService;

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
    private TableView<Livraisons> livraisonTableView;
    @FXML
    private TableColumn<Livraisons, Integer> id;
    @FXML
    private TableColumn<Livraisons, Integer> commande;
    @FXML
    private TableColumn<Livraisons, String> livreur;
    @FXML
    private TableColumn<Livraisons, String> adresse;
    @FXML
    private TableColumn<Livraisons, LocalDate> date;
    @FXML
    private TableColumn<Livraisons, String> status;
    @FXML
    private TableColumn<Livraisons, String> mode;
    @FXML
    private TableColumn<Livraisons, Float> prix;

    @FXML
    private Button closeButton;

    @FXML
    void closeApp() {
        Platform.exit();
    }

    @FXML
    void addCommande() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddCommande.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteCommande() {
        Commandes selectedCommande = commandeTableView.getSelectionModel().getSelectedItem();
        if (selectedCommande != null) {
            CommandeService commandeService = new CommandeService();
            commandeService.delete(selectedCommande);
            commandeTableView.getItems().remove(selectedCommande);
            System.out.println("Commande deleted successfully!");
        } else {
            System.out.println("Please select a commande to delete.");
        }
    }

    @FXML
    void editCommande() {
        Commandes selectedCommande = commandeTableView.getSelectionModel().getSelectedItem();

        if (selectedCommande != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditCommande.fxml"));
                Parent root = loader.load();
                EditCommande editController = loader.getController();
                editController.setCommande(selectedCommande);
                Scene scene = new Scene(root);
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.setScene(scene);
                stage.setFullScreen(true);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Please select a commande to edit.");
        }
    }

    @FXML
    public void initialize() {
        System.out.println("Initializing commande table...");
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

        System.out.println("Initializing livraison table...");
        try {
            // Set up columns
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            commande.setCellValueFactory(new PropertyValueFactory<>("commande_id"));
            livreur.setCellValueFactory(new PropertyValueFactory<>("livreur"));
            adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            status.setCellValueFactory(new PropertyValueFactory<>("status"));
            mode.setCellValueFactory(new PropertyValueFactory<>("mode"));
            prix.setCellValueFactory(new PropertyValueFactory<>("prix"));

            // Load data
            LivraisonService cs = new LivraisonService();
            List<Livraisons> livraisons = cs.getAll();
            ObservableList<Livraisons> observableList2 = FXCollections.observableArrayList(livraisons);
            livraisonTableView.setItems(observableList2);

            System.out.println("Table initialized successfully.");

        } catch (Exception e) {
            System.out.println("ERROR during table initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void livrerCommande(ActionEvent actionEvent) {
        Commandes selectedCommande = commandeTableView.getSelectionModel().getSelectedItem();

        if (selectedCommande != null) {
            selectedCommande.setStatus("en livraison");
            CommandeService commandeService = new CommandeService();
            commandeService.update(selectedCommande);
            commandeTableView.refresh();
        } else {
            System.out.println("Please select a commande to mark as 'en livraison'.");
        }    }
}
