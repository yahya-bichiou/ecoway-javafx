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


    int totalitem = 0, totalPrice = 0;
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
                                totalitem += Integer.parseInt(parts[2]);
                                totalPrice += Integer.parseInt(parts[1]);
                                controller.setData(parts[0], parts[1], parts[2], parts[3]);

                                list.getItems().add(pane);
                            }
                        }
                    }
                }
            }
            totalitems.setText(String.valueOf(totalitem));
            totalprice.setText(String.valueOf(totalPrice) + " DT");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
