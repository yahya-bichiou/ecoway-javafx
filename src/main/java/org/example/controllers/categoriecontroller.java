package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import org.example.models.categorie;
import org.example.services.categorieservice;

import java.io.IOException;
import java.sql.SQLException;

public class categoriecontroller {
    @FXML
    private TableColumn<categorie, Integer> IdCol;
    @FXML
    private TableColumn<categorie, String> nomCol;
    @FXML
    private TableColumn<categorie, String> descriptionCol;
    @FXML
    private Button closeButton;

    @FXML
    private TableView<categorie> categorieTableView;
    @FXML
    private TextField nomTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField idTextField;  // Champ pour l'ID de la catégorie (si nécessaire)

    // Méthode pour ajouter une catégorie
    @FXML
    void ajoutercategorieAction(ActionEvent event) throws SQLException {
        String nom = nomTextField.getText();
        String description = descriptionTextField.getText();

        if (nom.isEmpty() || description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setHeaderText("Veuillez remplir tous les champs !");
            alert.show();
            return;
        }

        categorie cat = new categorie(nom, description);
        categorieservice service = new categorieservice();

        service.addcategorie(cat);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Catégorie ajoutée");
        alert.setHeaderText("Catégorie ajoutée avec succès !");
        alert.show();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/categorieback.fxml"));
        try {
            Parent root = loader.load();
            detaillecategorie detail = loader.getController();
            detail.setNomTextField(nomTextField.getText());
            detail.setDescriptionTextField(descriptionTextField.getText());
            nomTextField.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("Erreur ajoutercategorie: " + e.getMessage());
        }
    }

    // Méthode pour modifier une catégorie

    @FXML
    void modifierCategorieAction(ActionEvent event) throws SQLException {
        String nom = nomTextField.getText();
        String description = descriptionTextField.getText();
        int id = Integer.parseInt(idTextField.getText());  // Récupérer l'ID de la catégorie

        if (nom.isEmpty() || description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setHeaderText("Veuillez remplir tous les champs !");
            alert.show();
            return;
        }

        categorie cat = new categorie(id, nom, description);  // On crée l'objet catégorie avec l'ID
        categorieservice service = new categorieservice();

        service.updatecategorie(cat);  // Méthode de mise à jour dans le service
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Catégorie modifiée");
        alert.setHeaderText("Catégorie modifiée avec succès !");
        alert.show();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailCategorie.fxml"));
        try {
            Parent root = loader.load();
            detaillecategorie detail = loader.getController();
            detail.setNomTextField(nomTextField.getText());
            detail.setDescriptionTextField(descriptionTextField.getText());
            nomTextField.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("Erreur modifierCategorie: " + e.getMessage());
        }
    }

  /*  @FXML
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

    }*/
}

   /* @FXML
    private void supprimercategorieAction(ActionEvent event) {
        try {
            // Vérifier qu'une ligne est sélectionnée dans le tableau
            categorie selectedCategorie = categorieTableView.getSelectionModel().getSelectedItem();
            if (selectedCategorie == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aucune sélection");
                alert.setHeaderText("Veuillez sélectionner une catégorie à supprimer !");
                alert.show();
                return;
            }

            // Confirmation de suppression
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation de suppression");
            confirmation.setHeaderText("Êtes-vous sûr de vouloir supprimer cette catégorie ?");
            confirmation.setContentText("Catégorie : " + selectedCategorie.getNom());

            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    categorieservice service = new categorieservice();
                    service.deletecategorie(getId);
                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Catégorie supprimée");
                    success.setHeaderText("La catégorie a été supprimée avec succès !");
                    success.show();

                    // Rafraîchir la vue
                    categorieTableView.getItems().remove(selectedCategorie);
                    refreshView();

                }
            });
        } catch (Exception e) {
            System.out.println("Erreur supprimercategorieAction: " + e.getMessage());
        }
    }*/

