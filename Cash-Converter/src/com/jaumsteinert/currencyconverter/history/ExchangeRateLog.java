package com.jaumsteinert.currencyconverter.history;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateLog {
    private String baseCode;
    private String targetCode;
    private double amount;
    private double conversionResult;
    private LocalDateTime callTime;

    private static List<ExchangeRateLog> logs = new ArrayList<>();

    public ExchangeRateLog(String baseCode, String targetCode, double amount, double conversionResult, LocalDateTime callTime) {
        this.baseCode = baseCode;
        this.targetCode = targetCode;
        this.amount = amount;
        this.conversionResult = conversionResult;
        this.callTime = callTime;
    }

    @Override
    public String toString() {
        return "[" + baseCode + "] " + amount +
                " --> " +
                "[" + targetCode + "] " + String.format("%.2f", conversionResult)
                + " at " + callTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"));
    }

    public static void addLog(ExchangeRateLog log) {
        logs.add(log);
    }

    public static void printLogs() {
        if (logs.isEmpty()) {
            System.out.println("No logs.");
        } else {
            for (ExchangeRateLog log : logs) {
                System.out.println(log);
            }
        }
    }
}
