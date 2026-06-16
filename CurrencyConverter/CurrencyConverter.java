

package CurrencyConverter;

import java.util.Scanner;

/**
 * CurrencyConverter is the entry point of the CLI application.
 * It manages the user interface, console loop, and combines utilities and services
 * to perform real-time currency conversions.
 */
public class CurrencyConverter {

    private final ExchangeRateService rateService;
    private final Scanner scanner;

    public CurrencyConverter() {
        this.rateService = new ExchangeRateService();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the main application execution loop.
     */
    public void start() {
        printWelcomeMessage();
        boolean running = true;

        while (running) {
            // Step 1: Display supported currencies to guide the user
            CurrencyUtils.printSupportedCurrencies();

            // Step 2: Prompt and read Base Currency
            String baseCurrency = promptCurrency("Enter BASE currency code (e.g., USD): ");

            // Step 3: Prompt and read Target Currency
            String targetCurrency = promptCurrency("Enter TARGET currency code (e.g., EUR): ");

            // Step 4: Prompt and read Amount to convert
            double amount = promptAmount();

            // Step 5: Perform Fetching and Conversion
            System.out.println("\nConnecting to API to fetch live rates...");
            try {
                double rate = rateService.fetchExchangeRate(baseCurrency, targetCurrency);
                double convertedAmount = amount * rate;

                // Step 6: Display the beautifully formatted conversion summary
                printConversionSummary(baseCurrency, targetCurrency, amount, convertedAmount, rate);

            } catch (Exception e) {
                printErrorMessage("Conversion failed: " + e.getMessage());
            }

            // Step 7: Check if the user wants to continue
            running = promptContinue();
        }

        printGoodbyeMessage();
    }

    /**
     * Prompts the user for a currency code, validating that it is supported.
     *
     * @param promptMessage The instruction displayed to the user.
     * @return A valid, uppercase currency code.
     */
    private String promptCurrency(String promptMessage) {
        while (true) {
            System.out.print(promptMessage);
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
                continue;
            }

            if (CurrencyUtils.isSupportedCurrency(input)) {
                return input;
            } else {
                printErrorMessage("Unsupported currency code '" + input + "'. Please choose from the list above.");
            }
        }
    }

    /**
     * Prompts the user for a valid, positive conversion amount.
     *
     * @return A valid positive double amount.
     */
    private double promptAmount() {
        while (true) {
            System.out.print("Enter amount to convert: ");
            String input = scanner.nextLine().trim();

            try {
                double amount = Double.parseDouble(input);
                if (amount < 0) {
                    printErrorMessage("Amount cannot be negative. Please enter a positive number.");
                    continue;
                }
                return amount;
            } catch (NumberFormatException e) {
                printErrorMessage("Invalid input formatting. Please enter a valid number (e.g., 100.50).");
            }
        }
    }

    /**
     * Prompts the user if they wish to perform another conversion.
     *
     * @return true if the user wants to continue, false if they want to exit.
     */
    private boolean promptContinue() {
        while (true) {
            System.out.print("\nDo you want to perform another conversion? (yes/no or y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("yes") || input.equals("y")) {
                System.out.println();
                return true;
            } else if (input.equals("no") || input.equals("n")) {
                return false;
            } else {
                printErrorMessage("Invalid choice. Please enter 'yes' or 'no'.");
            }
        }
    }

    /**
     * Outputs a conversion summary table.
     */
    private void printConversionSummary(String base, String target, double originalAmount, double convertedAmount, double rate) {
        String baseName = CurrencyUtils.getCurrencyName(base);
        String targetName = CurrencyUtils.getCurrencyName(target);
        String baseSymbol = CurrencyUtils.getCurrencySymbol(base);
        String targetSymbol = CurrencyUtils.getCurrencySymbol(target);

        System.out.println("\n=================================================");
        System.out.println("              CONVERSION SUMMARY                 ");
        System.out.println("=================================================");
        System.out.printf("  Base Currency:    %-3s (%s)%n", base, baseName);
        System.out.printf("  Target Currency:  %-3s (%s)%n", target, targetName);
        System.out.printf("  Exchange Rate:    1 %s = %.4f %s%n", base, rate, target);
        System.out.println("  -----------------------------------------------");
        System.out.printf("  Original Amount:  %-30s%n", CurrencyUtils.formatAmount(originalAmount, base));
        System.out.printf("  Converted Amount: %-30s%n", CurrencyUtils.formatAmount(convertedAmount, target));
        System.out.printf("  Currency Symbol:  %s (%s -> %s)%n", targetSymbol, baseSymbol, targetSymbol);
        System.out.println("=================================================");
    }

    private void printWelcomeMessage() {
        System.out.println("=================================================");
        System.out.println("        REAL-TIME CURRENCY CONVERTER CLI        ");
        System.out.println("            (Internship Task 4 Project)          ");
        System.out.println("=================================================");
        System.out.println("Welcome! Convert amounts between various currencies");
        System.out.println("using live, real-time exchange rates.");
        System.out.println("=================================================\n");
    }

    private void printGoodbyeMessage() {
        System.out.println("\n=================================================");
        System.out.println("   Thank you for using Currency Converter! Bye!  ");
        System.out.println("=================================================");
    }

    private void printErrorMessage(String message) {
        System.out.println(">> ERROR: " + message);
    }

    /**
     * Main method to boot the application.
     */
    public static void main(String[] args) {
        CurrencyConverter app = new CurrencyConverter();
        app.start();
    }
}
