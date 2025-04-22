package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import models.Commandes;
import services.CommandeService;

public class CommandeFront {

    @FXML
    public Label totalprice;
    @FXML
    public Label totalitems;
    @FXML
    private ListView<Pane> list;

    private ObservableList<Commandes> items = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            CommandeService cs = new CommandeService();
            for (Commandes commande : cs.select()) {
                if (commande.getClient_id() == 1) {
                    String produitsString = commande.getProduits();
                    if (produitsString != null && !produitsString.isEmpty()) {
                        String[] produits = produitsString.split("%");

                        for (String produit : produits) {
                            String[] parts = produit.split("-");
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
                            }
                        }
                    }
                }
            }
            // Calculate the totals after everything is loaded
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

                newProduits.append(nom).append("-").append(prix).append("-").append(quantite).append("-").append(imageUrl).append("%");
            }

            if (newProduits.length() > 0) {
                newProduits.setLength(newProduits.length() - 1);  // remove the last '%'
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
        int newTotalPrice = 0;

        for (Pane pane : list.getItems()) {
            ProduitPanel controller = (ProduitPanel) pane.getUserData();
            int quantite = (int) controller.quantite.getValue();
            int prixUnitaire = Integer.parseInt(controller.prixNum);

            newTotalItem += quantite;
            newTotalPrice += quantite * prixUnitaire;
        }

        totalitems.setText(String.valueOf(newTotalItem));
        totalprice.setText(newTotalPrice + " DT");
    }
}
