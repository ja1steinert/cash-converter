package com.jaumsteinert.currencyconverter.service;

import com.google.gson.Gson;
import com.jaumsteinert.currencyconverter.exception.ExchangeRateException;
import com.jaumsteinert.currencyconverter.history.ExchangeRateLog;
import com.jaumsteinert.currencyconverter.model.ExchangeRate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

public class ExchangeRateService {

    private final String apiKey;
    HttpClient client = HttpClient.newHttpClient();
    Gson gson = new Gson();

    public ExchangeRateService(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean isValidApiKey() {
        if (this.apiKey.contains(" ") || !this.apiKey.matches("^[a-zA-Z0-9-_]+$")) {
            System.err.println("API key should not contain spaces nor symbols.");
            return false;
        }

        try {
            String json = getJson("https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/USD");

            ExchangeRate exchangeRate = gson.fromJson(json, ExchangeRate.class);

            // Inverts the result so "true" means "no errors"
            return !exchangeRate.getResult().equals("error");

        } catch (IOException | InterruptedException e) {
            System.err.println("Error validating API key: " + e.getMessage());
            return false;
        }
    }

    public ExchangeRate getExchangeRate(String baseCode, String targetCode, double amount) throws IOException, InterruptedException, ExchangeRateException {
        String json = getJson("https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/" +
                baseCode + "/" + targetCode + "/" + amount);

        ExchangeRate exchangeRate = gson.fromJson(json, ExchangeRate.class);

        checkForErrors(exchangeRate);

        // Log the service call with time using ServiceCallLog
        ExchangeRateLog.addLog(new ExchangeRateLog(baseCode, targetCode, amount, exchangeRate.getConversionResult(), LocalDateTime.now()));

        return exchangeRate;
    }

    public ExchangeRate getAllSupportedCodes() throws IOException, InterruptedException, ExchangeRateException {
        String json = getJson("https://v6.exchangerate-api.com/v6/" + apiKey + "/codes");

        ExchangeRate allSupportedCodes = gson.fromJson(json, ExchangeRate.class);

        checkForErrors(allSupportedCodes);

        return allSupportedCodes;
    }

    private String getJson(String APIurl) throws IOException, InterruptedException {
        URI url = URI.create(APIurl);
        HttpRequest request = HttpRequest.newBuilder().uri(url).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    };

    private void checkForErrors(ExchangeRate e) throws ExchangeRateException {
        if ("error".equals(e.getResult())) {
            String errorType = e.getErrorType();

            if (errorType == null) {
                throw new ExchangeRateException("An unknown error occurred. Error type is missing.");
            }

            throw new ExchangeRateException(getErrorMessage(errorType));
        }
    }


    private String getErrorMessage(String errorType) {
        return switch (errorType) {
            case "unsupported-code" ->
                    "One of the currency codes entered is not supported. Please check the codes and try again.";
            case "malformed-request" -> "The request was not formed correctly. Please try again.";
            case "invalid-key" -> "The API key provided is not valid! Please, make sure you typed it correctly.";
            case "inactive-account" -> "Your account is inactive. Please, make sure you verified your email address.";
            case "quota-reached" ->
                    "You have reached your API usage quota. Please try again later or upgrade your plan.";
            default -> "An unknown error occurred. Please, try again later.";
        };
    }
}
