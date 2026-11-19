import javafx.application.Application;
import javafx.stage.Stage;

public class BankingApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        BankingController controller = new BankingController(primaryStage);
        controller.showLogin();
    }

    public static void main(String[] args) {
        launch(args);
    }
}