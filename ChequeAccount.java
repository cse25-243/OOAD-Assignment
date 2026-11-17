public class ChequeAccount extends Account {
    private String employerName;
    private String employerAddress;

    public ChequeAccount(String accountNumber, String branch, Customer holder, String employerName, String employerAddress) {
        super(accountNumber, branch, holder);
        this.employerName = employerName;
        this.employerAddress = employerAddress;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public String getEmployerName() {
        return employerName;
    }

    public String getEmployerAddress() {
        return employerAddress;
    }
}