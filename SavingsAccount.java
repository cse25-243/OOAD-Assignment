public class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 0.0005; // 0.05% monthly

    public SavingsAccount(String accountNumber, String branch, Customer holder) {
        super(accountNumber, branch, holder);
    }

    @Override
    public boolean withdraw(double amount) {
        // Savings account does not allow withdrawals as per specification
        return false;
    }

    public void calculateInterest() {
        double interest = balance * INTEREST_RATE;
        deposit(interest);
    }
}