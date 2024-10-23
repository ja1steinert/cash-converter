package com.jaumsteinert.currencyconverter;

import com.jaumsteinert.currencyconverter.service.ExchangeRateService;
import com.jaumsteinert.currencyconverter.ui.UI;

import java.util.Locale;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        Scanner scanner = new Scanner(System.in);
        String apiKey = "";
        ExchangeRateService service = null;

        boolean isValid = false;
        while (!isValid) {
            System.out.print("Please enter your API key: ");
            apiKey = scanner.nextLine();

            service = new ExchangeRateService(apiKey);
            isValid = service.isValidApiKey();

            if (!isValid) {
                System.out.println("Invalid API key. Please try again.");
            }
        }

        UI programUI = new UI(service);
        programUI.displayMainMenu();  // Starts the program.
    }
}
