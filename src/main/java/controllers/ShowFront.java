package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import models.Collectes;
import models.Depots;
import services.CollectesServices;
import services.DepotsServices;
import services.MailSender;

import java.util.List;

public class ShowFront {

    @FXML
    public Label name;
    @FXML
    public Label adresse;
    @FXML
    public Label capacite;
    @FXML
    public ImageView image;
    @FXML
    public TextField quantiteTextField;
    @FXML
    public TextField responsableTextField;
    @FXML
    public Button save;
    @FXML
    public DatePicker date;
    @FXML
    public Label error;
    @FXML
    public WebView mapView;
    @FXML
    public Label notif;
    @FXML
    public Pane notifBar;

    private Depots depot;
    int reste;


    private int depot_id;

    public void setDepot_id(int id) {
        this.depot_id = id;
        System.out.println("Received depot ID: " + depot_id);
        DepotsServices ds = new DepotsServices();
        depot = ds.getDepotById(depot_id);
        init();
    }

    @FXML
    public void createCollecte() {
        error.setText("");

        try {
            String quantiteStr = quantiteTextField.getText().trim();
            String responsable = responsableTextField.getText().trim();

            if (quantiteStr.isEmpty() || responsable.isEmpty() || date.getValue() == null) {
                error.setText("Veuillez remplir tous les champs.");
                return;
            }

            double quantite = Double.parseDouble(quantiteStr);

            if (quantite <= 0) {
                error.setText("La quantité doit être un nombre positif.");
                return;
            }

            if (date.getValue().isBefore(java.time.LocalDate.now())) {
                error.setText("La date doit être dans le futur.");
                return;
            }

            Collectes collecte = new Collectes();
            collecte.setDepot_id(depot_id);
            collecte.setQuantite(quantite);
            collecte.setResponsable(responsable);
            collecte.setDate(date.getValue());
            CollectesServices cs = new CollectesServices();
            if(collecte.getQuantite()<=reste) {
                notif.setText("collecte ajoutée avec succés");
                notif.setStyle("-fx-text-fill: green;");
                notifBar.setVisible(true);
                javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(5));
                pause.setOnFinished(event -> notifBar.setVisible(false));
                pause.play();
                cs.add(collecte);
                //mail
                reste -= collecte.getQuantite();
                System.out.println(reste);
                if (reste <= 0) {
                    MailSender mailSender = new MailSender();
                    mailSender.sendMail("yahyabichiou2003@gmail.com", depot.getId());
                }
                quantiteTextField.clear();
                responsableTextField.clear();
                date.setValue(null);
            }
            else {
                notif.setStyle("-fx-text-fill: red;");
                notif.setText("Le depot est plein");
                notifBar.setVisible(true);
                javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(5));
                pause.setOnFinished(event -> notifBar.setVisible(false));
                pause.play();
            }

        } catch (NumberFormatException e) {
            error.setText("La quantité doit être un nombre valide !");
        } catch (Exception e) {
            error.setText("Erreur lors de l'ajout de la collecte.");
            e.printStackTrace();
        }
    }

    public void init() {
        if (depot == null || depot.getAdresse() == null) {
            System.out.println("No depot or adresse provided");
            return;
        }

        try {
            reste = depot.getCapacite();
            CollectesServices cs = new CollectesServices();
            List<Collectes> collecteList2 = cs.select();
            for (Collectes collecte : collecteList2) {
                if (collecte.getDepot_id() == depot.getId()) {
                    reste -= collecte.getQuantite();
                }
            }

            if (depot != null) {
                image.setImage(new Image(depot.getImage(), true));
                name.setText(depot.getNom());
                adresse.setText(depot.getAdresse());
                capacite.setText(String.valueOf(depot.getCapacite()));
            } else {
                System.out.println("Depot not found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        notifBar.setVisible(false);

        // Split the address
        String adresse = depot.getAdresse(); // Example: "10.1815,36.8065"
        String[] parts = adresse.split(",");
        double latitude = Double.parseDouble(parts[0].trim());
        double longitude = Double.parseDouble(parts[1].trim());

        // Generate HTML with coordinates
        WebEngine webEngine = mapView.getEngine();
        String content = """
    <!DOCTYPE html>
    <html>
    <head>
        <meta charset="utf-8" />
        <title>Simple Leaflet Map</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://unpkg.com/leaflet@1.7.1/dist/leaflet.css" />
        <script src="https://unpkg.com/leaflet@1.7.1/dist/leaflet.js"></script>
        <style>
            html, body, #map {
                height: 100%;
                margin: 0;
                padding: 0;
            }
        </style>
    </head>
    <body>
        <div id="map"></div>
        <script>
            var map = L.map('map').setView([""" + latitude + ", " + longitude + """
            ], 13);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '© OpenStreetMap contributors'
            }).addTo(map);

            var marker = L.marker([""" + latitude + ", " + longitude + """
                ]).addTo(map)
                .bindPopup('Depot Location')
                .openPopup();
        </script>
    </body>
    </html>
""";


        webEngine.loadContent(content);
    }
}

