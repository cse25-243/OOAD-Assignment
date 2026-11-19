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
}