package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.models.categorie;
import org.example.models.produit;
import org.example.services.categorieservice;
import org.example.services.produitservice;

import java.io.IOException;
import java.util.List;

public class ProduitFrontController{
    @FXML private FlowPane categoriesContainer;
    @FXML private FlowPane productsContainer;
    @FXML private Label pageInfo;
    @FXML private StackPane notificationPane;
    @FXML private ListView<String> notifList;
    @FXML private Label noNotifMessage;
    @FXML private Label notifCount;
    @FXML private Button previousPageBtn;
    @FXML private Button nextPageBtn;

    private int currentPage = 1;
    private int itemsPerPage = 5;
    private categorie selectedCategory;

    private final categorieservice categorieService = new categorieservice();
    private final produitservice produitService = new produitservice();

    private void loadCategories() {
        categoriesContainer.getChildren().clear();
        try {
            List<categorie> categories = categorieService.getAll();
            System.out.println("[DEBUG] Categories récupérées : " + categories.size());  // Debug message
            for (categorie cat : categories) {
                Button categoryBtn = new Button(cat.getNom());
                categoryBtn.getStyleClass().add("category-button");
                categoryBtn.setOnAction(e -> {
                    selectedCategory = cat;
                    currentPage = 1;
                    loadProducts();
                });
                categoriesContainer.getChildren().add(categoryBtn);
            }
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les catégories");
            e.printStackTrace();
        }
    }


    private void loadProducts() {
        productsContainer.getChildren().clear();
        try {
            List<produit> produits;
            int totalItems;

            // Vérification de la catégorie sélectionnée et récupération des produits
            if (selectedCategory != null) {
                produits = produitService.getProduitsByCategorie(selectedCategory.getId(), currentPage, itemsPerPage);
                totalItems = produitService.countProduitsByCategorie(selectedCategory.getId());
            } else {
                produits = produitService.getAll(currentPage, itemsPerPage);
                totalItems = produitService.countAllProduits();
            }

            // Si aucun produit n'est trouvé
            if (produits.isEmpty()) {
                showAlert("Aucun produit", "Aucun produit trouvé dans cette catégorie.");
            }

            // Afficher les produits récupérés
            for (produit produit : produits) {
                productsContainer.getChildren().add(createProductCard(produit));
            }

            // Mise à jour de l'information de pagination
            updatePaginationInfo(totalItems);
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les produits: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private VBox createProductCard(produit produit) {
        VBox card = new VBox(10);
        card.getStyleClass().add("product-card");
        card.setPrefWidth(300);
        card.setPadding(new Insets(15));

        // Ajout d'un cadre autour du produit
        card.setStyle("-fx-border-color: #4CAF50; -fx-border-width: 2; -fx-border-radius: 5;");

        // Image
        ImageView imageView = new ImageView();
        try {
            String imagePath = produit.getImage();
            if (imagePath == null || imagePath.isEmpty()) {
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/compost.jpeg")));
            } else {
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/" + imagePath)));
            }
        } catch (Exception e) {
            imageView.setImage(new Image(getClass().getResourceAsStream("/images/compost.jpeg")));
        }
        imageView.setFitWidth(280);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(true);

        // Product Info
        Label nameLabel = new Label(produit.getNom());
        nameLabel.getStyleClass().add("product-name");
        nameLabel.setStyle("-fx-font-weight: bold;");  // Mettre le titre en gras

        Label priceLabel = new Label(String.format("%.2f €", produit.getPrix()));
        priceLabel.getStyleClass().add("product-price");

        Label categoryLabel = new Label("Catégorie: " + getCategoryName(produit.getCategorieId()));

        // Création du bouton "Voir détails"
        Button viewBtn = new Button("Voir détails");
        viewBtn.getStyleClass().add("view-button");
        viewBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Couleur du bouton vers le vert
        viewBtn.setOnAction(e -> showProductDetails(produit));

        // Icône de panier
        ImageView cartIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/cart.png")));
        cartIcon.setFitWidth(20);
        cartIcon.setFitHeight(20);

        // HBox pour aligner le bouton "Voir détails" et l'icône du panier
        HBox actionBox = new HBox(10, viewBtn, cartIcon);

        // Image clickable
        imageView.setOnMouseClicked(e -> showProductDetails(produit));

        // Ajouter les composants à la carte
        card.getChildren().addAll(imageView, nameLabel, priceLabel, categoryLabel, actionBox);

        return card;
    }




    private void showProductDetails(produit produit) {
        try {
            // Charger le fichier FXML pour afficher les détails du produit dans un popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/detaille_produit.fxml"));
            Parent root = loader.load();

            // Initialiser le contrôleur du popup avec les données du produit
            DetailleProduitFrontController controller = loader.getController();  // Utilisez le bon contrôleur
            controller.initData(produit);  // Initialiser les données du produit dans le popup

            // Créer une nouvelle fenêtre pour afficher le popup
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails du produit");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'afficher les détails: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    public void initialize() {
        loadCategories();  // Charge les catégories au démarrage de la page
    }

    private String getCategoryName(int categoryId) {
        try {
            categorie cat = categorieService.getById(categoryId);
            return cat != null ? cat.getNom() : "Inconnue";
        } catch (Exception e) {
            return "Inconnue";
        }
    }

    @FXML
    private void previousPage() {
        if (currentPage > 1) {
            currentPage--;
            loadProducts();
        }
    }

    @FXML
    private void nextPage() {
        currentPage++;
        loadProducts();
    }

    private void updatePaginationInfo(int totalItems) {
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);
        pageInfo.setText(String.format("Page %d/%d", currentPage, totalPages));

        // Désactiver les boutons si nécessaire
        previousPageBtn.setDisable(currentPage <= 1);
        nextPageBtn.setDisable(currentPage >= totalPages);
    }

    @FXML
    private void handleNotifications() {
        notificationPane.setVisible(!notificationPane.isVisible());
        if (notificationPane.isVisible()) {
            notifCount.setVisible(false);
            notifCount.setText("0");
        }
    }

    @FXML
    private void hideNotifications() {
        notificationPane.setVisible(false);
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

