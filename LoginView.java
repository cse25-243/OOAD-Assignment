import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {
    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Button loginButton = new Button("Login");
    private final Label statusLabel = new Label();

    public Scene createScene(Stage stage, Consumer<String> onLoginSubmit) {
        Label titleLabel = new Label("Banking System Login");
        usernameField.setPromptText("Username or Customer ID");
        passwordField.setPromptText("Password");

        loginButton.setOnAction(event -> {
            String user = usernameField.getText().trim();
            if (user.isEmpty()) {
                setStatus("Please enter a username.");
                return;
            }
            if (onLoginSubmit != null) {
                onLoginSubmit.accept(user);
            }
        });

        VBox layout = new VBox(12, titleLabel, usernameField, passwordField, loginButton, statusLabel);
        layout.setPadding(new Insets(24));
        layout.setAlignment(Pos.CENTER);

        stage.setTitle("CSE202 Banking System - Login");
        return new Scene(layout, 360, 240);
    }

    public void setStatus(String message) {
        statusLabel.setText(message);
    }
}