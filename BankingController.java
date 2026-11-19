import javafx.scene.Scene;
import javafx.stage.Stage;

public class BankingController {
    private final Stage stage;
    private final LoginView loginView;
    private final AccountView accountView;
    private final Customer customer;
    private final SavingsAccount savingsAccount;
    private final InvestmentAccount investmentAccount;
    private final ChequeAccount chequeAccount;

    public BankingController(Stage stage) {
        this.stage = stage;
        this.loginView = new LoginView();
        this.accountView = new AccountView();

        this.customer = new Customer("Yash", "Joshi", "Gaborone, Botswana");
        this.savingsAccount = new SavingsAccount("SAV-001", "Main Branch", customer);
        this.investmentAccount = new InvestmentAccount("INV-001", "Main Branch", customer, 500.00);
        this.chequeAccount = new ChequeAccount("CHQ-001", "Main Branch", customer, "TechCorp", "Gaborone CBD");

        customer.addAccount(savingsAccount);
        customer.addAccount(investmentAccount);
        customer.addAccount(chequeAccount);
    }

    public void showLogin() {
        Scene loginScene = loginView.createScene(stage, username -> {
            loginView.setStatus("Welcome, " + username);
            showAccountScreen();
        });
        stage.setScene(loginScene);
        stage.show();
    }

    public void showAccountScreen() {
        refreshBalanceLabel();
        Scene accountScene = accountView.createScene(
                stage,
                amountText -> handleDeposit(amountText),
                amountText -> handleWithdraw(amountText),
                () -> handleCalculateInterest());
        stage.setScene(accountScene);
    }

    private void handleDeposit(String amountText) {
        Double amount = parsePositiveAmount(amountText);
        if (amount == null) {
            return;
        }
        getSelectedAccount().deposit(amount);
        refreshBalanceLabel();
        accountView.setStatusText("Deposited BWP " + amount + " into " + accountView.getSelectedAccountType() + ".");
    }

    private void handleWithdraw(String amountText) {
        Double amount = parsePositiveAmount(amountText);
        if (amount == null) {
            return;
        }
        boolean allowed = getSelectedAccount().withdraw(amount);
        refreshBalanceLabel();
        if (allowed) {
            accountView.setStatusText("Withdrew BWP " + amount + " from " + accountView.getSelectedAccountType() + ".");
        } else {
            accountView.setStatusText("Withdrawal declined for " + accountView.getSelectedAccountType() + " (check account rules/balance).");
        }
    }

    private void handleCalculateInterest() {
    Account selectedAccount = getSelectedAccount();
    if (selectedAccount instanceof SavingsAccount) {
        ((SavingsAccount) selectedAccount).calculateInterest();
        refreshBalanceLabel();
        accountView.setStatusText("Interest applied to SavingsAccount.");
    } else if (selectedAccount instanceof InvestmentAccount) {
        ((InvestmentAccount) selectedAccount).calculateInterest();
        refreshBalanceLabel();
        accountView.setStatusText("Interest applied to InvestmentAccount.");
    } else {
        accountView.setStatusText("ChequeAccount does not earn interest.");
    }
}

    private Account getSelectedAccount() {
        String selected = accountView.getSelectedAccountType();
        if ("InvestmentAccount".equals(selected)) {
            return investmentAccount;
        }
        if ("ChequeAccount".equals(selected)) {
            return chequeAccount;
        }
        return savingsAccount;
    }

    private Double parsePositiveAmount(String amountText) {
        try {
            double value = Double.parseDouble(amountText);
            if (value <= 0) {
                accountView.setStatusText("Enter an amount greater than 0.");
                return null;
            }
            return value;
        } catch (NumberFormatException ex) {
            accountView.setStatusText("Please enter a valid numeric amount.");
            return null;
        }
    }

    private void refreshBalanceLabel() {
        accountView.setBalanceText("Balance (" + accountView.getSelectedAccountType() + "): BWP " + getSelectedAccount().getBalance());
    }
}