package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Commandes;
import services.CommandeService;

import java.io.IOException;

public class CommandeFront {

    @FXML
    public Label totalprice;
    @FXML
    public Label totalitems;
    @FXML
    public Pane backgroundPane;
    @FXML
    public ChoiceBox delivery;
    @FXML
    public Label totalLivraison;
    @FXML
    public TextField promo;
    @FXML
    public Label vide;
    @FXML
    private ListView<Pane> list;

    private Commandes commande2;
    private float finalPrice;

    @FXML
    public void initialize() {
        vide.setVisible(false);
        delivery.getItems().addAll("Normal", "Express", "Same Day");
        delivery.setValue("Normal");
        delivery.setOnAction(e -> updateTotals());
        promo.textProperty().addListener((obs, oldVal, newVal) -> updateTotals());
        try {
            CommandeService cs = new CommandeService();
            for (Commandes commande : cs.select()) {
                if (commande.getClient_id() == 1 && commande.getStatus().equals("non_confirmée")) {
                    commande2 = commande;
                    String produitsString = commande.getProduits();
                    if (produitsString != null && !produitsString.isEmpty()) {
                        String[] produits = produitsString.split("%");
                        for (String produit : produits) {
                            String[] parts = produit.split("\\|");
                            if (parts.length >= 4) {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/produitPanel.fxml"));
                                Pane pane = loader.load();
                                ProduitPanel controller = loader.getController();

                                controller.setData(parts[0], parts[1], parts[2], parts[3]);

                                controller.setOnQuantityChanged(() -> {
                                    updateCommandeDatabase();
                                    updateTotals();
                                });

                                pane.setUserData(controller);
                                list.getItems().add(pane);

                                controller.setDeleteHandler(() -> {
                                    String productName = controller.nom.getText();
                                    list.getItems().remove(pane);
                                    String productsString = commande.getProduits(); // Get the original string
                                    String[] productEntries = productsString.split("%");

                                    StringBuilder updated = new StringBuilder();
                                    for (String entry : productEntries) {
                                        String[] parts2 = entry.split("\\|");
                                        if (parts2.length >= 4) {
                                            String name = parts2[0];  // Assuming the name is the first part
                                            if (!name.equals(productName)) {
                                                updated.append(entry).append("%");
                                            }
                                        }
                                    }
                                    if (updated.length() > 0 && updated.charAt(updated.length() - 1) == '%') {
                                        updated.setLength(updated.length() - 1);
                                    }

                                    commande.setProduits(updated.toString());
                                    cs.update(commande);
                                    updateTotals();
                                });
                            }
                        }
                    }
                }
            }
            if (commande2 == null){
                vide.setVisible(true);
            }
            updateTotals();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updateCommandeDatabase() {
        try {
            StringBuilder newProduits = new StringBuilder();

            for (Pane pane : list.getItems()) {
                ProduitPanel controller = (ProduitPanel) pane.getUserData();
                String nom = controller.nom.getText();
                String prix = controller.prixNum;
                int quantite = (int) controller.quantite.getValue();
                String imageUrl = controller.image.getImage().getUrl();

                newProduits.append(nom).append("|").append(prix).append("|").append(quantite).append("|").append(imageUrl).append("%");
            }

            if (newProduits.length() > 0) {
                newProduits.setLength(newProduits.length() - 1);
            }

            CommandeService cs = new CommandeService();
            for (Commandes commande : cs.select()) {
                if (commande.getClient_id() == 1) {
                    commande.setProduits(newProduits.toString());
                    cs.update(commande);
                    break;
                }
            }

            System.out.println("Commande updated in database!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTotals() {
        int newTotalItem = 0;
        float newTotalPrice = 0;

        // Calculate total items and product price
        for (Pane pane : list.getItems()) {
            ProduitPanel controller = (ProduitPanel) pane.getUserData();
            int quantite = (int) controller.quantite.getValue();
            float prixUnitaire = Float.parseFloat(controller.prixNum);
            newTotalItem += quantite;
            newTotalPrice += quantite * prixUnitaire;
        }

        // Update total items and product price (without delivery)
        totalitems.setText(String.valueOf(newTotalItem));
        totalprice.setText(newTotalPrice + " DT");

        // Get selected delivery type and add delivery cost
        finalPrice = newTotalPrice;
        String selectedDelivery = (String) delivery.getValue();

        if (selectedDelivery != null) {
            switch (selectedDelivery.toLowerCase()) {
                case "normal":
                    finalPrice += 10;
                    break;
                case "express":
                    finalPrice += 20;
                    break;
                case "same day":
                    finalPrice += 50;
                    break;
            }
        }

        String promoc = promo.getText().trim().toUpperCase();
        if (promoc.equals("PROMO10")) {
            finalPrice *= 0.9;
        } else if (promoc.equals("PROMO20")) {
            finalPrice *= 0.8;
        }

        totalLivraison.setText(finalPrice + " DT");
    }


    @FXML
    private void checkout(){
        try {
            if (commande2 == null) {
                System.out.println("No commande to confirm!");
                return;
            }
            else{
                backgroundPane.setVisible(true);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ConfirmPayment.fxml"));
                Parent root = loader.load();
                ConfirmPayment controller = loader.getController();
                controller.setCommande(commande2);
                controller.setPrice(finalPrice);
                Stage newStage = new Stage();
                newStage.setTitle("Confirm Payment");
                newStage.setScene(new Scene(root));
                newStage.initOwner(list.getScene().getWindow());
                newStage.initModality(Modality.APPLICATION_MODAL);
                newStage.initStyle(StageStyle.UNDECORATED);
                newStage.showAndWait();
                if (controller.isPaymentConfirmed()) {
                    refreshScreen();
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        backgroundPane.setVisible(false);
    }

    private void refreshScreen() {
        list.getItems().clear();
        totalitems.setText("0");
        totalprice.setText("0.0 DT");
        totalLivraison.setText("0.0 DT");
        promo.clear();
        delivery.setValue("Normal");
        commande2 = null;
        vide.setVisible(true);
    }

}
