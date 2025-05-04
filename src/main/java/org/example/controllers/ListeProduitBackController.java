package org.example.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
    @FXML private Label totalLikesLabel;
    @FXML private Label avgLikesLabel;
    @FXML private Label topProductLabel;
    @FXML private HBox statsContainer;
    @FXML
    private PieChart likesPieChart;
    @FXML private BarChart<String, Number> likesChart;
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

        // Configurer les colonnes
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
        setupStatistics();
        // Charger les données initiales
        refreshproduitTable();
        refreshcategorieTable();
    }

    @FXML
    private void setupStatistics() {
        try {
            produitservice pService = new produitservice();

            // 1. Calculer les statistiques
            int totalLikes = pService.getTotalLikes();
            int productCount = pService.countAllProduits();
            double avgLikes = productCount > 0 ? (double)totalLikes / productCount : 0;
            List<produit> topProducts = pService.getMostLikedProducts(5); // Top 5 produits pour tous les graphiques

            // 2. Mettre à jour les labels avec les indicateurs clés
            Platform.runLater(() -> {
                totalLikesLabel.setText("Total Likes: " + totalLikes);
                avgLikesLabel.setText("Moyenne: " + String.format("%.1f", avgLikes));

                // Format amélioré pour le top produit
                if (!topProducts.isEmpty()) {
                    produit top = topProducts.get(0);
                    topProductLabel.setText("Top Produit: " + top.getNom() + " (" + top.getLikes() + " likes)");
                } else {
                    topProductLabel.setText("Top Produit: Aucun");
                }
            });

            // 3. Configurer les graphiques
            configureBarChart(topProducts);
            configurePieChart(topProducts);

        } catch (Exception e) {
            Platform.runLater(() -> {
                showAlert("Erreur", "Erreur lors du chargement des statistiques: " + e.getMessage());
            });
            e.printStackTrace();
        }
    }

    private void configureBarChart(List<produit> topProducts) {
        Platform.runLater(() -> {
            try {
                // Nettoyer les données existantes
                likesChart.getData().clear();

                // Configuration des axes
                CategoryAxis xAxis = (CategoryAxis) likesChart.getXAxis();
                NumberAxis yAxis = (NumberAxis) likesChart.getYAxis();

                // Paramètres visuels
                xAxis.setLabel("Produits");
                yAxis.setLabel("Nombre de Likes");
                likesChart.setTitle("Top 5 des Produits les Plus Aimés");
                likesChart.setAnimated(false);

                // Amélioration de la lisibilité des étiquettes
                xAxis.setTickLabelRotation(-45);
                xAxis.setTickLabelFont(Font.font("Arial", FontWeight.NORMAL, 10));
                xAxis.setTickLabelGap(5);

                // Création de la série de données
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName("Likes");

                // Ajout des données
                for (produit p : topProducts) {
                    // Tronquer les noms trop longs pour éviter débordement
                    String nomCourt = p.getNom().length() > 15 ? p.getNom().substring(0, 15) + "..." : p.getNom();
                    XYChart.Data<String, Number> data = new XYChart.Data<>(nomCourt, p.getLikes());
                    series.getData().add(data);
                }

                // Ajout de la série au graphique
                likesChart.getData().add(series);

                // Style personnalisé pour les barres (couleur orange)
                for (XYChart.Data<String, Number> data : series.getData()) {
                    // Appliquer le style lorsque le nœud est disponible
                    if (data.getNode() != null) {
                        data.getNode().setStyle("-fx-bar-fill: #FF7D3C;");
                    } else {
                        // Si le nœud n'est pas encore disponible, utiliser un listener
                        data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                            if (newNode != null) {
                                newNode.setStyle("-fx-bar-fill: #FF7D3C;");
                            }
                        });
                    }

                    // Ajouter des tooltips pour plus de détails au survol
                    final XYChart.Data<String, Number> finalData = data;
                    final int value = data.getYValue().intValue();

                    // Trouver le produit complet pour les détails du tooltip
                    produit matchingProduct = topProducts.stream()
                            .filter(p -> p.getLikes() == value && p.getNom().startsWith(data.getXValue().replace("...", "")))
                            .findFirst()
                            .orElse(null);

                    if (matchingProduct != null) {
                        Tooltip tooltip = new Tooltip(
                                matchingProduct.getNom() + "\n" +
                                        "Likes: " + matchingProduct.getLikes() + "\n" +
                                        "Prix: " + matchingProduct.getPrix() + "€"
                        );

                        // Appliquer le tooltip lorsque le nœud est disponible
                        if (data.getNode() != null) {
                            Tooltip.install(data.getNode(), tooltip);
                        } else {
                            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                                if (newNode != null) {
                                    Tooltip.install(newNode, tooltip);
                                }
                            });
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la configuration du graphique à barres: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void configurePieChart(List<produit> topProducts) {
        Platform.runLater(() -> {
            try {
                // Nettoyer les données existantes
                likesPieChart.getData().clear();

                // Configuration générale
                likesPieChart.setTitle("Répartition des Likes");
                likesPieChart.setLabelsVisible(true);
                likesPieChart.setLegendVisible(false);
                likesPieChart.setAnimated(false);

                // Définir un jeu de couleurs pour le graphique
                String[] colors = {"#FF7D3C", "#4D98CF", "#9E6DE0", "#5FAD56", "#FFB347"};
                int totalLikes = topProducts.stream().mapToInt(produit::getLikes).sum();

                // Créer les sections du graphique avec les données des produits
                int colorIndex = 0;
                for (produit p : topProducts) {
                    // Ne pas afficher les produits sans likes
                    if (p.getLikes() > 0) {
                        PieChart.Data slice = new PieChart.Data(p.getNom(), p.getLikes());
                        likesPieChart.getData().add(slice);

                        // Stocker le produit pour le tooltip
                        final produit finalProduct = p;

                        // Appliquer le style et le tooltip lorsque le nœud est disponible
                        int finalColorIndex = colorIndex;
                        slice.nodeProperty().addListener((obs, oldNode, newNode) -> {
                            if (newNode != null) {
                                // Appliquer une couleur
                                String color = colors[finalColorIndex % colors.length];
                                newNode.setStyle("-fx-pie-color: " + color + ";");

                                // Calculer le pourcentage pour le tooltip
                                double percentage = (finalProduct.getLikes() * 100.0) / totalLikes;

                                // Créer un tooltip détaillé
                                Tooltip tooltip = new Tooltip(
                                        finalProduct.getNom() + "\n" +
                                                "Likes: " + finalProduct.getLikes() + "\n" +
                                                String.format("%.1f%%", percentage) + " du total"
                                );
                                Tooltip.install(newNode, tooltip);
                            }
                        });

                        colorIndex++;
                    }
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la configuration du graphique circulaire: " + e.getMessage());
                e.printStackTrace();
            }
        });
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