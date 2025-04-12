package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import models.Commandes;
import services.CommandeService;
import java.time.LocalDate;

public class AddCommande {

    @FXML
    private TextField clientIdTextField;
    @FXML
    private TextField statusTextField;
    @FXML
    private TextField modeDePaiementTextField;
    @FXML
    private DatePicker dateDatePicker;
    @FXML
    private TextField prixTextField;
    @FXML
    private TextField produitsTextField;
    @FXML
    private Button submitButton;

    @FXML
    void ajouterCommandeAction() {

        int clientId = Integer.parseInt(clientIdTextField.getText());
        String status = statusTextField.getText();
        String modeDePaiement = modeDePaiementTextField.getText();
        LocalDate date = dateDatePicker.getValue();
        float prix = Float.parseFloat(prixTextField.getText());
        String produits = produitsTextField.getText();

        Commandes c = new Commandes(clientId,status,modeDePaiement,date,prix,produits);
        CommandeService cs = new CommandeService();
        cs.add(c);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Commande ajouter");
        alert.setHeaderText("Commande ajoutée avec succes!");
        alert.show();
    }
}
