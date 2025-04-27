package org.example.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.models.produit;
import org.example.services.produitservice;
import org.example.models.categorie;
import org.example.services.categorieservice;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ListeProduitBackController {
    @FXML
    private TableView<produit> produitTableView;
    @FXML
    private TableColumn<produit, Integer> idCol;
    @FXML
    private TableColumn<produit, String> NomCol;
    @FXML
    private TableColumn<produit, String> DescriptionCol;
    @FXML
    private TableColumn<produit, String> QualiteCol;
    @FXML
    private TableColumn<produit, Integer> QuantiteCol;
    @FXML
    private TableColumn<produit, Double> PrixCol;
    @FXML
    private TableColumn<produit, LocalDateTime> DateAjoutCol;
    @FXML
    private TableColumn<produit, String> imageCol;
    @FXML
    private TableColumn<produit, Integer> categorieCol;
    @FXML
    private TableColumn<produit, Void> actionCol;

    // Catégorie
    @FXML
    private TableView<categorie> categorieTableView;
    @FXML
    private TableColumn<categorie, Integer> IdCol;
    @FXML
    private TableColumn<categorie, String> nomCol;
    @FXML
    private TableColumn<categorie, String> descriptionCol;
    @FXML
    private TableColumn<categorie, Void> actionCol2;
    @FXML
    private Button closeButton;

    // ObservableLists pour le data binding
    private ObservableList<produit> produitList = FXCollections.observableArrayList();
    private ObservableList<categorie> categorieList = FXCollections.observableArrayList();

    @FXML
    void closeApp() {
        Platform.exit();
    }

    @FXML
    void addproduit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddproduitBack.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un produit");
            stage.centerOnScreen();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Rafraîchir après fermeture
            refreshproduitTable();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur de chargement");
            e.printStackTrace();
        }
    }

    @FXML
    void addcategorie() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddcategorieBack.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une catégorie");
            stage.centerOnScreen();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Rafraîchir après fermeture
            refreshcategorieTable();
            refreshproduitTable(); // Les produits peuvent dépendre des catégories
        } catch (IOException e) {
            showAlert("Erreur", "Erreur de chargement");
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Lier les ObservableList aux TableView
        produitTableView.setItems(produitList);
        categorieTableView.setItems(categorieList);

        // Configurer les colonnes produit
        NomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        DescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        QualiteCol.setCellValueFactory(new PropertyValueFactory<>("qualite"));
        QuantiteCol.setCellValueFactory(new PropertyValueFactory<>("quantite_disponible"));
        PrixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));

        DateAjoutCol.setCellValueFactory(new PropertyValueFactory<>("date_ajout"));
        DateAjoutCol.setCellFactory(column -> new TableCell<produit, LocalDateTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatter.format(item));
            }
        });

        imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));
        categorieCol.setCellValueFactory(new PropertyValueFactory<>("catégorie_id"));

        // Configurer les colonnes catégorie
        IdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Configurer les colonnes d'actions
        setupActionColumns();

        // Charger les données initiales
        refreshproduitTable();
        refreshcategorieTable();
    }

    private void refreshproduitTable() {
        try {
            produitservice prodService = new produitservice();
            List<produit> produits = prodService.getAll();
            produitList.setAll(produits); // Mise à jour de l'ObservableList
        } catch (Exception e) {
            showAlert("Erreur", "Erreur de chargement");
            e.printStackTrace();
        }
    }

    private void refreshcategorieTable() {
        try {
            categorieservice catservice = new categorieservice();
            List<categorie> categories = catservice.getAll();
            categorieList.setAll(categories); // Mise à jour de l'ObservableList
        } catch (Exception e) {
            showAlert("Erreur", "Erreur de chargement");
            e.printStackTrace();
        }
    }

    private void setupActionColumns() {
        // Configuration pour la table produit
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final HBox buttons = new HBox(5);
            private final Button editBtn = new Button();
            private final Button deleteBtn = new Button();
            private final Button viewBtn = new Button();

            {
                buttons.setAlignment(Pos.CENTER);

                // Bouton Éditer
                ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/edit.png")));
                editIcon.setFitHeight(16);
                editIcon.setFitWidth(16);
                editBtn.setGraphic(editIcon);
                editBtn.setStyle("-fx-background-color: transparent;");
                editBtn.setOnAction(event -> {
                    produit prodData = getTableView().getItems().get(getIndex());
                    editprod(prodData);
                });

                // Bouton Supprimer
                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/trash.png")));
                deleteIcon.setFitHeight(16);
                deleteIcon.setFitWidth(16);
                deleteBtn.setGraphic(deleteIcon);
                deleteBtn.setStyle("-fx-background-color: transparent;");
                deleteBtn.setOnAction(event -> {
                    produit prodData = getTableView().getItems().get(getIndex());
                    try {
                        deleteprod(prodData);
                    } catch (SQLException e) {
                        showAlert("Erreur", "Erreur de suppression");
                    }
                });

                // Bouton Voir
                ImageView viewIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/eye.png")));
                viewIcon.setFitHeight(16);
                viewIcon.setFitWidth(16);
                viewBtn.setGraphic(viewIcon);
                viewBtn.setStyle("-fx-background-color: transparent;");
                viewBtn.setOnAction(event -> {
                    produit prodData = getTableView().getItems().get(getIndex());
                    viewDetailsprod(prodData);
                });

                buttons.getChildren().addAll(viewBtn, editBtn, deleteBtn);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });

        // Configuration pour la table catégorie
        actionCol2.setCellFactory(param -> new TableCell<>() {
            private final HBox buttons = new HBox(5);
            private final Button editBtn = new Button();
            private final Button deleteBtn = new Button();
            private final Button viewBtn = new Button();

            {
                buttons.setAlignment(Pos.CENTER);

                // Bouton Éditer
                ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/edit.png")));
                editIcon.setFitHeight(16);
                editIcon.setFitWidth(16);
                editBtn.setGraphic(editIcon);
                editBtn.setStyle("-fx-background-color: transparent;");
                editBtn.setOnAction(event -> {
                    categorie catData = getTableView().getItems().get(getIndex());
                    editcat(catData);
                });

                // Bouton Supprimer
                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/trash.png")));
                deleteIcon.setFitHeight(16);
                deleteIcon.setFitWidth(16);
                deleteBtn.setGraphic(deleteIcon);
                deleteBtn.setStyle("-fx-background-color: transparent;");
                deleteBtn.setOnAction(event -> {
                    categorie catData = getTableView().getItems().get(getIndex());
                    try {
                        deletecat(catData);
                    } catch (SQLException e) {
                        showAlert("Erreur", "Erreur de suppression");
                    }
                });

                // Bouton Voir
                ImageView viewIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/eye.png")));
                viewIcon.setFitHeight(16);
                viewIcon.setFitWidth(16);
                viewBtn.setGraphic(viewIcon);
                viewBtn.setStyle("-fx-background-color: transparent;");
                viewBtn.setOnAction(event -> {
                    categorie catData = getTableView().getItems().get(getIndex());
                    viewDetailscat(catData);
                });

                buttons.getChildren().addAll(viewBtn, editBtn, deleteBtn);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
    }

    @FXML
    void viewDetailsprod(produit prod) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailprodBack.fxml"));
            Parent root = loader.load();

            DetailleProduitController controller = loader.getController();
            controller.initData(prod);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails du produit");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur de chargement");
            e.printStackTrace();
        }
    }

    @FXML
    void viewDetailscat(categorie cat) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailcatBack.fxml"));
            Parent root = loader.load();

            DetailleCategorieController controller = loader.getController();
            controller.initData(cat);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails de la catégorie");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur de chargement");
            e.printStackTrace();
        }
    }

    @FXML
    void deleteprod(produit prod) throws SQLException {
        if (prod != null && showConfirmation("Confirmation", "Supprimer le produit",
                "Êtes-vous sûr de vouloir supprimer " + prod.getNom() + "?")) {

            produitservice service = new produitservice();
            service.deleteproduit(prod.getId());

            refreshproduitTable();
            showAlert("Succès", "Produit supprimé");
        }
    }

    @FXML
    void deletecat(categorie cat) throws SQLException {
        if (cat != null && showConfirmation("Confirmation", "Supprimer la catégorie",
                "Êtes-vous sûr de vouloir supprimer " + cat.getNom() + "?")) {

            categorieservice service = new categorieservice();
            service.deletecategorie(cat.getId());

            refreshcategorieTable();
            refreshproduitTable(); // Rafraîchir aussi les produits
            showAlert("Succès", "Catégorie supprimée");
        }
    }

    @FXML
    void editprod(produit prod) {
        try {
            // Vérification du produit
            if (prod == null) {
                showAlert("Erreur", "Aucun produit sélectionné");
                return;
            }

            // Chargement du FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditProduitBack.fxml"));
            Parent root = loader.load();

            // Configuration du contrôleur
            EditProduitBackController controller = loader.getController();
            controller.setProduitData(prod);

            // Création de la fenêtre
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Produit - " + prod.getNom());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Rafraîchissement après modification
            refreshproduitTable();

        } catch (IOException e) {
            showAlert("Erreur Critique",
                    "Impossible d'ouvrir l'éditeur\n" +
                            "Cause: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void editcat(categorie cat) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditCategorieBack.fxml"));
            Parent root = loader.load();

            EditCategorieBackController controller = loader.getController();
            controller.setCategorieData(cat);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier catégorie");
            stage.showAndWait();

            refreshcategorieTable(); // Rafraîchir après modification
            refreshproduitTable(); // Les produits peuvent dépendre des catégories
        } catch (IOException e) {
            showAlert("Erreur", "Erreur de chargement");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String header) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(title);
        alert.showAndWait();
    }

    private boolean showConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}