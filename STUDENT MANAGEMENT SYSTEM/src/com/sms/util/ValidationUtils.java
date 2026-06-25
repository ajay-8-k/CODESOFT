package com.sms.util;

import java.util.Scanner;

/**
 * Utility class containing methods for input validation and safe Scanner reading.
 */
public class ValidationUtils {

    /**
     * Validates that a name is not null and not empty (non-whitespace).
     */
    public static boolean validateName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    /**
     * Validates that a value is within the specified range (inclusive).
     */
    public static boolean validateRange(double val, double min, double max) {
        return val >= min && val <= max;
    }

    /**
     * Safely reads an integer from the scanner with prompt and validation.
     */
    public static int readInt(Scanner scanner, String prompt, String errorMsg, int minValue) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= minValue) {
                    return value;
                } else {
                    System.out.println("Error: Value must be at least " + minValue + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println(errorMsg);
            }
        }
    }

    /**
     * Safely reads a double within a range from the scanner.
     */
    public static double readDoubleInRange(Scanner scanner, String prompt, double min, double max, String errorMsg) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (validateRange(value, min, max)) {
                    return value;
                } else {
                    System.out.printf("Error: Value must be between %.2f and %.2f.\n", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.println(errorMsg);
            }
        }
    }

    /**
     * Safely reads a non-empty string.
     */
    public static String readNonEmptyString(Scanner scanner, String prompt, String errorMsg) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (validateName(input)) {
                return input;
            } else {
                System.out.println(errorMsg);
            }
        }
    }
}
