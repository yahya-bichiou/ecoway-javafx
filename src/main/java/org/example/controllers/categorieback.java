package org.example.controllers;

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
import org.example.models.categorie;
import org.example.services.categorieservice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class categorieback {

    @FXML
    private TableView<categorie> categorieTableView;
    @FXML
    private TableColumn<categorie, Integer> IdCol;
    @FXML
    private TableColumn<categorie, String> nomCol;
    @FXML
    private TableColumn<categorie, String> descriptionCol;
    @FXML
    private Button closeButton;

    // Méthode pour ajouter une catégorie
    @FXML
    void addCategorie() {
        try {
            // Charger la fenêtre d'ajout de catégorie
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/categorieback.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour fermer l'application
    @FXML
    void closeApp() {
        Platform.exit();
    }

    @FXML
    public void initialize() throws SQLException {
        // Configuration des colonnes du tableau
        IdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Charger les catégories depuis la base de données
        categorieservice service = new categorieservice();
        List<categorie> categories = service.getAll();
        ObservableList<categorie> observableList = FXCollections.observableArrayList(categories);
        categorieTableView.setItems(observableList);

    }
}
