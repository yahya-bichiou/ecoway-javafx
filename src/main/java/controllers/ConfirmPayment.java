package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import models.Commandes;
import services.CommandeService;

public class ConfirmPayment {

    @FXML
    public ChoiceBox<String> choice;

    private Commandes commande;
    private CommandeService commandeService = new CommandeService();
    private float price;
    private boolean paymentConfirmed = false;

    @FXML
    public void closeApp() {
        Stage stage = (Stage) choice.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        choice.getItems().addAll( "Card", "Cash (On Delivery)");
        choice.setValue("Card");
    }


    @FXML
    public void confirmPayment() {
        if (commande == null) {
            System.out.println("No commande to confirm!");
            return;
        }

        String paymentMode = (String) choice.getValue();

        commande.setPrix(price);
        commande.setMode_paiement(paymentMode);

        if (paymentMode.equalsIgnoreCase("Cash (On Delivery)")) {
            // 🛒 Cash: Just confirm and close
            commande.setStatus("confirmée");
            try {
                commandeService.update(commande);
                System.out.println("Commande confirmed successfully (Cash)!");
                paymentConfirmed = true;
                ExportPDF.generateFacture(commande, "facture_" + commande.getId() + ".pdf");
                closeApp();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to update commande (Cash).");
            }

        } else if (paymentMode.equalsIgnoreCase("Card")) {
            // 💳 Card: Call Stripe Payment
            try {


                // After payment is successful (for now simulate as success)
                commande.setStatus("payée"); // You can mark it "payée" after payment
                commandeService.update(commande);
                System.out.println("Commande confirmed successfully (Card)!");
                paymentConfirmed = true;
                ExportPDF.generateFacture(commande, "facture_" + commande.getId() + ".pdf");
                closeApp();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Stripe Payment Failed.");
            }
        }
    }


    public void setCommande(Commandes commande) {
        this.commande = commande;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isPaymentConfirmed() {
        return paymentConfirmed;
    }
}
