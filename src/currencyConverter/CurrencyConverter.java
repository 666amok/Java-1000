package currencyConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CurrencyConverter {

    private static final String ECB_RATES_URL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    private static final Map<String, Double> exchangeRates = new HashMap<>();

    public static void main(String[] args) {
        loadExchangeRates();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Available currencies: " + exchangeRates.keySet());

        while (true) {
            try {
                System.out.print("\nEnter amount to convert (or 0 to exit): ");
                double amount = scanner.nextDouble();
                if (amount == 0) break;

                System.out.print("Enter source currency (e.g. USD, RUB): ");
                String fromCurrency = scanner.next().toUpperCase();

                System.out.print("Enter target currency (e.g. EUR, USD): ");
                String toCurrency = scanner.next().toUpperCase();

                double convertedAmount = convertCurrency(amount, fromCurrency, toCurrency);
                DecimalFormat df = new DecimalFormat("#,##0.00");
                System.out.println("\n" + df.format(amount) + " " + fromCurrency + " = " +
                        df.format(convertedAmount) + " " + toCurrency);

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                scanner.nextLine(); // Clear buffer
            }
        }
        System.out.println("Currency converter exited.");
    }

    public static double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        if (!exchangeRates.containsKey(fromCurrency) && !fromCurrency.equals("EUR")) {
            throw new IllegalArgumentException("Unsupported source currency: " + fromCurrency +
                    "\nSupported currencies: " + exchangeRates.keySet());
        }

        if (!exchangeRates.containsKey(toCurrency) && !toCurrency.equals("EUR")) {
            throw new IllegalArgumentException("Unsupported target currency: " + toCurrency +
                    "\nSupported currencies: " + exchangeRates.keySet());
        }

        double fromRate = fromCurrency.equals("EUR") ? 1.0 : exchangeRates.get(fromCurrency);
        double toRate = toCurrency.equals("EUR") ? 1.0 : exchangeRates.get(toCurrency);

        return (amount / fromRate) * toRate;
    }

    private static void loadExchangeRates() {
        // First set all default rates
        setDefaultRates();

        // Then try to fetch live rates
        try {
            URL url = new URL(ECB_RATES_URL);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("<Cube currency=")) {
                    String[] parts = line.split("\"");
                    if (parts.length >= 4) {
                        String currency = parts[1];
                        try {
                            double rate = Double.parseDouble(parts[3]);
                            exchangeRates.put(currency, rate); // Overwrite default with live rate
                        } catch (NumberFormatException e) {
                            System.err.println("Skipping invalid rate for currency: " + currency);
                        }
                    }
                }
            }
            reader.close();
            System.out.println("Successfully loaded live exchange rates");

        } catch (IOException e) {
            System.err.println("Warning: Couldn't fetch live rates. Using default rates.");
        }
    }

    private static void setDefaultRates() {
        // Major world currencies
        exchangeRates.put("USD", 1.08);    // US Dollar
        exchangeRates.put("EUR", 1.0);     // Euro (base)
        exchangeRates.put("GBP", 0.85);    // British Pound
        exchangeRates.put("JPY", 141.50);  // Japanese Yen
        exchangeRates.put("CHF", 0.96);    // Swiss Franc
        exchangeRates.put("CAD", 1.35);    // Canadian Dollar
        exchangeRates.put("AUD", 1.50);    // Australian Dollar

        // BRICS currencies
        exchangeRates.put("RUB", 75.50);   // Russian Ruble
        exchangeRates.put("CNY", 7.10);     // Chinese Yuan
        exchangeRates.put("INR", 82.90);    // Indian Rupee
        exchangeRates.put("BRL", 5.20);     // Brazilian Real
        exchangeRates.put("ZAR", 18.75);    // South African Rand

        // Other important currencies
        exchangeRates.put("MXN", 20.15);    // Mexican Peso
        exchangeRates.put("KRW", 1300.0);   // South Korean Won
        exchangeRates.put("TRY", 15.80);    // Turkish Lira
    }
}