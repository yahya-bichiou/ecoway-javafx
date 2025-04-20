package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProduitPanel {

    @FXML
    public Label nom;
    @FXML
    public Label prix;
    @FXML
    public Label quantite;
    @FXML
    public ImageView image;

    public void setData(String nom, String prix, String quantite, String image) {
        this.nom.setText(nom);
        this.prix.setText(prix);
        this.quantite.setText(quantite);
        try {
            //Image image2 = new Image("https://upload.wikimedia.org/wikipedia/commons/4/47/PNG_transparency_demonstration_1.png");
            this.image.setImage(new Image(image));
        } catch (Exception e) {
            System.out.println("Error loading image: " + image);
            e.printStackTrace();
        }

    }


}
