package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.models.categorie;
import org.example.models.produit;
import org.example.services.SmsService;
import org.example.services.categorieservice;
import org.example.services.produitservice;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mysql.cj.conf.PropertyKey.logger;

public class AddProduitController {

    @FXML private TextField nomTextField;
    @FXML private TextField descriptionTextField;
    @FXML private ComboBox<String> qualiteComboBox;
    @FXML private TextField quantiteTextField;
    @FXML private TextField prixTextField;
    @FXML private DatePicker dateAjoutDatePicker;
    @FXML private ComboBox<categorie> categorieComboBox;
    @FXML private Button addButton;


    @FXML private ListView<String> imagesListView;
    @FXML private Button addImagesButton;
    @FXML private Button removeImageButton;

    private List<File> selectedImages = new ArrayList<>();
    private final produitservice produitService;
    private final categorieservice categorieService;
    public AddProduitController() throws SQLException {
        produitService = new produitservice();
        categorieService = new categorieservice();
    }

    @FXML
    public void initialize() {
        qualiteComboBox.getItems().addAll("Standard", "Premium", "Bio");
        try {
            List<categorie> categories = categorieService.getAll();
            categorieComboBox.getItems().addAll(categories);
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les catégories : " + e.getMessage());
        }

        // Configuration de la ListView pour afficher les noms des fichiers
        imagesListView.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : new File(item).getName());
            }
        });
    }

    @FXML
    private void handleAddImages() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir des images");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        List<File> files = fileChooser.showOpenMultipleDialog(addImagesButton.getScene().getWindow());

        if (files != null && !files.isEmpty()) {
            selectedImages.addAll(files);
            updateImagesListView();
        }
    }

    @FXML
    private void handleRemoveImage() {
        int selectedIndex = imagesListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            selectedImages.remove(selectedIndex);
            updateImagesListView();
        } else {
            showAlert("Aucune sélection", "Veuillez sélectionner une image à supprimer");
        }
    }

    private void updateImagesListView() {
        imagesListView.getItems().setAll(
                selectedImages.stream()
                        .map(File::getAbsolutePath)
                        .collect(Collectors.toList())
        );
    }

    @FXML
    private void handleAdd() {
        if (!validateFields()) return;

        // Copie des images dans le dossier de destination
        List<String> savedImagePaths = new ArrayList<>();
        String destDirectory = "src/main/resources/images/";

        try {
            Files.createDirectories(Paths.get(destDirectory));

            for (File imageFile : selectedImages) {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getName();
                Path destPath = Paths.get(destDirectory + fileName);
                Files.copy(imageFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
                savedImagePaths.add(fileName);
            }
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de la copie des images : " + e.getMessage());
            return;
        }

        // Création du produit
        produit prod = new produit(
                categorieComboBox.getValue().getId(),
                nomTextField.getText().trim(),
                descriptionTextField.getText().trim(),
                qualiteComboBox.getValue(),
                Integer.parseInt(quantiteTextField.getText().trim()),
                Double.parseDouble(prixTextField.getText().trim()),
                dateAjoutDatePicker.getValue().atStartOfDay(),
                String.join("|", savedImagePaths)
        );

        try {
            // Ajout du produit
            produitService.addproduit(prod);

            // ✅ Envoi du SMS après ajout réussi
            SmsService smsService = new SmsService();
            smsService.sendSms(
                    "+21656442190",  // Remplacer par numéro utilisateur
                    "Un nouveau produit est disponible dans notre boutique ! 🎉 Découvrez-le maintenant."
            );

            showSuccessAlert("Produit ajouté avec succès avec " + savedImagePaths.size() + " image(s) ! SMS envoyé !");

            // ✅ Fermer la fenêtre
            ((Stage) addButton.getScene().getWindow()).close();

        } catch (Exception e) {
            showAlert("Erreur SQL", e.getMessage());
        }
    }


    @FXML
    private void handleClear() {
        // Fermer la fenêtre lorsqu'on clique sur Annuler
        ((Stage) nomTextField.getScene().getWindow()).close();
    }

    private void clearFields() {
        categorieComboBox.setValue(null);
        nomTextField.clear();
        descriptionTextField.clear();
        qualiteComboBox.setValue(null);
        quantiteTextField.clear();
        prixTextField.clear();
        dateAjoutDatePicker.setValue(null);
        selectedImages.clear();
        imagesListView.getItems().clear();
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        if (nomTextField.getText().trim().isEmpty()) errors.append("- Le nom est requis.\n");
        if (descriptionTextField.getText().trim().isEmpty()) errors.append("- La description est requise.\n");
        if (qualiteComboBox.getValue() == null) errors.append("- La qualité est requise.\n");
        if (categorieComboBox.getValue() == null) errors.append("- La catégorie est requise.\n");
        if (dateAjoutDatePicker.getValue() == null) errors.append("- La date d'ajout est requise.\n");
        if (selectedImages.isEmpty()) errors.append("- Au moins une image est requise.\n");

        try {
            Integer.parseInt(quantiteTextField.getText().trim());
        } catch (NumberFormatException e) {
            errors.append("- Quantité invalide.\n");
        }

        try {
            Double.parseDouble(prixTextField.getText().trim());
        } catch (NumberFormatException e) {
            errors.append("- Prix invalide.\n");
        }

        if (errors.length() > 0) {
            showAlert("Erreur de validation", errors.toString());
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}