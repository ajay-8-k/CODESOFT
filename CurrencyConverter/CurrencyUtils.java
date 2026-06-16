

package CurrencyConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * CurrencyUtils provides utilities for managing and validating supported currencies,
 * retrieving currency symbols, and displaying currency information.
 */
public class CurrencyUtils {

    // Map to store supported currency codes and their corresponding symbols
    private static final Map<String, String> currencySymbols = new HashMap<>();
    
    // Map to store supported currency codes and their full names
    private static final Map<String, String> currencyNames = new HashMap<>();

    static {
        // Initialize the supported currencies, their symbols, and names
        currencySymbols.put("USD", "$");
        currencyNames.put("USD", "United States Dollar");

        currencySymbols.put("INR", "₹");
        currencyNames.put("INR", "Indian Rupee");

        currencySymbols.put("EUR", "€");
        currencyNames.put("EUR", "Euro");

        currencySymbols.put("GBP", "£");
        currencyNames.put("GBP", "British Pound");

        currencySymbols.put("JPY", "¥");
        currencyNames.put("JPY", "Japanese Yen");

        currencySymbols.put("AUD", "A$");
        currencyNames.put("AUD", "Australian Dollar");

        currencySymbols.put("CAD", "C$");
        currencyNames.put("CAD", "Canadian Dollar");
    }

    /**
     * Checks if the given currency code is supported by the application.
     *
     * @param code The 3-letter currency code (e.g., USD, INR).
     * @return true if supported, false otherwise.
     */
    public static boolean isSupportedCurrency(String code) {
        if (code == null) {
            return false;
        }
        return currencySymbols.containsKey(code.toUpperCase().trim());
    }

    /**
     * Retrieves the symbol for a given currency code.
     *
     * @param code The currency code.
     * @return The currency symbol (e.g., $, ₹), or the code itself if not found.
     */
    public static String getCurrencySymbol(String code) {
        if (code == null) {
            return "";
        }
        String normalizedCode = code.toUpperCase().trim();
        return currencySymbols.getOrDefault(normalizedCode, normalizedCode);
    }

    /**
     * Retrieves the full name for a given currency code.
     *
     * @param code The currency code.
     * @return The full name of the currency.
     */
    public static String getCurrencyName(String code) {
        if (code == null) {
            return "Unknown Currency";
        }
        String normalizedCode = code.toUpperCase().trim();
        return currencyNames.getOrDefault(normalizedCode, "Unknown Currency");
    }

    /**
     * Displays a beautifully formatted list of all supported currencies.
     */
    public static void printSupportedCurrencies() {
        System.out.println("=================================================");
        System.out.println("          SUPPORTED CURRENCIES LIST              ");
        System.out.println("=================================================");
        System.out.printf("%-6s | %-25s | %-6s%n", "Code", "Currency Name", "Symbol");
        System.out.println("-------------------------------------------------");
        for (String code : currencySymbols.keySet()) {
            System.out.printf("%-6s | %-25s | %-6s%n", 
                    code, currencyNames.get(code), currencySymbols.get(code));
        }
        System.out.println("=================================================");
    }

    /**
     * Formats the amount to two decimal places for standard display.
     *
     * @param amount The numerical amount to format.
     * @param code The currency code to prepend with its symbol.
     * @return A formatted string (e.g., $1,250.50).
     */
    public static String formatAmount(double amount, String code) {
        String symbol = getCurrencySymbol(code);
        return String.format("%s%,.2f", symbol, amount);
    }
}
