package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import models.Commandes;
import services.CommandeService;
import services.StripeService;
import services.StripeSessionResponse;
import java.net.URI;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class ConfirmPayment {

    @FXML
    public ChoiceBox<String> choice;

    private Commandes commande;
    private CommandeService commandeService = new CommandeService();
    private float price;
    private boolean paymentConfirmed = false;
    private String paymentSessionId;

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

        if (paymentMode.equalsIgnoreCase("Cash")) {
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
            try {
                StripeService stripeService = new StripeService();
                long finalPrice = (long) (price * 100);
                StripeSessionResponse sessionResponse = stripeService.createCheckoutSession(finalPrice);
                Desktop.getDesktop().browse(new URI(sessionResponse.getUrl()));
                startPaymentStatusCheck(sessionResponse.getSessionId());
                paymentConfirmed = true;
                closeApp();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Stripe Payment Failed.");
            }
        }
    }

    public void startPaymentStatusCheck(String sessionId) {
        Timer timer = new Timer();
        StripeService stripeService = new StripeService();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    boolean paid = stripeService.isSessionPaid(sessionId);
                    if (paid) {
                        System.out.println("✅ Payment successful!");
                        commande.setStatus("payée");
                        commande.setPrix(price);
                        commande.setMode_paiement("Card");
                        commandeService.update(commande);
                        paymentConfirmed = true;
                        ExportPDF.generateFacture(commande, "facture_" + commande.getId() + ".pdf");
                        timer.cancel();
                    } else {
                        System.out.println("⏳ Payment not completed yet...");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    timer.cancel();
                }
            }
        }, 0, 2000); // Delay 0ms, repeat every 2000ms (2 seconds)
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
