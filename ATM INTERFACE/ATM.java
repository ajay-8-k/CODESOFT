import java.util.List;
import java.util.Scanner;

/**
 * The ATM class handles user interaction, processes requests,
 * performs validation, and formats professional ATM-style console screens.
 */
public class ATM {
    private final BankAccount account;
    private final Scanner scanner;

    /**
     * Constructs the ATM object with a linked BankAccount.
     */
    public ATM(BankAccount account) {
        this.account = account;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the ATM service, initiating PIN authentication.
     */
    public void start() {
        printHeader("WELCOME TO APEX BANK ATM");
        if (authenticate()) {
            menuLoop();
        } else {
            printFooter("THANK YOU FOR USING APEX BANK. GOODBYE!");
        }
    }

    /**
     * Authenticates the user with up to 3 PIN entry attempts.
     */
    private boolean authenticate() {
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        System.out.println("  Card Detected: " + account.getAccountHolder());
        System.out.println("  Account No:    " + account.getMaskedAccountNumber());
        System.out.println();

        while (attempts < MAX_ATTEMPTS) {
            System.out.print("  Enter your 4-digit PIN: ");
            String enteredPin = scanner.nextLine().trim();

            if (account.verifyPIN(enteredPin)) {
                System.out.println("\n  [+] Access Granted! Loading your account...\n");
                sleep(800);
                return true;
            } else {
                attempts++;
                int remaining = MAX_ATTEMPTS - attempts;
                System.out.println("  [!] Invalid PIN. (Attempts remaining: " + remaining + ")");
                System.out.println();
            }
        }

        account.lockAccount();
        System.out.println("  [CRITICAL] Too many invalid attempts. Your account has been LOCKED.");
        System.out.println("  Please contact customer service at support@apexbank.com");
        return false;
    }

    /**
     * The main operations loop.
     */
    private void menuLoop() {
        boolean exit = false;
        while (!exit) {
            printMainMenu();
            int choice = getIntegerInput("  Select an option (1-6): ");
            System.out.println();

            switch (choice) {
                case 1:
                    checkBalance();
                    break;
                case 2:
                    depositMoney();
                    break;
                case 3:
                    withdrawMoney();
                    break;
                case 4:
                    printMiniStatement();
                    break;
                case 5:
                    changePin();
                    break;
                case 6:
                    exit = true;
                    printFooter("TRANSACTION COMPLETED. PLEASE RETRIEVE YOUR CARD.");
                    break;
                default:
                    System.out.println("  [!] Invalid selection. Please enter a number between 1 and 6.");
            }
            if (!exit) {
                System.out.print("\n  Press Enter to return to the Main Menu...");
                scanner.nextLine();
            }
        }
    }

    private void printMainMenu() {
        System.out.println("+------------------------------------------+");
        System.out.println("|                MAIN MENU                 |");
        System.out.println("+------------------------------------------+");
        System.out.println("|  [1] Check Balance                       |");
        System.out.println("|  [2] Deposit Money                       |");
        System.out.println("|  [3] Withdraw Money                      |");
        System.out.println("|  [4] Mini Statement / History            |");
        System.out.println("|  [5] Change Security PIN                 |");
        System.out.println("|  [6] Exit System                         |");
        System.out.println("+------------------------------------------+");
    }

    private void checkBalance() {
        printHeader("ACCOUNT BALANCE");
        System.out.printf("  Account Holder: %s\n", account.getAccountHolder());
        System.out.printf("  Account Number: %s\n", account.getMaskedAccountNumber());
        System.out.printf("  Current Balance: \u20B9%,.2f\n", account.getBalance());
        System.out.println("--------------------------------------------");
    }

    private void depositMoney() {
        printHeader("DEPOSIT MODULE");
        double amount = getDoubleInput("  Enter deposit amount (in \u20B9): ");

        if (amount <= 0) {
            System.out.println("  [Error] Deposit amount must be greater than zero.");
            return;
        }

        account.deposit(amount);
        System.out.printf("\n  [+] Successfully deposited \u20B9%,.2f!\n", amount);
        System.out.printf("  Updated Balance: \u20B9%,.2f\n", account.getBalance());
        System.out.println("--------------------------------------------");
    }

    private void withdrawMoney() {
        printHeader("WITHDRAWAL MODULE");
        double amount = getDoubleInput("  Enter withdrawal amount (in \u20B9): ");

        if (amount <= 0) {
            System.out.println("  [Error] Withdrawal amount must be greater than zero.");
            return;
        }

        boolean success = account.withdraw(amount);
        if (success) {
            System.out.printf("\n  [+] Dispensing Cash: \u20B9%,.2f\n", amount);
            System.out.printf("  Updated Balance: \u20B9%,.2f\n", account.getBalance());
            System.out.println("  Please collect your cash below.");
        } else {
            System.out.println("  [!] TRANSACTION FAILED: Insufficient funds.");
            System.out.printf("  Your current balance is: \u20B9%,.2f\n", account.getBalance());
        }
        System.out.println("--------------------------------------------");
    }

    private void printMiniStatement() {
        printHeader("MINI STATEMENT (RECENT TRANSACTIONS)");
        List<Transaction> txns = account.getTransactionHistory();

        if (txns.isEmpty()) {
            System.out.println("  No transactions recorded yet.");
            System.out.println("--------------------------------------------");
            return;
        }

        // Print table header
        System.out.println("+------------+---------------------+--------------+------------+----------+------------+");
        System.out.println("| Txn Ref ID | Date & Time         | Type         | Amount     | Status   | Balance    |");
        System.out.println("+------------+---------------------+--------------+------------+----------+------------+");

        // Display up to last 5 transactions for mini statement
        int start = Math.max(0, txns.size() - 5);
        for (int i = start; i < txns.size(); i++) {
            System.out.println(txns.get(i).toString());
        }

        System.out.println("+------------+---------------------+--------------+------------+----------+------------+");
        System.out.printf("  Total Transactions: %d (Showing last 5)\n", txns.size());
        System.out.printf("  Current Available Balance: \u20B9%,.2f\n", account.getBalance());
        System.out.println("--------------------------------------------");
    }

    private void changePin() {
        printHeader("PIN MANAGEMENT");
        System.out.print("  Enter your CURRENT 4-digit PIN: ");
        String currentPin = scanner.nextLine().trim();

        if (!account.verifyPIN(currentPin)) {
            System.out.println("  [!] Verification failed. PIN change aborted.");
            return;
        }

        System.out.print("  Enter your NEW 4-digit PIN: ");
        String newPin1 = scanner.nextLine().trim();

        if (newPin1.length() != 4 || !newPin1.matches("\\d+")) {
            System.out.println("  [Error] Invalid PIN. It must be exactly 4 digits.");
            return;
        }

        System.out.print("  Re-enter your NEW 4-digit PIN to confirm: ");
        String newPin2 = scanner.nextLine().trim();

        if (!newPin1.equals(newPin2)) {
            System.out.println("  [!] PINs do not match. PIN change aborted.");
            return;
        }

        account.changePIN(newPin1);
        System.out.println("\n  [+] Security PIN successfully updated!");
        System.out.println("--------------------------------------------");
    }

    // Helper method for getting robust numeric input
    private int getIntegerInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String rawInput = scanner.nextLine().trim();
            try {
                return Integer.parseInt(rawInput);
            } catch (NumberFormatException e) {
                System.out.println("  [Error] Invalid choice. Please enter a valid number.");
            }
        }
    }

    // Helper method for getting robust monetary input
    private double getDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String rawInput = scanner.nextLine().trim();
            try {
                return Double.parseDouble(rawInput);
            } catch (NumberFormatException e) {
                System.out.println("  [Error] Invalid amount. Please enter a numeric value.");
            }
        }
    }

    private void printHeader(String title) {
        System.out.println("\n=============================================");
        int paddingSize = (43 - title.length()) / 2;
        String padding = " ".repeat(Math.max(0, paddingSize));
        System.out.println(padding + title);
        System.out.println("=============================================");
    }

    private void printFooter(String message) {
        System.out.println("\n=============================================");
        int paddingSize = (43 - message.length()) / 2;
        String padding = " ".repeat(Math.max(0, paddingSize));
        System.out.println(padding + message);
        System.out.println("=============================================\n");
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}
