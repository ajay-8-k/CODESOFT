import java.util.ArrayList;
import java.util.List;

/**
 * Represents the customer's bank account linked with the ATM card.
 * Implements encapsulation with private fields, public methods, and manages
 * balance modifications (deposit, withdraw), PIN changes, and transaction records.
 */
public class BankAccount {
    private final String accountHolder;
    private final String accountNumber;
    private double balance;
    private String pin;
    private boolean isLocked;
    private final List<Transaction> transactionHistory;

    /**
     * Initializes the bank account with default values.
     *
     * @param accountHolder The name of the account holder
     * @param accountNumber The account number (e.g. 10 digits)
     * @param initialBalance The initial opening balance
     * @param defaultPin The initial PIN code
     */
    public BankAccount(String accountHolder, String accountNumber, double initialBalance, String defaultPin) {
        this.accountHolder = accountHolder;
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.pin = defaultPin;
        this.isLocked = false;
        this.transactionHistory = new ArrayList<>();
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    /**
     * Returns the account number masked for security (e.g., showing only the last 4 digits).
     */
    public String getMaskedAccountNumber() {
        if (accountNumber == null || accountNumber.length() < 4) {
            return "XXXX-XXXX";
        }
        return "XXXX-XXXX-" + accountNumber.substring(accountNumber.length() - 4);
    }

    public double getBalance() {
        return balance;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void lockAccount() {
        this.isLocked = true;
    }

    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory); // Return a copy for encapsulation safety
    }

    /**
     * Verifies if the entered PIN matches the account PIN.
     */
    public boolean verifyPIN(String enteredPin) {
        return this.pin.equals(enteredPin);
    }

    /**
     * Updates the account PIN to a new value.
     */
    public void changePIN(String newPin) {
        this.pin = newPin;
        // Log PIN change as a non-monetary transaction
        addTransaction(new Transaction("PIN CHANGE", 0.0, "SUCCESS", this.balance));
    }

    /**
     * Deposits a positive amount into the account.
     */
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            Transaction txn = new Transaction("DEPOSIT", amount, "SUCCESS", this.balance);
            addTransaction(txn);
        }
    }

    /**
     * Withdraws an amount from the account if funds are sufficient.
     *
     * @param amount The withdrawal amount
     * @return true if withdrawal succeeded, false otherwise
     */
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }

        if (amount > this.balance) {
            // Log failed withdrawal attempt
            Transaction txn = new Transaction("WITHDRAW", amount, "FAILED", this.balance);
            addTransaction(txn);
            return false;
        }

        this.balance -= amount;
        Transaction txn = new Transaction("WITHDRAW", amount, "SUCCESS", this.balance);
        addTransaction(txn);
        return true;
    }

    /**
     * Manually adds a transaction log entry.
     */
    public void addTransaction(Transaction txn) {
        this.transactionHistory.add(txn);
    }
}
