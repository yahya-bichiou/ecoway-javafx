package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Depots;

public class DepotPane {

    @FXML
    public Label capacite;
    @FXML
    public Label nom;
    @FXML
    public ImageView image;
    @FXML
    public Button show;

    private Depots currentDepot; // store the depot

    public void setItemData(Depots d) {
        this.currentDepot = d;

        capacite.setText(String.valueOf(d.getCapacite()));
        nom.setText(d.getNom());
        try {
            image.setImage(new Image(d.getImage(), true));
        } catch (NullPointerException e) {
            image.setImage(null);
        }

        show.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/showFront.fxml"));
                Parent root = loader.load();

                ShowFront controller = loader.getController();
                controller.setDepot_id(currentDepot.getId());

                Scene scene = new Scene(root);
                Stage stage = (Stage) show.getScene().getWindow();
                stage.setScene(scene);
                stage.setFullScreen(true);
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
