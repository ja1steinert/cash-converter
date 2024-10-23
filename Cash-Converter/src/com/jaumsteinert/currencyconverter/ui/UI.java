package com.jaumsteinert.currencyconverter.ui;

import com.jaumsteinert.currencyconverter.exception.ExchangeRateException;
import com.jaumsteinert.currencyconverter.history.ExchangeRateLog;
import com.jaumsteinert.currencyconverter.model.ExchangeRate;
import com.jaumsteinert.currencyconverter.service.ExchangeRateService;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UI {
    private final Scanner scanner;
    private final ExchangeRateService service;

    public UI(ExchangeRateService service) {
        this.scanner = new Scanner(System.in);
        this.service = service;
    }

    public void displayMainMenu() {
        char option = '0';
        while (option != 5) {
            printMenu();
            String input = scanner.nextLine().trim().toLowerCase();
            if (!input.isEmpty()) {
                option = input.charAt(0);
            }

            switch (option) {
                case '1' -> displayQuickMenu();
                case '2' -> convertCurrency();
                case '3' -> {
                    ExchangeRateLog.printLogs();
                    returnToMenu();
                }
                case '4' -> {
                    displayAllSupportedCurrencies();
                    returnToMenu();
                }
                case '5' -> {
                    System.out.println("""
                ╔══════════════════════════════════════════════╗
                ║              Project made for                ║
                ║            Oracle Next Education             ║
                ╚══════════════════════════════════════════════╝
                """);
                    System.exit(0);
                }
                default -> System.out.println("Please choose a valid option.");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║            CURRENCY CONVERTER            ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  1. Quick Menu                           ║");
        System.out.println("║  2. Convert Currency                     ║");
        System.out.println("║  3. View Conversion History              ║");
        System.out.println("║  4. Show All Supported Currencies        ║");
        System.out.println("║  5. Exit                                 ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.print  ("              Select an option: ");

    }

    private void displayQuickMenu() {
        char quickOption = '0';

        while (true) {
            System.out.println("""
        ╔══════════════════════════════════════════╗
        ║               QUICK MENU                 ║
        ╠══════════════════════════════════════════╣
        ║  1. ARS to USD                           ║
        ║  2. USD to ARS                           ║
        ║  3. BRL to USD                           ║
        ║  4. USD to BRL                           ║
        ║  5. COP to USD                           ║
        ║  6. USD to COP                           ║
        ╚══════════════════════════════════════════╝
        """);

            System.out.print("Select an option: ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (!input.isEmpty()) {
                quickOption = input.charAt(0);
            }

            try {
                System.out.print("Amount: ");
                double amount = scanner.nextDouble();
                scanner.nextLine(); // Consumes NewLine

                switch (quickOption) {
                    case '1' -> executeConversion("ARS", "USD", amount);
                    case '2' -> executeConversion("USD", "ARS", amount);
                    case '3' -> executeConversion("BRL", "USD", amount);
                    case '4' -> executeConversion("USD", "BRL", amount);
                    case '5' -> executeConversion("COP", "USD", amount);
                    case '6' -> executeConversion("USD", "COP", amount);
                    default -> System.out.println("Invalid option.");
                }

                break;

            } catch (InputMismatchException e) {
                System.err.println("Invalid input. Please enter a valid number for the amount.");
                scanner.nextLine(); // Consumes NewLine
            }
        }

        returnToMenu();
    }

    private void executeConversion(String baseCode, String targetCode, double amount) {
        try {
            ExchangeRate exchangeRate = service.getExchangeRate(baseCode, targetCode, amount);
            System.out.println("[" + baseCode + "] " + amount + " --> " +"[" + targetCode + "] "
                                + String.format("%.2f", exchangeRate.getConversionResult()));
        } catch (IOException | InterruptedException e) {
            System.err.println("Error occurred: " + e.getMessage());
        } catch (ExchangeRateException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void returnToMenu() {
        char answer = '0';

        while (answer != 'y') {
            System.out.print("Return to menu? [y/n] ");

            String input = scanner.nextLine().trim().toLowerCase();
            if (!input.isEmpty()) {
                answer = input.charAt(0);
            }

            switch (answer) {
                case 'y' -> {
                    return;
                }
                case 'n'-> {
                    System.out.println("""
                ╔══════════════════════════════════════════════╗
                ║              Project made for                ║
                ║            Oracle Next Education             ║
                ╚══════════════════════════════════════════════╝
                """);
                    System.exit(0);
                }
                default -> System.out.println("Invalid option. Please enter 'y' or 'n'.");
            }
        }
    }

    private void convertCurrency() {
        System.out.print("Base code: ");
        String baseCode = scanner.nextLine();
        System.out.print("Target code: ");
        String targetCode = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();

        executeConversion(baseCode, targetCode, amount);
        returnToMenu();
    }

    private void displayAllSupportedCurrencies() {
        try {
            ExchangeRate supportedCodes = service.getAllSupportedCodes();
            List<List<String>> codes = supportedCodes.getSupportedCodes();

            StringBuilder row = new StringBuilder();
            int count = 0;
            int codesPerRow = 4;

            for (List<String> codePair : codes) {
                row.append("[").append(codePair.get(0)).append("] ").append(codePair.get(1)).append("  ");
                count++;

                // Print 4 codes per line
                if (count % codesPerRow == 0) {
                    System.out.println(row.toString());
                    row.setLength(0); // Clear the row
                }
            }

            // Print remaining codes if any
            if (row.length() > 0) {
                System.out.println(row.toString());
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Error occurred: " + e.getMessage());
        } catch (ExchangeRateException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

}
