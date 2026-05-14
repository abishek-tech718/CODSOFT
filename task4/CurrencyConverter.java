import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CurrencyConverter {

    // Exchange rates relative to USD (base currency)
    private static final Map<String, Double> exchangeRates = new HashMap<>();

    static {
        exchangeRates.put("USD", 1.0);
        exchangeRates.put("EUR", 0.92);
        exchangeRates.put("GBP", 0.79);
        exchangeRates.put("INR", 83.50);
        exchangeRates.put("JPY", 149.50);
        exchangeRates.put("CAD", 1.36);
        exchangeRates.put("AUD", 1.53);
        exchangeRates.put("CHF", 0.90);
        exchangeRates.put("CNY", 7.24);
        exchangeRates.put("SGD", 1.34);
        exchangeRates.put("AED", 3.67);
        exchangeRates.put("BRL", 4.97);
    }

    // Currency symbols
    private static final Map<String, String> currencySymbols = new HashMap<>();

    static {
        currencySymbols.put("USD", "$");
        currencySymbols.put("EUR", "€");
        currencySymbols.put("GBP", "£");
        currencySymbols.put("INR", "₹");
        currencySymbols.put("JPY", "¥");
        currencySymbols.put("CAD", "CA$");
        currencySymbols.put("AUD", "A$");
        currencySymbols.put("CHF", "Fr");
        currencySymbols.put("CNY", "¥");
        currencySymbols.put("SGD", "S$");
        currencySymbols.put("AED", "AED");
        currencySymbols.put("BRL", "R$");
    }

    /**
     * Converts amount from one currency to another.
     *
     * @param amount       the amount to convert
     * @param baseCurrency the source currency code
     * @param targetCurrency the destination currency code
     * @return the converted amount
     */
    public static double convert(double amount, String baseCurrency, String targetCurrency) {
        if (!exchangeRates.containsKey(baseCurrency)) {
            throw new IllegalArgumentException("Unsupported base currency: " + baseCurrency);
        }
        if (!exchangeRates.containsKey(targetCurrency)) {
            throw new IllegalArgumentException("Unsupported target currency: " + targetCurrency);
        }

        // Convert base → USD → target
        double inUSD = amount / exchangeRates.get(baseCurrency);
        return inUSD * exchangeRates.get(targetCurrency);
    }

    /**
     * Returns the exchange rate between two currencies.
     */
    public static double getExchangeRate(String baseCurrency, String targetCurrency) {
        return exchangeRates.get(targetCurrency) / exchangeRates.get(baseCurrency);
    }

    /**
     * Displays all available currencies.
     */
    public static void displayAvailableCurrencies() {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║      Available Currencies        ║");
        System.out.println("╠══════════════════════════════════╣");
        for (Map.Entry<String, String> entry : currencySymbols.entrySet()) {
            System.out.printf("║  %-5s  %-5s                      ║%n",
                    entry.getKey(), entry.getValue());
        }
        System.out.println("╚══════════════════════════════════╝");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║       CURRENCY CONVERTER  v1.0       ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean continueConverting = true;

        while (continueConverting) {

            // Step 1: Show available currencies
            displayAvailableCurrencies();

            // Step 2: Select base currency
            System.out.print("\nEnter base currency (e.g. USD): ");
            String baseCurrency = scanner.next().toUpperCase().trim();

            if (!exchangeRates.containsKey(baseCurrency)) {
                System.out.println("❌ Invalid currency code. Please try again.");
                continue;
            }

            // Step 3: Select target currency
            System.out.print("Enter target currency (e.g. INR): ");
            String targetCurrency = scanner.next().toUpperCase().trim();

            if (!exchangeRates.containsKey(targetCurrency)) {
                System.out.println("❌ Invalid currency code. Please try again.");
                continue;
            }

            // Step 4: Enter amount
            System.out.print("Enter amount in " + baseCurrency + ": ");

            if (!scanner.hasNextDouble()) {
                System.out.println("❌ Invalid amount. Please enter a number.");
                scanner.next(); // clear invalid input
                continue;
            }

            double amount = scanner.nextDouble();

            if (amount < 0) {
                System.out.println("❌ Amount cannot be negative.");
                continue;
            }

            // Step 5: Convert and display result
            double convertedAmount = convert(amount, baseCurrency, targetCurrency);
            double exchangeRate    = getExchangeRate(baseCurrency, targetCurrency);

            String baseSymbol   = currencySymbols.get(baseCurrency);
            String targetSymbol = currencySymbols.get(targetCurrency);

            System.out.println("\n┌─────────────────────────────────────────┐");
            System.out.printf ("│  %s%-10.2f  →  %s%-10.2f         │%n",
                    baseSymbol, amount, targetSymbol, convertedAmount);
            System.out.printf ("│  Rate: 1 %s = %.6f %s           │%n",
                    baseCurrency, exchangeRate, targetCurrency);
            System.out.println("└─────────────────────────────────────────┘");

            // Step 6: Ask to continue
            System.out.print("\nConvert another? (yes/no): ");
            String answer = scanner.next().toLowerCase().trim();
            continueConverting = answer.equals("yes") || answer.equals("y");
        }

        System.out.println("\nThank you for using Currency Converter. Goodbye!");
        scanner.close();
    }
}