package org.example.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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

    // categorie
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

    @FXML
    void closeApp() {
        Platform.exit();
    }

    @FXML
    void addproduit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddproduitBack.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Ajouter un produit");

            stage.centerOnScreen(); // ✅ Centre la fenêtre
            stage.initModality(Modality.APPLICATION_MODAL); // Bloque interaction avec la fenêtre principale
            stage.showAndWait(); // Attendre la fermeture de la fenêtre

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addcategorie() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddcategorieBack.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Ajouter une catégorie");

            stage.centerOnScreen(); // ✅ Centre la fenêtre
            stage.initModality(Modality.APPLICATION_MODAL); // Bloque interaction avec la fenêtre principale
            stage.showAndWait(); // Attendre la fermeture de la fenêtre

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {

        System.out.println("Initializing table...");

        try {
            // Set up columns produit
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

            // Set up columns categorie
            IdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

            // Setup action columns
            setupActionColumns();

            // Load data produit
            refreshproduitTable();
            System.out.println("produit table initialized successfully.");

            // Load data categorie
            refreshcategorieTable();
            System.out.println("categorie table initialized successfully.");

        } catch (Exception e) {
            System.out.println("ERROR during table initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void refreshproduitTable() {
        try {
            produitservice prodService = new produitservice();
            List<produit> produits = prodService.getAll();

            // Debug
            System.out.println("[DEBUG] Produits chargés: " + produits.size());
            produits.forEach(p -> System.out.println(
                    p.getId() + " - " + p.getNom() +
                            " - Cat: " + (p.getCategorieId() != 0 ? p.getCategorieId() : "null")));

            ObservableList<produit> observableList = FXCollections.observableArrayList(produits);
            produitTableView.setItems(observableList);

            // Force le refresh de la table
            produitTableView.refresh();
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des produits:");
            e.printStackTrace();
        }
    }

    private void refreshcategorieTable() {
        categorieservice catservice = new categorieservice();
        List<categorie> categories = catservice.getAll();
        ObservableList<categorie> observableList = FXCollections.observableArrayList(categories);
        categorieTableView.setItems(observableList);
    }

    private void setupActionColumns() {
        // Setup for produit table
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final HBox buttons = new HBox(5);
            private final Button editBtn = new Button();
            private final Button deleteBtn = new Button();
            private final Button viewBtn = new Button();

            {
                // Styling buttons
                buttons.setAlignment(Pos.CENTER);

                // Edit button setup
                ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/edit.png")));
                editIcon.setFitHeight(16);
                editIcon.setFitWidth(16);
                editBtn.setGraphic(editIcon);
                editBtn.setStyle("-fx-background-color: transparent;");
                editBtn.setOnAction(event -> {
                    produit prodData = getTableView().getItems().get(getIndex());
                    editprod(prodData);
                });

                // Delete button setup
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
                        throw new RuntimeException(e);
                    }
                });

                // View button setup
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

        // Setup for categorie table
        actionCol2.setCellFactory(param -> new TableCell<>() {
            private final HBox buttons = new HBox(5);
            private final Button editBtn = new Button();
            private final Button deleteBtn = new Button();
            private final Button viewBtn = new Button();

            {
                // Styling buttons
                buttons.setAlignment(Pos.CENTER);

                // Edit button setup
                ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/edit.png")));
                editIcon.setFitHeight(16);
                editIcon.setFitWidth(16);
                editBtn.setGraphic(editIcon);
                editBtn.setStyle("-fx-background-color: transparent;");
                editBtn.setOnAction(event -> {
                    categorie categorieData = getTableView().getItems().get(getIndex());
                    editcat(categorieData);
                });

                // Delete button setup
                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/trash.png")));
                deleteIcon.setFitHeight(16);
                deleteIcon.setFitWidth(16);
                deleteBtn.setGraphic(deleteIcon);
                deleteBtn.setStyle("-fx-background-color: transparent;");
                deleteBtn.setOnAction(event -> {
                    categorie categorieData = getTableView().getItems().get(getIndex());
                    try {
                        deletecat(categorieData);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                // View button setup
                ImageView viewIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/eye.png")));
                viewIcon.setFitHeight(16);
                viewIcon.setFitWidth(16);
                viewBtn.setGraphic(viewIcon);
                viewBtn.setStyle("-fx-background-color: transparent;");
                viewBtn.setOnAction(event -> {
                    categorie categorieData = getTableView().getItems().get(getIndex());
                    viewDetailscat(categorieData);
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
        if (prod != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailprodBack.fxml"));
                Parent root = loader.load();

                DetailleProduitController detailsController = loader.getController();
                detailsController.initData(prod);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Détails de produit");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir les détails", e.getMessage());
            }
        }
    }

    @FXML
    void viewDetailscat(categorie cat) {
        if (cat != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailcatBack.fxml"));
                Parent root = loader.load();

                DetailleCategorieController detailsController = loader.getController();
                detailsController.initData(cat);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Détails de categorie");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir les détails", e.getMessage());
            }
        }
    }

    @FXML
    void deleteprod(produit prod) throws SQLException {
        if (prod != null) {
            if (showConfirmation("Confirmer la suppression",
                    "Supprimer produit?",
                    "Êtes-vous sûr de vouloir supprimer le produit '" + prod.getNom() + "' ?")) {
                refreshproduitTable();
                produitservice prodservice = new produitservice();
                prodservice.deleteproduit(prod.getId());

                showAlert("Succès", "produit supprimé", "produit a été supprimé avec succès.");
            }
        }
    }

    @FXML
    void deletecat(categorie cat) throws SQLException {
        refreshproduitTable();
        if (cat != null) {
            if (showConfirmation("Confirmer la suppression",
                    "Supprimer la catégorie",
                    "Êtes-vous sûr de vouloir supprimer la catégorie '" + cat.getNom() + "' ?")) {
                categorieservice catservice = new categorieservice();
                catservice.deletecategorie(cat.getId());
                showAlert("Succès", "categorie supprimé", "categorie a été supprimé avec succès.");
            }
        }
    }

    @FXML
    void editprod(produit prod) {
        if (prod != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditProduitBack.fxml"));
                Parent root = loader.load();

                //EditProduitBackController editController = loader.getController();
                // editController.setProduitData(prod);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Participant");
                stage.showAndWait();

                refreshproduitTable();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification", e.getMessage());
            }
        } else {
            showAlert("Aucune sélection", "Aucun participant sélectionné", "Veuillez sélectionner un participant à modifier.");
        }
    }

    @FXML
    void editcat(categorie cat) {
        if (cat != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditCategorieBack.fxml"));
                Parent root = loader.load();

                EditCategorieBackController editController = loader.getController();
                editController.setCategorieData(cat);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Participant");
                stage.showAndWait();

                refreshproduitTable();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification", e.getMessage());
            }
        } else {
            showAlert("Aucune sélection", "Aucun participant sélectionné", "Veuillez sélectionner un participant à modifier.");
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
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
