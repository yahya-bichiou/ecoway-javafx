
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.user;

public class MainApp extends Application {

    public static Stage primaryStage;
    public static user connectedUser;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        loadLoginPage();
    }

    private void loadLoginPage() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/frontoffices/fxml/login.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void switchScene(String fxmlPath, String title) throws Exception {
        Parent root = FXMLLoader.load(MainApp.class.getResource(fxmlPath));
        primaryStage.setTitle(title);
        primaryStage.setScene(new Scene(root));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
