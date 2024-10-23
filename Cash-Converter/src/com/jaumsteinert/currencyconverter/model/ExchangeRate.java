package com.jaumsteinert.currencyconverter.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExchangeRate {

    @SerializedName("result")
    private String result;

    @SerializedName("error-type")
    private String errorType;

    @SerializedName("base_code")
    private String baseCode;

    @SerializedName("target_code")
    private String targetCode;

    @SerializedName("conversion_rate")
    private double conversionRate;

    @SerializedName("conversion_result")
    private double conversionResult;

    @SerializedName("supported_codes")
    private List<List<String>> supportedCodes;

    // Getters
    public String getResult() {
        return result;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getBaseCode() {
        return baseCode;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public double getConversionRate() {
        return conversionRate;
    }

    public double getConversionResult() {
        return conversionResult;
    }

    public List<List<String>> getSupportedCodes() {
        return supportedCodes;
    }
}