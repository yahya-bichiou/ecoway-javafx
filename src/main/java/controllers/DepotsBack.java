package controllers;

import jakarta.mail.MessagingException;
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
import models.Collectes;
import models.Depots;
import services.CollectesServices;
import services.DepotsServices;
import services.MailSender;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class DepotsBack {

    @FXML
    private TableView<Depots> depotsTableView;
    @FXML
    private TableColumn<Depots, Integer> idCol;
    @FXML
    private TableColumn<Depots, String> nomCol;
    @FXML
    private TableColumn<Depots, String> adresseCol;
    @FXML
    private TableColumn<Depots, Integer> capaciteCol;
    @FXML
    private TableColumn<Depots, String> imageCol;
    @FXML
    private TableView<Collectes> collectesTableView;
    @FXML
    private TableColumn<Collectes, Integer> idCol1;
    @FXML
    private TableColumn<Collectes, Integer> depot_idCol;
    @FXML
    private TableColumn<Collectes, Double> quantiteCol;
    @FXML
    private TableColumn<Collectes, Date> dateCol;
    @FXML
    private TableColumn<Collectes, String> responsableCol;
    @FXML
    private Button closeButton;

    @FXML
    void closeApp() {
        Platform.exit();
    }


    @FXML
    void addDepot() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddDepots.fxml"));
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
    void deleteDepot() {
        Depots selectedDepot = depotsTableView.getSelectionModel().getSelectedItem();
        if (selectedDepot != null) {
            DepotsServices depotService = new DepotsServices();
            depotService.delete(selectedDepot);
            depotsTableView.getItems().remove(selectedDepot);
            CollectesServices cs = new CollectesServices();
            Collectes toDelete = null;
            for (Collectes l : collectesTableView.getItems()) {
                if (l.getDepot_id() == selectedDepot.getId()) {
                    toDelete = l;
                    break;
                }
            }
            cs.delete(toDelete);
            collectesTableView.getItems().remove(toDelete);
            System.out.println("Depot deleted successfully!");
        } else {
            System.out.println("Please select a Depot to delete.");
        }
    }
    @FXML
    void editDepot() {
        Depots selectedDepot = depotsTableView.getSelectionModel().getSelectedItem();

        if (selectedDepot != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditDepots.fxml"));
                Parent root = loader.load();
                EditDepot editController = loader.getController();
                editController.setDepots(selectedDepot);
                Scene scene = new Scene(root);
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.setScene(scene);
                stage.setFullScreen(true);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Please select a depot to edit.");
        }
    }

    @FXML
    void editCollecte() {
        Collectes selectedCollecte = collectesTableView.getSelectionModel().getSelectedItem();

        if (selectedCollecte != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditCollectes.fxml"));
                Parent root = loader.load();
                EditCollecte editController = loader.getController();
                editController.setCollectes(selectedCollecte);
                Scene scene = new Scene(root);
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.setScene(scene);
                stage.setFullScreen(true);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Please select a collecte to edit.");
        }
    }

    @FXML
    void deleteCollecte() {
        Collectes selectedCollecte = collectesTableView.getSelectionModel().getSelectedItem();
        if (selectedCollecte != null) {
            CollectesServices CollecteService = new CollectesServices();
            CollecteService.delete(selectedCollecte);
            collectesTableView.getItems().remove(selectedCollecte);
            System.out.println("Collecte deleted successfully!");
        } else {
            System.out.println("Please select a Collecte to delete.");
        }
    }

    @FXML
    public void initialize() throws MessagingException, SQLException {
        System.out.println("Initializing table...");
        //mail
        DepotsServices ds = new DepotsServices();
        List<Depots> depotList = ds.select();
        for (Depots depot : depotList) {
            int reste = depot.getCapacite();
            CollectesServices cs = new CollectesServices();
            List<Collectes> collecteList = cs.select();
            for (Collectes collecte : collecteList) {
                if (collecte.getDepot_id() == depot.getId()) {
                    reste -= collecte.getQuantite();
                }
            }
            System.out.println(reste);
            if (reste <= 0)
            {
                MailSender mailSender = new MailSender();
                mailSender.sendMail("yahyabichiou2003@gmail.com",depot.getId());
            }
        }

        try {
            nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
            adresseCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
            capaciteCol.setCellValueFactory(new PropertyValueFactory<>("capacite"));
            imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));

            // Load data
            DepotsServices cs = new DepotsServices();
            List<Depots> depots = cs.select();
            ObservableList<Depots> observableList = FXCollections.observableArrayList(depots);
            depotsTableView.setItems(observableList);

            System.out.println("Table initialized successfully.");

        } catch (Exception e) {
            System.out.println("ERROR during table initialization: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            depot_idCol.setCellValueFactory(new PropertyValueFactory<>("depot_id"));
            quantiteCol.setCellValueFactory(new PropertyValueFactory<>("quantite"));
            dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            responsableCol.setCellValueFactory(new PropertyValueFactory<>("responsable"));

            // Load data
            CollectesServices cs = new CollectesServices();
            List<Collectes> collecte = cs.select();
            ObservableList<Collectes> observableList = FXCollections.observableArrayList(collecte);
            collectesTableView.setItems(observableList);

            System.out.println("Table initialized successfully.");

        } catch (Exception e) {
            System.out.println("ERROR during table initialization: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @FXML
    void commandePageOpen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CommandesBack.fxml"));
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
}
