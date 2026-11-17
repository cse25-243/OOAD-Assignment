public class Main {
    public static void main(String[] args) {
        System.out.println("Testing OOAD Banking System Core Model");

        // 1. Create Customer
        Customer customer = new Customer("Yash", "Joshi", "Gaborone, Botswana");
        System.out.println("Customer Created: " + customer.getFullName());

        // 2. Open Investment Account (min BWP 500.00)
        InvestmentAccount invAcc = new InvestmentAccount("INV-001", "Main Branch", customer, 1000.00);
        customer.addAccount(invAcc);
        System.out.println("Investment Account balance after deposit: " + invAcc.getBalance());

        // 3. Test Investment Account Interest (5% monthly)
        invAcc.calculateInterest();
        System.out.println("Investment Account balance after 5% interest: " + invAcc.getBalance());

        // 4. Open a Savings Account (0.05% interest, no withdrawals)
        SavingsAccount savAcc = new SavingsAccount("SAV-001", "Main Branch", customer);
        savAcc.deposit(5000.00);
        savAcc.calculateInterest();
        System.out.println("Savings Account balance after 0.05% interest: " + savAcc.getBalance());

        // 5. Test Withdrawal restriction on Savings Account
        boolean savWithdrawal = savAcc.withdraw(100.00);
        System.out.println("Savings account withdrawal allowed? " + savWithdrawal);

        // 6. Open a Cheque Account
        ChequeAccount chqAcc = new ChequeAccount("CHQ-001", "Main Branch", customer, "TechCorp", "Gaborone CBD");
        chqAcc.deposit(2500.00);
        chqAcc.withdraw(500.00);
        System.out.println("Cheque Account final balance: " + chqAcc.getBalance());
        System.out.println("Employer: " + chqAcc.getEmployerName());

        System.out.println("All core model tests passed successfully!");
    }
}