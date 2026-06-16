

package CurrencyConverter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ExchangeRateService handles fetching real-time exchange rates from a public API
 * and extracting the specific rate for the target currency.
 */
public class ExchangeRateService {

    // Free public API endpoint (requires no API key)
    private static final String API_URL_TEMPLATE = "https://open.er-api.com/v6/latest/";

    /**
     * Fetches the real-time exchange rate between a base currency and a target currency.
     *
     * @param baseCurrency   The source currency code (e.g., USD).
     * @param targetCurrency The destination currency code (e.g., EUR).
     * @return The exchange rate as a double.
     * @throws Exception If network errors, HTTP failure, or parsing issues occur.
     */
    public double fetchExchangeRate(String baseCurrency, String targetCurrency) throws Exception {
        // Normalize currency codes to uppercase
        String base = baseCurrency.toUpperCase().trim();
        String target = targetCurrency.toUpperCase().trim();

        // If the base and target are the same, the rate is always 1.0
        if (base.equals(target)) {
            return 1.0;
        }

        String urlString = API_URL_TEMPLATE + base;
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = java.net.URI.create(urlString).toURL();
            connection = (HttpURLConnection) url.openConnection();

            // Set appropriate request parameters
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // 5 seconds connection timeout
            connection.setReadTimeout(5000);    // 5 seconds read timeout
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Java CurrencyConverter/1.0");

            int responseCode = connection.getResponseCode();
            
            // Check if the request was successful (HTTP 200 OK)
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new Exception("HTTP request failed with status code: " + responseCode + 
                        ". API might be temporarily unavailable or currency code is invalid.");
            }

            // Read the JSON response from the API
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Parse the exchange rate from the JSON payload
            return parseExchangeRate(response.toString(), target);

        } catch (java.net.UnknownHostException e) {
            throw new Exception("No internet connection or API server is unreachable. Please check your network connection.");
        } catch (java.net.SocketTimeoutException e) {
            throw new Exception("The connection to the exchange rate server timed out. Please try again later.");
        } catch (Exception e) {
            throw new Exception("Error fetching exchange rate: " + e.getMessage(), e);
        } finally {
            // Clean up resources
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    // Ignore exception on close
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Parses the exchange rate value of the target currency from the JSON response.
     * Uses Regex to prevent the need for third-party JSON parsing libraries.
     *
     * Example JSON segment: "... "rates":{"USD":1,"INR":83.45,"EUR":0.92 ... }
     *
     * @param jsonResponse   The raw JSON string returned by the API.
     * @param targetCurrency The target currency code to search for.
     * @return The extracted exchange rate.
     * @throws Exception If the target currency rate is not found in the JSON.
     */
    private double parseExchangeRate(String jsonResponse, String targetCurrency) throws Exception {
        // Regular expression to look for "targetCurrency": <numeric_value>
        // Examples: "EUR":0.9213 or "EUR": 0.92
        String regex = "\"" + targetCurrency + "\"\\s*:\\s*([0-9.]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(jsonResponse);

        if (matcher.find()) {
            try {
                return Double.parseDouble(matcher.group(1));
            } catch (NumberFormatException e) {
                throw new Exception("Failed to convert exchange rate for " + targetCurrency + " to a valid number.");
            }
        } else {
            throw new Exception("Exchange rate for target currency '" + targetCurrency + "' was not found in the API response.");
        }
    }
}
