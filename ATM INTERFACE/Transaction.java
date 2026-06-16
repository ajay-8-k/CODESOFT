import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single transaction performed on the BankAccount.
 * This class captures critical transaction details like type, amount, status,
 * post-transaction balance, a unique Reference ID, and a timestamp.
 */
public class Transaction {
    private final String txnId;
    private final String timestamp;
    private final String type;
    private final double amount;
    private final String status;
    private final double postBalance;

    /**
     * Constructs a new Transaction.
     * Generates a unique transaction ID and captures the current system timestamp.
     *
     * @param type        The type of transaction (e.g., DEPOSIT, WITHDRAW, PIN CHANGE)
     * @param amount      The transaction amount (0 for PIN changes)
     * @param status      The outcome (e.g., SUCCESS, FAILED)
     * @param postBalance The balance in the account after the transaction was processed
     */
    public Transaction(String type, double amount, String status, double postBalance) {
        this.txnId = "TXN" + (int) (Math.random() * 900000 + 100000); // Generates a unique 6-digit number prefixed with TXN
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.postBalance = postBalance;
    }

    public String getTxnId() {
        return txnId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public double getPostBalance() {
        return postBalance;
    }

    @Override
    public String toString() {
        // Return a formatted table row for the mini statement output
        return String.format("| %-10s | %-19s | %-12s | \u20B9%-9.2f | %-8s | \u20B9%-10.2f |",
                txnId, timestamp, type, amount, status, postBalance);
    }
}
