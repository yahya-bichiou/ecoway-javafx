package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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

public class produitfrontcontroller {
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
    private int itemsPerPage = 6;
    private categorie selectedCategory;

    private final categorieservice categorieService = new categorieservice();
    private final produitservice produitService = new produitservice();


    private void loadCategories() {
        categoriesContainer.getChildren().clear();
        try {
            List<categorie> categories = categorieService.getAll();
            for (categorie categorie : categories) {
                Button categoryBtn = new Button(categorie.getNom());
                categoryBtn.getStyleClass().add("category-button");
                categoryBtn.setOnAction(e -> {
                    selectedCategory = categorie;
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

            if (selectedCategory != null) {
                produits = produitService.getProduitsByCategorie(selectedCategory.getId(), currentPage, itemsPerPage);
                totalItems = produitService.countProduitsByCategorie(selectedCategory.getId());
            } else {
                produits = produitService.getAll(currentPage, itemsPerPage);
                totalItems = produitService.countAllProduits();
            }

            for (produit produit : produits) {
                productsContainer.getChildren().add(createProductCard(produit));
            }

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

        // Image
        ImageView imageView = new ImageView();
        try {
            Image image = new Image(produit.getImage());
            imageView.setImage(image);
        } catch (Exception e) {
            imageView.setImage(new Image(getClass().getResourceAsStream("/images/default-product.png")));
        }
        imageView.setFitWidth(280);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(true);

        // Product Info
        Label nameLabel = new Label(produit.getNom());
        nameLabel.getStyleClass().add("product-name");

        Label priceLabel = new Label(String.format("%.2f €", produit.getPrix()));
        priceLabel.getStyleClass().add("product-price");

        Label categoryLabel = new Label("Catégorie: " + getCategoryName(produit.getCategorieId()));

        // View Button
        Button viewBtn = new Button("Voir détails");
        viewBtn.getStyleClass().add("view-button");
        viewBtn.setOnAction(e -> showProductDetails(produit));

        card.getChildren().addAll(imageView, nameLabel, priceLabel, categoryLabel, viewBtn);
        return card;
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

    private void showProductDetails(produit produit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/detail_produit.fxml"));
            Parent root = loader.load();

            detailleproduit controller = loader.getController();
            controller.initData(produit);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails du produit");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'afficher les détails: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}