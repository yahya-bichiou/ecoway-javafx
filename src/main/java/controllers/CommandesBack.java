package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Commandes;
import models.Livraisons;
import services.CommandeService;
import services.LivraisonService;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;


import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandesBack {

    public Pane backgroundPane;

    @FXML
    public CategoryAxis xAxis;
    @FXML
    public NumberAxis yAxis;
    @FXML
    public LineChart chart;
    @FXML
    public TextField searchField2;
    @FXML
    public Button sortButton;
    @FXML
    public Label summaryLabel;
    @FXML
    private TableView<Commandes> commandeTableView;
    @FXML
    private TableColumn<Commandes, Integer> clientIdCol;
    @FXML
    private TableColumn<Commandes, String> statusCol;
    @FXML
    private TableColumn<Commandes, String> modeCol;
    @FXML
    private TableColumn<Commandes, LocalDate> dateCol;
    @FXML
    private TableColumn<Commandes, Float> prixCol;
    @FXML
    private TableColumn<Commandes, String> produitsCol;

    @FXML
    private TableView<Livraisons> livraisonTableView;
    @FXML
    private TableColumn<Livraisons, Integer> commande;
    @FXML
    private TableColumn<Livraisons, String> livreur;
    @FXML
    private TableColumn<Livraisons, String> adresse;
    @FXML
    private TableColumn<Livraisons, LocalDate> date;
    @FXML
    private TableColumn<Livraisons, String> status;
    @FXML
    private TableColumn<Livraisons, String> mode;
    @FXML
    private TableColumn<Livraisons, Float> prix;

    private boolean sortByDate = true;
    private ObservableList<Commandes> commandeList = FXCollections.observableArrayList();

    @FXML
    private Button closeButton;

    @FXML
    void closeApp() {
        Platform.exit();
    }


    @FXML
    void deleteCommande() {
        Commandes selectedCommande = commandeTableView.getSelectionModel().getSelectedItem();
        if (selectedCommande != null) {
            CommandeService commandeService = new CommandeService();
            commandeService.delete(selectedCommande);
            commandeTableView.getItems().remove(selectedCommande);
            LivraisonService livraisonService = new LivraisonService();
            livraisonService.delete(selectedCommande.getId());
            Livraisons toDelete = null;
            for (Livraisons l : livraisonTableView.getItems()) {
                if (l.getCommande_id() == selectedCommande.getId()) {
                    toDelete = l;
                    break;
                }
            }
            livraisonTableView.getItems().remove(toDelete);
            System.out.println("Commande deleted successfully!");
        } else {
            System.out.println("Please select a commande to delete.");
        }
    }

    @FXML
    void deleteLivraison() {
        Livraisons selectedLivraison = livraisonTableView.getSelectionModel().getSelectedItem();
        if (selectedLivraison != null) {
            LivraisonService livraisonService = new LivraisonService();
            livraisonService.delete(selectedLivraison.getId());
            livraisonTableView.getItems().remove(selectedLivraison);
            System.out.println("Livraison deleted successfully!");
        } else {
            System.out.println("Please select a commande to delete.");
        }
    }

    @FXML
    void editCommande() {
        Commandes selectedCommande = commandeTableView.getSelectionModel().getSelectedItem();
        if (selectedCommande != null) {
            backgroundPane.setVisible(true);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditCommande.fxml"));
                Parent root = loader.load();
                EditCommande editController = loader.getController();
                editController.setCommande(selectedCommande);
                Stage newStage = new Stage();
                newStage.setTitle("Modifier Commande");
                newStage.setScene(new Scene(root));
                newStage.initOwner(closeButton.getScene().getWindow());
                newStage.initModality(Modality.APPLICATION_MODAL);
                newStage.initStyle(StageStyle.UNDECORATED);
                newStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
            CommandeService cs = new CommandeService();
            List<Commandes> commandes = cs.getAll();
            ObservableList<Commandes> observableList2 = FXCollections.observableArrayList(commandes);
            commandeTableView.setItems(observableList2);
            commandeTableView.refresh();
            backgroundPane.setVisible(false);
        } else {
            System.out.println("Please select a commande to edit.");
        }
    }

    @FXML
    void editLivraison() {
        Livraisons selectedLivraison = livraisonTableView.getSelectionModel().getSelectedItem();

        if (selectedLivraison != null) {
            backgroundPane.setVisible(true);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditLivraison.fxml"));
                Parent root = loader.load();
                EditLivraison editController = loader.getController();
                editController.setLivraison(selectedLivraison);
                Stage newStage = new Stage();
                newStage.setTitle("Edit Livraison");
                newStage.setScene(new Scene(root));
                newStage.initOwner(closeButton.getScene().getWindow());
                newStage.initModality(Modality.APPLICATION_MODAL);
                newStage.initStyle(StageStyle.UNDECORATED);
                newStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
            LivraisonService cs = new LivraisonService();
            List<Livraisons> livraisons = cs.getAll();
            ObservableList<Livraisons> observableList2 = FXCollections.observableArrayList(livraisons);
            livraisonTableView.setItems(observableList2);
            livraisonTableView.refresh();
            backgroundPane.setVisible(false);
        } else {
            System.out.println("Please select a livraison to edit.");
        }
    }

    @FXML
    public void initialize() {
        System.out.println("Initializing commande table...");
        try {
            clientIdCol.setCellValueFactory(new PropertyValueFactory<>("client_id"));
            statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
            modeCol.setCellValueFactory(new PropertyValueFactory<>("mode_paiement"));
            dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));
            produitsCol.setCellValueFactory(new PropertyValueFactory<>("produits"));

            // Load data
            CommandeService cs = new CommandeService();
            commandeList = FXCollections.observableArrayList(cs.getAll());
            commandeList.sort(Comparator.comparing(Commandes::getDate));
            commandeTableView.setItems(commandeList);
            sortButton.setText("Date");

            System.out.println("Table initialized successfully.");

        } catch (Exception e) {
            System.out.println("ERROR during table initialization: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Initializing livraison table...");
        try {
            commande.setCellValueFactory(new PropertyValueFactory<>("commande_id"));
            livreur.setCellValueFactory(new PropertyValueFactory<>("livreur"));
            adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            status.setCellValueFactory(new PropertyValueFactory<>("status"));
            mode.setCellValueFactory(new PropertyValueFactory<>("mode"));
            prix.setCellValueFactory(new PropertyValueFactory<>("prix"));

            // Load data
            LivraisonService ls = new LivraisonService();
            ObservableList<Livraisons> livraisonList = FXCollections.observableArrayList(ls.getAll());
            FilteredList<Livraisons> filteredData = new FilteredList<>(livraisonList, b -> true);
            searchField2.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(livraison -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (livraison.getAdresse().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (livraison.getStatus().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }

                    return false;
                });
            });
            SortedList<Livraisons> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(livraisonTableView.comparatorProperty());
            livraisonTableView.setItems(sortedData);

            System.out.println("Table initialized successfully.");

            statsLivraison();

        } catch (Exception e) {
            System.out.println("ERROR during table initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void livrerCommande(ActionEvent actionEvent) {
        Commandes selectedCommande = commandeTableView.getSelectionModel().getSelectedItem();
        if (selectedCommande != null) {
            backgroundPane.setVisible(true);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddLivraison.fxml"));
                Parent root = loader.load();
                AddLivraison controller = loader.getController();
                controller.setCommande(selectedCommande);
                Stage newStage = new Stage();
                newStage.setTitle("Ajouter Livraison");
                newStage.setScene(new Scene(root));
                newStage.initOwner(closeButton.getScene().getWindow());
                newStage.initModality(Modality.APPLICATION_MODAL);
                newStage.initStyle(StageStyle.UNDECORATED);
                newStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
            selectedCommande.setStatus("en livraison");
            CommandeService commandeService = new CommandeService();
            commandeService.update(selectedCommande);
            commandeTableView.refresh();
            LivraisonService cs = new LivraisonService();
            List<Livraisons> livraisons = cs.getAll();
            ObservableList<Livraisons> observableList2 = FXCollections.observableArrayList(livraisons);
            livraisonTableView.setItems(observableList2);
            livraisonTableView.refresh();
            statsLivraison();
            backgroundPane.setVisible(false);
        } else {
            System.out.println("Please select a commande to mark as 'en livraison'.");
        }
    }

    public void statsLivraison() {
        CommandeService cs = new CommandeService();
        List<Commandes> commandes = cs.getAll();
        LivraisonService ls = new LivraisonService();
        List<Livraisons> livraisons = ls.getAll();

        Map<String, Long> expectedDeliveryTimes = new HashMap<>();
        expectedDeliveryTimes.put("Normal", 5L);
        expectedDeliveryTimes.put("Express", 3L);
        expectedDeliveryTimes.put("Same Day", 0L);

        // Map: DeliveryType -> (WeekNumber -> List of delays)
        Map<String, Map<Integer, List<Long>>> delaysByTypeAndWeek = new HashMap<>();
        // Map: DeliveryType -> List of all delays (for global stats)
        Map<String, List<Long>> delaysByType = new HashMap<>();

        for (Commandes commande : commandes) {
            for (Livraisons livraison : livraisons) {
                if (commande.getDate() != null && livraison.getDate() != null && livraison.getCommande_id() == commande.getId()) {
                    String deliveryType = livraison.getMode();
                    long expectedDays = expectedDeliveryTimes.getOrDefault(deliveryType, 0L);

                    Duration duration = Duration.between(commande.getDate().atStartOfDay(), livraison.getDate().atStartOfDay());
                    long actualDays = duration.toDays();
                    long delay = actualDays > expectedDays ? actualDays - expectedDays : 0;

                    int weekNumber = commande.getDate().get(WeekFields.ISO.weekOfWeekBasedYear());

                    delaysByTypeAndWeek
                            .computeIfAbsent(deliveryType, k -> new HashMap<>())
                            .computeIfAbsent(weekNumber, k -> new ArrayList<>())
                            .add(delay);

                    delaysByType
                            .computeIfAbsent(deliveryType, k -> new ArrayList<>())
                            .add(delay);
                }
            }
        }

        chart.getData().clear();

        for (String deliveryType : expectedDeliveryTimes.keySet()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(deliveryType);

            Map<Integer, List<Long>> weekDelays = delaysByTypeAndWeek.get(deliveryType);

            if (weekDelays != null) {
                for (Map.Entry<Integer, List<Long>> entry : weekDelays.entrySet()) {
                    int week = entry.getKey();
                    List<Long> delays = entry.getValue();
                    double averageDelay = delays.stream().mapToLong(Long::longValue).average().orElse(0);

                    series.getData().add(new XYChart.Data<>("Week " + week, averageDelay));
                }
            }

            chart.getData().add(series);
        }

        generateSummaryStats(delaysByType);
    }


    private void generateSummaryStats(Map<String, List<Long>> delaysByType) {
        double totalDelay = 0;
        int totalCount = 0;

        String bestType = "";
        String worstType = "";
        double bestAvg = Double.MAX_VALUE;
        double worstAvg = Double.MIN_VALUE;

        StringBuilder details = new StringBuilder();

        for (Map.Entry<String, List<Long>> entry : delaysByType.entrySet()) {
            String type = entry.getKey();
            List<Long> delays = entry.getValue();

            double average = delays.stream().mapToLong(Long::longValue).average().orElse(0);
            totalDelay += delays.stream().mapToLong(Long::longValue).sum();
            totalCount += delays.size();

            details.append(type).append(": ").append(String.format("%.2f", average)).append(" days\n");

            if (average < bestAvg) {
                bestAvg = average;
                bestType = type;
            }

            if (average > worstAvg) {
                worstAvg = average;
                worstType = type;
            }
        }

        double globalAverage = totalCount > 0 ? totalDelay / totalCount : 0;

        String summary = "Retard Global:    " + String.format("%.2f", globalAverage) + " days\n\n" +
                "Best Type:     " + bestType + " (" + String.format("%.2f", bestAvg) + " days)\n" +
                "Worst Type:    " + worstType + " (" + String.format("%.2f", worstAvg) + " days)\n\n" +
                "Details ( en moyenne ):\n" + details.toString();
        summaryLabel.setText(summary);
    }


    @FXML
    private void toggleSort() {
        if (sortByDate) {
            commandeList.sort(Comparator.comparing(Commandes::getPrix));
            sortButton.setText("Prix");
        } else {
            commandeList.sort(Comparator.comparing(Commandes::getDate));
            sortButton.setText("Date");
        }

        // Refresh the table view
        commandeTableView.refresh();

        // Toggle the flag
        sortByDate = !sortByDate;
    }

}
