package org.example;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
/*public class homecontroller extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    // Lancement du backoffice avec la vue catégorie principale
    @Override
    public void start(Stage primaryStage) {
        try {
           Parent root = FXMLLoader.load(getClass().getResource("/back.fxml"));
            //Parent root = FXMLLoader.load(getClass().getResource("/produitfront.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("BackOffice - Gestion des Catégories et Produits");
            primaryStage.setScene(scene);
            primaryStage.setFullScreen(true); // facultatif
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de categorieback.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }
   // Méthode appelée quand on clique sur "Ajouter un Produit"
    @FXML
    private void ouvrirFormulaireProduit(ActionEvent event) {
        try {
            Parent produitRoot = FXMLLoader.load(getClass().getResource("/produitform.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Ajouter un Produit");
            stage.setScene(new Scene(produitRoot));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

            // Fermer l'accueil si tu veux
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'ouverture du formulaire produit : " + e.getMessage());
        }
    }


    // Méthode appelée quand on clique sur "Ajouter une Catégorie"
    @FXML
    private void ouvrirFormulaireCategorie(ActionEvent event) {
        try {
            Parent categorieRoot = FXMLLoader.load(getClass().getResource("/categorieform.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Ajouter une Catégorie");
            stage.setScene(new Scene(categorieRoot));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

            // Fermer l'accueil si tu veux
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'ouverture du formulaire catégorie : " + e.getMessage());
        }
    }

    // Fermer le backoffice (lié au bouton X par exemple)
    @FXML
    private void closeApp(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
*/
/////////////////////////////////////////////

public class homecontroller extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    // Méthode start principale (charge home.fxml)
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/back.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Accueil");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de l'accueil : " + e.getMessage());
        }
    }

    // Méthode appelée quand on clique sur "Ajouter une Catégorie"
    public void ouvrirFormulaireCategorie(javafx.event.ActionEvent actionEvent) {
        try {
            Parent categorieRoot = FXMLLoader.load(getClass().getResource("/categorieform.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Ajouter une Catégorie");
            stage.setScene(new Scene(categorieRoot));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

            // Fermer l'accueil si tu veux
            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'ouverture du formulaire catégorie : " + e.getMessage());
        }
    }

    // Méthode appelée quand on clique sur "Ajouter un Produit"

    public void ouvrirFormulaireProduit(javafx.event.ActionEvent actionEvent) {
        try {
            Parent produitRoot = FXMLLoader.load(getClass().getResource("/produitform.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Ajouter un Produit");
            stage.setScene(new Scene(produitRoot));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

            // Fermer l'accueil si tu veux
            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'ouverture du formulaire produit : " + e.getMessage());
        }
    }
    // Fermer le backoffice (lié au bouton X par exemple)
    @FXML
    private void closeApp(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
