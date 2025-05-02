package backoffice.controllers;

import frontoffice.controllers.UserSession;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import models.user;
import services.userService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import java.io.FileWriter;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

// For PDF export
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class UserListController {

    @FXML
    private TableView<user> userTable;
    @FXML
    private TableColumn<user, String> nameCol;
    @FXML
    private TableColumn<user, String> emailCol;
    @FXML
    private TableColumn<user, String> rolesCol;
    @FXML
    private TableColumn<user, Void> actionCol;
    @FXML
    private ImageView profileImageView;
    @FXML
    private Label adminNameLabel;
    @FXML
    private MenuButton adminMenu;
    @FXML
    private Button closeButton;
    @FXML
    private TextField searchField;


    @FXML
    void closeApp() {
        Platform.exit();
    }

    @FXML
    void addUser(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/backofice/fxml/AddUserBack.fxml"));
            Parent root = loader.load();
            Stage addUserStage = new Stage();
            addUserStage.initModality(Modality.APPLICATION_MODAL);
            addUserStage.initOwner(((Node)event.getSource()).getScene().getWindow());

            // Set up the scene and show the popup
            Scene scene = new Scene(root);
            addUserStage.setScene(scene);
            addUserStage.setTitle("Ajouter un Utilisateur");
            addUserStage.showAndWait();


            refreshUserTable();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre d'ajout", e.getMessage());
        }
    }


    @FXML
    void viewDetails(user user) {
        if (user != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/backofice/fxml/DetailUserBack.fxml"));
                Parent root = loader.load();

                DetailUserListController detailsController = loader.getController();
                detailsController.initData(user);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Détails de l'utilisateur");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir les détails", e.getMessage());
            }
        }
    }

    @FXML
    void deleteItem(user user) {
        if (user != null) {
            try {
                if (showConfirmation("Confirmer la suppression",
                        "Supprimer l'utilisateur",
                        "Êtes-vous sûr de vouloir supprimer l'utilisateur '" + user.getName() + "' ?")) {

                    userService service = new userService();
                    service.delete(user);
                    refreshUserTable();

                    showAlert("Succès", "Utilisateur supprimé", "L'utilisateur a été supprimé avec succès.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur de suppression", "Impossible de supprimer l'utilisateur: " + e.getMessage());
            }
        }
    }

    private void refreshUserTable() {
        try {
            userService service = new userService();
            List<user> users = service.getAll();
            ObservableList<user> observableList = FXCollections.observableArrayList(users);
            userTable.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleLogout() {
        UserSession.clearSession();
        redirectToLogin();
    }


    @FXML
    public void initialize() {
        System.out.println("Initializing user table...");

        if (!UserSession.isAuthenticated()) {
            Platform.runLater(this::redirectToLogin);
            return;
        }

        user currentUser = UserSession.getInstance().getUser();
        adminNameLabel.setText(currentUser.getName());

        if (currentUser.getImageProfile() != null && !currentUser.getImageProfile().isEmpty()) {
            profileImageView.setImage(new Image("file:" + currentUser.getImageProfile()));
        }

        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());

        try {
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
            rolesCol.setCellValueFactory(new PropertyValueFactory<>("roles"));

            setupActionColumn();
            refreshUserTable();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error initializing table: " + e.getMessage());
        }
    }




    private void redirectToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontoffices/fxml/login.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();


            Stage current = (Stage) closeButton.getScene().getWindow();
            current.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToStatistics(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/backofice/fxml/Statistics.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }




    private void handleBlockUser(user user) {
        boolean newStatus = !user.isBlocked(); // toggle status
        user.setBlocked(newStatus);

        userService service = new userService();
        service.blockUser(user);

        showAlert("User Status Updated", "Block Status Changed",
                user.getName() + (newStatus ? " has been blocked." : " has been unblocked."));

        refreshUserTable();
    }



    private void setupActionColumn() {
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final HBox buttons = new HBox(5);
            private final Button viewBtn = new Button();
            private final Button editBtn = new Button();
            private final Button deleteBtn = new Button();
            private final Button blockBtn = new Button();

            {
                // Styling buttons container
                buttons.setAlignment(Pos.CENTER);

                // View button setup
                ImageView viewIcon = new ImageView(new Image(getClass().getResourceAsStream("/assets/eye.png")));
                viewIcon.setFitHeight(16);
                viewIcon.setFitWidth(16);
                viewBtn.setGraphic(viewIcon);
                viewBtn.setStyle("-fx-background-color: transparent;");
                viewBtn.setOnAction(event -> {
                    user userData = getTableView().getItems().get(getIndex());
                    viewDetails(userData);
                });
                viewBtn.setTooltip(new Tooltip("View Details"));

                // Edit button setup
                ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/assets/edit.png")));
                editIcon.setFitHeight(16);
                editIcon.setFitWidth(16);
                editBtn.setGraphic(editIcon);
                editBtn.setStyle("-fx-background-color: transparent;");
                editBtn.setOnAction(event -> {
                    user userData = getTableView().getItems().get(getIndex());
                    editItem(userData);
                });
                editBtn.setTooltip(new Tooltip("Edit User"));

                // Delete button setup
                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/assets/trash.png")));
                deleteIcon.setFitHeight(16);
                deleteIcon.setFitWidth(16);
                deleteBtn.setGraphic(deleteIcon);
                deleteBtn.setStyle("-fx-background-color: transparent;");
                deleteBtn.setOnAction(event -> {
                    user userData = getTableView().getItems().get(getIndex());
                    deleteItem(userData);
                });
                deleteBtn.setTooltip(new Tooltip("Delete User"));

                // Block button setup
                ImageView blockIcon = new ImageView(new Image(getClass().getResourceAsStream("/assets/block.jpg")));
                blockIcon.setFitHeight(16);
                blockIcon.setFitWidth(16);
                blockBtn.setGraphic(blockIcon);
                blockBtn.setStyle("-fx-background-color: transparent;");
                blockBtn.setOnAction(event -> {
                    user userData = getTableView().getItems().get(getIndex());
                    handleBlockUser(userData);
                });
                blockBtn.setTooltip(new Tooltip("Block/Unblock User"));

                buttons.getChildren().addAll(viewBtn, editBtn, deleteBtn, blockBtn);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {

                    user currentUser = getTableView().getItems().get(getIndex());
                    if (currentUser.isBlocked()) {
                        ImageView unblockIcon = new ImageView(new Image(getClass().getResourceAsStream("/assets/unblock.png")));
                        unblockIcon.setFitHeight(16);
                        unblockIcon.setFitWidth(16);
                        blockBtn.setGraphic(unblockIcon);
                        blockBtn.setTooltip(new Tooltip("Unblock User"));
                    } else {
                        ImageView blockIcon = new ImageView(new Image(getClass().getResourceAsStream("/assets/block.jpg")));
                        blockIcon.setFitHeight(16);
                        blockIcon.setFitWidth(16);
                        blockBtn.setGraphic(blockIcon);
                        blockBtn.setTooltip(new Tooltip("Block User"));
                    }
                    setGraphic(buttons);
                }
            }
        });
    }

    @FXML
    void editItem(user user) {
        if (user != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/backofice/fxml/EditUserBack.fxml"));
                Parent root = loader.load();

                EditUserListController editController = loader.getController();
                editController.setUserData(user);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Utilisateur");
                stage.showAndWait();

                refreshUserTable();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification", e.getMessage());
            }
        } else {
            showAlert("Aucune sélection", "Aucun utilisateur sélectionné", "Veuillez sélectionner un utilisateur à modifier.");
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean showConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    @FXML
    private void handleProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/backofice/fxml/AdminProfile.fxml"));
            Parent profileRoot = loader.load();
            adminMenu.getScene().setRoot(profileRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void handleEditProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/backofice/fxml/EditAdminProfile.fxml"));
            Parent root = loader.load();


            EditAdminProfileController EditAdminProfileController = loader.getController();
            EditAdminProfileController.initData(UserSession.getInstance().getUser());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier le profil");
            stage.showAndWait();

            // Optional: refresh UI elements after editing
            user updatedUser = UserSession.getInstance().getUser();
            adminNameLabel.setText(updatedUser.getName());
            if (updatedUser.getImageProfile() != null && !updatedUser.getImageProfile().isEmpty()) {
                profileImageView.setImage(new Image("file:" + updatedUser.getImageProfile()));
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la page de modification de profil", e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().trim();

        if (searchTerm.isEmpty()) {
            refreshUserTable();
            return;
        }

        try {
            userService service = new userService();
            List<user> filteredUsers = service.searchUsers(searchTerm);
            userTable.setItems(FXCollections.observableArrayList(filteredUsers));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Search Failed", "An error occurred while searching: " + e.getMessage());
        }
    }

    @FXML
    private void exportUsers(ActionEvent event) {
        try {
            // Get all users or filtered users if search is active
            List<user> usersToExport = userTable.getItems();

            if (usersToExport.isEmpty()) {
                showAlert("Export Error", "No Users to Export",
                        "There are no users available to export.");
                return;
            }

            // Create export options dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Export Users");
            dialog.setHeaderText("Choose export format");

            // Set the button types

            ButtonType excelButtonType = new ButtonType("Excel", ButtonBar.ButtonData.OK_DONE);
            ButtonType pdfButtonType = new ButtonType("PDF", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = ButtonType.CANCEL;
            dialog.getDialogPane().getButtonTypes().addAll(excelButtonType, pdfButtonType, cancelButtonType);

            // Show the dialog and wait for response
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent()) {
                ButtonType selectedType = result.get();

                if (selectedType == cancelButtonType) {
                    return;
                }

                // Create file chooser for saving the export
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Export File");

                // Set extension filter based on chosen format
                FileChooser.ExtensionFilter extFilter;
                String format = "";

                if (selectedType == excelButtonType) {
                    format = "Excel";
                    extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
                    fileChooser.getExtensionFilters().add(extFilter);
                    fileChooser.setInitialFileName("users_export.xlsx");
                } else if (selectedType == pdfButtonType) {
                    format = "PDF";
                    extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
                    fileChooser.getExtensionFilters().add(extFilter);
                    fileChooser.setInitialFileName("users_export.pdf");
                } else {
                    return;
                }

                // Show save dialog
                File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());

                if (file != null) {

                    if (format.equals("Excel")) {
                        exportToExcel(usersToExport, file);
                    } else if (format.equals("PDF")) {
                        exportToPDF(usersToExport, file);
                    }

                    showExportSuccessAlert(format, file.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Export Error", "Failed to export users", e.getMessage());
        }
    }


    private void exportToExcel(List<user> users, File file) throws IOException {

        try (FileWriter writer = new FileWriter(file)) {
            // Write CSV header
            writer.append("ID,Name,Email,Role,Status\n");

            // Write user data
            for (user u : users) {
                writer.append(String.valueOf(u.getId())).append(",");
                writer.append(u.getName().replace(",", " ")).append(","); // Avoid CSV injection
                writer.append(u.getEmail()).append(",");
                writer.append(u.getRoles()).append(",");
                writer.append(u.isBlocked() ? "Blocked" : "Active").append("\n");
            }
            writer.flush();
        }

    }

    private void exportToPDF(List<user> users, File file) throws IOException {
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("User Management System - User List", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // Add space

            // Create table
            PdfPTable table = new PdfPTable(5); // 5 columns
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Set column widths
            float[] columnWidths = {1f, 3f, 4f, 2f, 2f};
            table.setWidths(columnWidths);

            // Add table headers
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            // Add header cells
            PdfPCell idHeader = new PdfPCell(new Phrase("ID", headerFont));
            idHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
            idHeader.setBorderWidth(2);
            idHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            idHeader.setVerticalAlignment(Element.ALIGN_MIDDLE);
            idHeader.setPadding(5);
            table.addCell(idHeader);

            PdfPCell nameHeader = new PdfPCell(new Phrase("Name", headerFont));
            nameHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
            nameHeader.setBorderWidth(2);
            nameHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            nameHeader.setVerticalAlignment(Element.ALIGN_MIDDLE);
            nameHeader.setPadding(5);
            table.addCell(nameHeader);

            PdfPCell emailHeader = new PdfPCell(new Phrase("Email", headerFont));
            emailHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
            emailHeader.setBorderWidth(2);
            emailHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            emailHeader.setVerticalAlignment(Element.ALIGN_MIDDLE);
            emailHeader.setPadding(5);
            table.addCell(emailHeader);

            PdfPCell roleHeader = new PdfPCell(new Phrase("Role", headerFont));
            roleHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
            roleHeader.setBorderWidth(2);
            roleHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            roleHeader.setVerticalAlignment(Element.ALIGN_MIDDLE);
            roleHeader.setPadding(5);
            table.addCell(roleHeader);

            PdfPCell statusHeader = new PdfPCell(new Phrase("Status", headerFont));
            statusHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
            statusHeader.setBorderWidth(2);
            statusHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            statusHeader.setVerticalAlignment(Element.ALIGN_MIDDLE);
            statusHeader.setPadding(5);
            table.addCell(statusHeader);

            // Add data rows
            for (user u : users) {
                table.addCell(String.valueOf(u.getId()));
                table.addCell(u.getName());
                table.addCell(u.getEmail());
                table.addCell(u.getRoles());
                table.addCell(u.isBlocked() ? "Blocked" : "Active");
            }

            document.add(table);

            // Add export date
            document.add(new Paragraph("Export Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));

        } catch (DocumentException e) {
            e.printStackTrace();
            throw new IOException("Error creating PDF document: " + e.getMessage());
        } finally {
            document.close();
        }
    }

    private void showExportSuccessAlert(String format, String path) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export Successful");
        alert.setHeaderText("Users exported successfully");
        alert.setContentText("The user data has been exported to " + format + " format at:\n" + path);
        alert.showAndWait();
    }
}