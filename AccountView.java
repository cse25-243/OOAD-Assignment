import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AccountView {
    private final ComboBox<String> accountTypeBox = new ComboBox<>();
    private final TextField amountField = new TextField();
    private final Button depositButton = new Button("Deposit");
    private final Button withdrawButton = new Button("Withdraw");
    private final Button interestButton = new Button("Calculate Interest");
    private final Label balanceLabel = new Label("Balance: BWP 0.00");
    private final Label statusLabel = new Label();

    public Scene createScene(
            Stage stage,
            Consumer<String> onDeposit,
            Consumer<String> onWithdraw,
            Runnable onCalculateInterest) {
        Label titleLabel = new Label("Account Operations");

        accountTypeBox.getItems().addAll("SavingsAccount", "InvestmentAccount", "ChequeAccount");
        accountTypeBox.getSelectionModel().selectFirst();

        amountField.setPromptText("Amount (BWP)");

        depositButton.setOnAction(event -> {
            if (onDeposit != null) {
                onDeposit.accept(amountField.getText().trim());
            }
        });

        withdrawButton.setOnAction(event -> {
            if (onWithdraw != null) {
                onWithdraw.accept(amountField.getText().trim());
            }
        });

        interestButton.setOnAction(event -> {
            if (onCalculateInterest != null) {
                onCalculateInterest.run();
            }
        });

        HBox actionRow = new HBox(10, depositButton, withdrawButton, interestButton);
        VBox layout = new VBox(12, titleLabel, accountTypeBox, amountField, actionRow, balanceLabel, statusLabel);
        layout.setPadding(new Insets(24));

        stage.setTitle("CSE202 Banking System - Accounts");
        return new Scene(layout, 420, 280);
    }

    public String getSelectedAccountType() {
        return accountTypeBox.getValue();
    }

    public void setBalanceText(String text) {
        balanceLabel.setText(text);
    }

    public void setStatusText(String text) {
        statusLabel.setText(text);
    }
}