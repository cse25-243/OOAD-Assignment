public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected Customer holder;

    public Account(String accountNumber, String branch, Customer holder) {
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.holder = holder;
        this.balance = 0.0;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public abstract boolean withdraw(double amount);

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}