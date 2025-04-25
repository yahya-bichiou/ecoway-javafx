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
    private int itemsPerPage = 20;
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
        VBox card = new VBox(10); // Espacement entre les éléments
        card.getStyleClass().add("product-card");
        card.setPrefWidth(300);
        card.setPadding(new Insets(15));

        // Ajout d'un cadre autour du produit
        card.setStyle("-fx-border-color: #4CAF50; -fx-border-width: 2; -fx-border-radius: 5;");

        // StackPane pour superposer l'image et l'icône de cœur
        StackPane imagePane = new StackPane();

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
        imageView.setFitWidth(400);  // Taille plus grande
        imageView.setFitHeight(450); // Taille plus grande
        imageView.setPreserveRatio(true);

        // Icône de cœur pour "J'aime"
        ImageView heartIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/heart.png")));
        heartIcon.setFitWidth(35);  // Taille initiale du cœur
        heartIcon.setFitHeight(35); // Taille initiale du cœur
        heartIcon.setStyle("-fx-cursor: hand;");  // Changer le curseur pour indiquer que c'est cliquable

        // Ajouter l'événement de clic pour changer la couleur et la taille du cœur
        heartIcon.setOnMouseClicked(e -> {
            handleLike(produit, heartIcon);  // Passer l'icône du cœur pour changer la couleur et la taille
        });

        // Positionner l'icône de cœur en haut à droite
        StackPane.setAlignment(heartIcon, Pos.TOP_RIGHT);

        // Ajouter l'image et l'icône au StackPane
        imagePane.getChildren().addAll(imageView, heartIcon);

        // Textes sous l'image
        Label nameLabel = new Label(produit.getNom());
        nameLabel.getStyleClass().add("product-name");
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");  // Titre en gras

        Label priceLabel = new Label(String.format("%.2f DT", produit.getPrix()));
        priceLabel.getStyleClass().add("product-price");
        priceLabel.setStyle("-fx-font-size: 16px;");  // Prix

        Label categoryLabel = new Label("Catégorie: " + getCategoryName(produit.getCategorieId()));
        categoryLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: rgba(85,28,19,0.65);");

        // Création du bouton "Voir détails"
        Button viewBtn = new Button("Voir détails");
        viewBtn.getStyleClass().add("view-button");
        viewBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");  // Bouton en vert
        viewBtn.setOnAction(e -> showProductDetails(produit));

        // Icône de panier
        ImageView cartIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/cart.png")));
        cartIcon.setFitWidth(20);
        cartIcon.setFitHeight(20);

        // HBox pour aligner le bouton "Voir détails" et l'icône du panier
        HBox actionBox = new HBox(10, viewBtn, cartIcon);

        // Ajouter l'image, les labels et le bouton dans la carte
        card.getChildren().addAll(imagePane, nameLabel, priceLabel, categoryLabel, actionBox);

        return card;
    }

    // Méthode pour gérer l'événement de "Like" et changer la couleur et la taille du cœur
    private void handleLike(produit produit, ImageView heartIcon) {
        // Incrémenter le compteur de "likes" pour ce produit
        System.out.println("Produit aimé: " + produit.getNom());

        // Vous devrez mettre à jour la base de données avec l'augmentation du nombre de likes
        produitService.incrementLike(produit);

        // Change la couleur du cœur en rouge et agrandir l'icône
        heartIcon.setImage(new Image(getClass().getResourceAsStream("/images/heartrouge.png")));  // Remplacer l'icône par un cœur rouge

        // Agrandir l'icône lorsque l'utilisateur clique dessus
        heartIcon.setFitWidth(35);  // Augmenter la taille du cœur
        heartIcon.setFitHeight(35); // Augmenter la taille du cœur
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
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage); // Calculer le nombre total de pages
        pageInfo.setText(String.format("Page %d/%d", currentPage, totalPages)); // Afficher l'information de pagination

        // Désactiver les boutons si nécessaire
        previousPageBtn.setDisable(currentPage <= 1); // Désactiver "Précédent" si la page actuelle est la première
        nextPageBtn.setDisable(currentPage >= totalPages); // Désactiver "Suivant" si la page actuelle est la dernière
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

