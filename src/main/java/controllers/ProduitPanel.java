package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProduitPanel {

    @FXML
    public Label nom;
    @FXML
    public Label prix;
    @FXML
    public Spinner quantite;
    @FXML
    public ImageView image;
    @FXML
    public Label prixTotal;
    String prixNum;

    public void setData(String nom, String prix, String quantite, String image) {
        this.nom.setText(nom);
        this.prixNum = prix;
        this.prix.setText(prixNum + " DT");

        try {
            int quantiteValue = Integer.parseInt(quantite);
            this.quantite.getValueFactory().setValue(quantiteValue);
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity: " + quantite);
        }

        try {
            this.image.setImage(new Image(image));
        } catch (Exception e) {
            System.out.println("Error loading image: " + image);
            e.printStackTrace();
        }

        updatePrixTotal();
    }


    private void updatePrixTotal() {
        try {
            int qte = (int) quantite.getValue();
            double unitPrice = Double.parseDouble(prixNum);
            double total = qte * unitPrice;
            prixTotal.setText(String.format("%.2f", total) + " DT");
        } catch (Exception e) {
            prixTotal.setText("Error");
        }
    }



    @FXML
    public void initialize() {
        quantite.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        quantite.valueProperty().addListener((obs, oldValue, newValue) -> updatePrixTotal());
    }

}
