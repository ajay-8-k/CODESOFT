/**
 * Main application entry point for the CodSoft ATM Interface task.
 * Instantiates the default BankAccount (₹10,000 balance, PIN 1234)
 * and starts the ATM interface.
 */
public class Main {
    public static void main(String[] args) {
        // Requirements:
        // - Initial account balance of ₹10,000
        // - Default PIN of 1234
        BankAccount userAccount = new BankAccount(
                "John Doe",        // Account Holder Name
                "1029384756",      // Account Number
                10000.00,          // Initial Balance (₹10,000)
                "1234"             // Default PIN
        );

        ATM atm = new ATM(userAccount);
        atm.start();
    }
}
