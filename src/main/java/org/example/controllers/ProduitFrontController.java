package org.example.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import org.example.models.categorie;
import org.example.models.produit;
import org.example.services.categorieservice;
import org.example.services.produitservice;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ProduitFrontController{

    @FXML private StackPane mainStack;
    @FXML private StackPane popupOverlay;
    @FXML private VBox popupContent;
    @FXML private ImageView popupImage;

    @FXML private Label popupTitle, popupDescription, popupPrice, popupQualite, popupQuantitedispo,popupdate;
    @FXML private FlowPane categoriesContainer;
    @FXML private FlowPane productsContainer;
    @FXML private StackPane popupImageContainer; // ou VBox selon votre choix
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
            System.out.println("[DEBUG] Nombre de catégories : " + categories.size());

            for (categorie cat : categories) {
                Button categoryBtn = createCategoryButton(cat);
                categoriesContainer.getChildren().add(categoryBtn);
            }

            // Appliquer le CSS après la création des boutons
            applyCategoryStyles();

        } catch (Exception e) {
            showAlert("Erreur", "Chargement des catégories échoué : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Button createCategoryButton(categorie cat) {
        Button btn = new Button(cat.getNom());

        // Style de base via code Java pour éviter les problèmes de CSS
        btn.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;");
        btn.setPadding(new Insets(8, 15, 8, 15));

        // Classes CSS
        btn.getStyleClass().addAll("category-button", "rounded-button");

        // Effet de survol
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #f8f8f8; -fx-text-fill: #333;"));

        btn.setOnAction(e -> {
            selectedCategory = cat;
            currentPage = 1;
            loadProducts();
        });

        return btn;
    }

    private void applyCategoryStyles() {
        try {
            String css = getClass().getResource("/ressources/styles.css").toExternalForm();
            if (!categoriesContainer.getScene().getStylesheets().contains(css)) {
                categoriesContainer.getScene().getStylesheets().add(css);
            }
        } catch (NullPointerException e) {
            System.err.println("Fichier CSS non trouvé, utilisation des styles par défaut");
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
        card.setStyle("-fx-border-color: #4CAF50; -fx-border-width: 2; -fx-border-radius: 5;");

        // StackPane pour l'image et le cœur
        StackPane imagePane = new StackPane();

        // Image principale
        ImageView mainImageView = new ImageView();
        mainImageView.setFitWidth(400);
        mainImageView.setFitHeight(450);
        mainImageView.setPreserveRatio(true);

        // Charger les images avec gestion de l'image par défaut
        List<String> imagePaths = produit.getAllImages();
        List<Image> productImages = new ArrayList<>();

        // Si aucune image n'est disponible, utiliser l'image par défaut
        if (imagePaths.isEmpty()) {
            try {
                Image defaultImage = new Image(getClass().getResourceAsStream("/images/compost.jpeg"));
                productImages.add(defaultImage);
            } catch (Exception e) {
                System.err.println("Erreur chargement image par défaut");
            }
        } else {
            // Charger les images du produit
            for (String path : imagePaths) {
                try {
                    Image img = new Image(getClass().getResourceAsStream("/images/" + path));
                    productImages.add(img);
                } catch (Exception e) {
                    System.err.println("Erreur chargement image: " + path);
                    // En cas d'erreur, utiliser l'image par défaut
                    try {
                        Image defaultImage = new Image(getClass().getResourceAsStream("/images/compost.jpeg"));
                        productImages.add(defaultImage);
                    } catch (Exception ex) {
                        System.err.println("Erreur chargement image par défaut");
                    }
                }
            }
        }

        // Afficher la première image (ou l'image par défaut)
        if (!productImages.isEmpty()) {
            mainImageView.setImage(productImages.get(0));
        }

        // Icône de cœur avec gestion du clic
        ImageView heartIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/heart.png")));
        heartIcon.setFitWidth(35);
        heartIcon.setFitHeight(35);
        heartIcon.setStyle("-fx-cursor: hand;");
        heartIcon.setOnMouseClicked(e -> handleLike(produit, heartIcon));
        StackPane.setAlignment(heartIcon, Pos.TOP_RIGHT);

        // Navigation si plusieurs images
        if (productImages.size() > 1) {
            HBox navBox = new HBox(5);
            navBox.setAlignment(Pos.CENTER);

            Button prevBtn = new Button("<");
            Button nextBtn = new Button(">");

            AtomicInteger currentIndex = new AtomicInteger(0);

            prevBtn.setOnAction(e -> {
                int newIndex = currentIndex.get() - 1;
                if (newIndex < 0) newIndex = productImages.size() - 1;
                currentIndex.set(newIndex);
                mainImageView.setImage(productImages.get(newIndex));
            });

            nextBtn.setOnAction(e -> {
                int newIndex = currentIndex.get() + 1;
                if (newIndex >= productImages.size()) newIndex = 0;
                currentIndex.set(newIndex);
                mainImageView.setImage(productImages.get(newIndex));
            });

            navBox.getChildren().addAll(prevBtn, nextBtn);
            imagePane.getChildren().addAll(mainImageView, heartIcon);
            card.getChildren().addAll(imagePane, navBox);
        } else {
            imagePane.getChildren().addAll(mainImageView, heartIcon);
            card.getChildren().add(imagePane);
        }

        // Informations du produit
        Label nameLabel = new Label(produit.getNom());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

        Label priceLabel = new Label(String.format("%.2f DT", produit.getPrix()));
        priceLabel.setStyle("-fx-font-size: 16px;");

        Label categoryLabel = new Label("Catégorie: " + getCategoryName(produit.getCategorieId()));
        categoryLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: rgba(85,28,19,0.65);");

        // Boutons d'action
        Button viewBtn = new Button("Voir détails");
        viewBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        viewBtn.setOnAction(e -> showProductDetails(produit));

        ImageView cartIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/cart.png")));
        cartIcon.setFitWidth(20);
        cartIcon.setFitHeight(20);

        HBox actionBox = new HBox(10, viewBtn, cartIcon);
        card.getChildren().addAll(nameLabel, priceLabel, categoryLabel, actionBox);

        return card;
    }

    private void updateThumbnails(HBox thumbnailsBox, List<Image> images, int currentIndex) {
        thumbnailsBox.getChildren().clear();

        for (int i = 0; i < images.size(); i++) {
            ImageView thumbnail = new ImageView(images.get(i));
            thumbnail.setFitWidth(40);
            thumbnail.setFitHeight(40);
            thumbnail.setPreserveRatio(true);

            if (i == currentIndex) {
                thumbnail.setStyle("-fx-border-color: #4CAF50; -fx-border-width: 2;");
            } else {
                thumbnail.setStyle("-fx-border-width: 0; -fx-cursor: hand;");
            }

            final int index = i;
            thumbnail.setOnMouseClicked(e -> {
                thumbnailsBox.getChildren().forEach(node -> {
                    if (node instanceof ImageView) {
                        node.setStyle("-fx-border-width: 0;");
                    }
                });
                thumbnail.setStyle("-fx-border-color: #4CAF50; -fx-border-width: 2;");
                ((ImageView)thumbnailsBox.getParent().getParent().getChildrenUnmodifiable().get(0).lookup(".image-view"))
                        .setImage(images.get(index));
            });

            thumbnailsBox.getChildren().add(thumbnail);
        }
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
    private void showProductDetails(produit produit) {
        // Mettre à jour les informations textuelles
        popupTitle.setText(produit.getNom());
        popupDescription.setText(produit.getDescription());
        popupPrice.setText(String.format("%.2f DT", produit.getPrix()));
        popupQualite.setText(produit.getQualite());
        popupQuantitedispo.setText(String.valueOf(produit.getQuantiteDisponible()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        popupdate.setText(produit.getDateAjout().format(formatter));

        // Préparer le conteneur d'images
        popupImageContainer.getChildren().clear();
        VBox imageCarousel = new VBox(10);
        imageCarousel.setAlignment(Pos.CENTER);

        // Charger toutes les images du produit
        List<String> imagePaths = produit.getAllImages();
        List<Image> productImages = new ArrayList<>();

        // Chargement avec gestion des erreurs
        for (String path : imagePaths) {
            try {
                Image img = new Image(getClass().getResourceAsStream("/images/" + path));
                productImages.add(img);
            } catch (Exception e) {
                try {
                    productImages.add(new Image(getClass().getResourceAsStream("/images/compost.jpeg")));
                } catch (Exception ex) {
                    System.err.println("Erreur chargement image par défaut");
                }
            }
        }

        // Si aucune image, utiliser l'image par défaut
        if (productImages.isEmpty()) {
            try {
                productImages.add(new Image(getClass().getResourceAsStream("/images/compost.jpeg")));
            } catch (Exception e) {
                System.err.println("Erreur chargement image par défaut");
            }
        }
            // Configuration de l'image principale
            ImageView mainImageView = new ImageView();
            mainImageView.setFitWidth(400);
            mainImageView.setFitHeight(400);
            mainImageView.setPreserveRatio(true);
            mainImageView.setImage(productImages.get(0));

            // Animation de transition
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), mainImageView);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.3);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), mainImageView);
            fadeIn.setFromValue(0.3);
            fadeIn.setToValue(1.0);

            // Navigation automatique seulement
            if (productImages.size() > 1) {
                // Label pour afficher la position
                Label counterLabel = new Label("1/" + productImages.size());
                counterLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

                AtomicInteger currentIndex = new AtomicInteger(0);

                // Timeline pour le défilement automatique
                Timeline autoPlayTimeline = new Timeline();
                autoPlayTimeline.setCycleCount(Timeline.INDEFINITE);
                autoPlayTimeline.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(3), e -> {
                            int newIndex = (currentIndex.get() + 1) % productImages.size();

                            fadeOut.setOnFinished(evt -> {
                                mainImageView.setImage(productImages.get(newIndex));
                                currentIndex.set(newIndex);
                                counterLabel.setText((newIndex + 1) + "/" + productImages.size());
                                fadeIn.play();
                            });
                            fadeOut.play();
                        })
                );

                // Démarrer l'animation automatique
                autoPlayTimeline.play();

                // Ajouter le compteur au carrousel
                VBox.setMargin(counterLabel, new Insets(10, 0, 0, 0));
                imageCarousel.getChildren().addAll(mainImageView, counterLabel);
            } else {
                imageCarousel.getChildren().add(mainImageView);
            }

            // Ajout du carrousel au conteneur
            popupImageContainer.getChildren().add(imageCarousel);
            popupOverlay.setVisible(true);
        }
    @FXML
    private void closeInlinePopup() {
            popupOverlay.setVisible(false);
        }
}

