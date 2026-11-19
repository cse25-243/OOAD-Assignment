import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:banking.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        String createAccountsTable = """
                CREATE TABLE IF NOT EXISTS accounts (
                    account_number TEXT PRIMARY KEY,
                    account_type TEXT NOT NULL,
                    branch TEXT NOT NULL,
                    holder_name TEXT NOT NULL,
                    balance REAL NOT NULL
                );
                """;

        String createTransactionsTable = """
                CREATE TABLE IF NOT EXISTS transactions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    account_number TEXT NOT NULL,
                    tx_type TEXT NOT NULL,
                    amount REAL NOT NULL,
                    balance_after REAL NOT NULL,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP
                );
                """;

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(createAccountsTable);
            stmt.execute(createTransactionsTable);
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }

    public static void saveAccountBalance(String accountNumber, String accountType, String branch, String holderName, double balance) {
        String upsertSql = """
                INSERT INTO accounts (account_number, account_type, branch, holder_name, balance)
                VALUES (?, ?, ?, ?, ?)
                ON CONFLICT(account_number) DO UPDATE SET
                    account_type = excluded.account_type,
                    branch = excluded.branch,
                    holder_name = excluded.holder_name,
                    balance = excluded.balance;
                """;

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(upsertSql)) {
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, accountType);
            pstmt.setString(3, branch);
            pstmt.setString(4, holderName);
            pstmt.setDouble(5, balance);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving account balance: " + e.getMessage());
        }
    }

    public static void recordTransaction(String accountNumber, String txType, double amount, double balanceAfter) {
        String insertTxSql = """
                INSERT INTO transactions (account_number, tx_type, amount, balance_after)
                VALUES (?, ?, ?, ?);
                """;

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(insertTxSql)) {
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, txType);
            pstmt.setDouble(3, amount);
            pstmt.setDouble(4, balanceAfter);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error recording transaction: " + e.getMessage());
        }
    }

    public static Double loadAccountBalance(String accountNumber) {
        String selectSql = "SELECT balance FROM accounts WHERE account_number = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
            pstmt.setString(1, accountNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("balance");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading account balance: " + e.getMessage());
        }
        return null;
    }
    public static int getAccountCount() {
        String countSql = "SELECT COUNT(*) AS total FROM accounts";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(countSql)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error counting accounts: " + e.getMessage());
        }
        return 0;
    }

    public static void updateAccountBalance(String accountNumber, double newBalance) {
        String updateSql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, accountNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating account balance: " + e.getMessage());
        }
    }

    public static void deleteAccount(String accountNumber) {
        String deleteTxSql = "DELETE FROM transactions WHERE account_number = ?";
        String deleteAccSql = "DELETE FROM accounts WHERE account_number = ?";
        try (Connection conn = connect()) {
            try (PreparedStatement txStmt = conn.prepareStatement(deleteTxSql)) {
                txStmt.setString(1, accountNumber);
                txStmt.executeUpdate();
            }
            try (PreparedStatement accStmt = conn.prepareStatement(deleteAccSql)) {
                accStmt.setString(1, accountNumber);
                accStmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error deleting account: " + e.getMessage());
        }
    }

    public static void seedSampleRecords() {
        initializeDatabase();
        String[][] sampleAccounts = {
            {"SAV-001", "SavingsAccount", "Main Branch", "Yash Joshi", "1200.00"},
            {"INV-001", "InvestmentAccount", "Main Branch", "Yash Joshi", "2500.00"},
            {"CHQ-001", "ChequeAccount", "Main Branch", "Yash Joshi", "1800.00"},
            {"SAV-002", "SavingsAccount", "Gaborone CBD", "Oliver Bennett", "950.00"},
            {"INV-002", "InvestmentAccount", "Gaborone CBD", "Oliver Bennett", "3200.00"},
            {"CHQ-002", "ChequeAccount", "Gaborone CBD", "Oliver Bennett", "1500.00"},
            {"SAV-003", "SavingsAccount", "Riverwalk Branch", "Hannah Brooks", "600.00"},
            {"INV-003", "InvestmentAccount", "Riverwalk Branch", "Hannah Brooks", "1400.00"},
            {"CHQ-003", "ChequeAccount", "Riverwalk Branch", "Liam Carter", "2100.00"},
            {"SAV-004", "SavingsAccount", "Main Branch", "Liam Carter", "800.00"}
        };

        for (String[] row : sampleAccounts) {
            String accNum = row[0];
            String accType = row[1];
            String branch = row[2];
            String holder = row[3];
            double bal = Double.parseDouble(row[4]);

            if (loadAccountBalance(accNum) == null) {
                saveAccountBalance(accNum, accType, branch, holder, bal);
                recordTransaction(accNum, "INITIAL_SEED", bal, bal);
            }
        }
    }
}