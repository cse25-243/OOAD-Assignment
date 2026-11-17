public class InvestmentAccount extends Account {
    private static final double INTEREST_RATE = 0.05; // 5% monthly
    private static final double MIN_INITIAL_DEPOSIT = 500.0;

    public InvestmentAccount(String accountNumber, String branch, Customer holder, double initialDeposit) {
        super(accountNumber, branch, holder);
        if (initialDeposit >= MIN_INITIAL_DEPOSIT) {
            deposit(initialDeposit);
        } else {
            throw new IllegalArgumentException("Investment account requires a minimum initial deposit of BWP500.00");
        }
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void calculateInterest() {
        double interest = balance * INTEREST_RATE;
        deposit(interest);
    }
}