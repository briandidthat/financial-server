package com.briandidthat.financialserver.domain.fred;

import com.briandidthat.financialserver.domain.exception.ResourceNotFoundException;

import java.util.HashMap;
import java.util.Map;

public final class FredSeriesId {
    public static final String SP_500 = "SP500";
    public static final String NASDAQ = "NASDAQ100";
    public static final String CORE_CPI = "CPILFESL";
    public static final String STICKY_CPI = "CORESTICKM159SFRBATL";
    public static final String TOTAL_PUBLIC_DEBT = "GFDEBTN";
    public static final String AVERAGE_MORTGAGE_RATE = "MORTGAGE30US";
    public static final String AVERAGE_HOME_SALE_PRICE = "ASPUS";
    public static final String DEBT_TO_GDP = "GFDEGDQ188S";
    public static final String ONE_YEAR_TREASURY_YIELD = "DGS1";
    public static final String FIVE_YEAR_TREASURY_YIELD = "DGS5";
    public static final String TEN_YEAR_TREASURY_YIELD = "DGS10";
    public static final String UNEMPLOYMENT_RATE = "U2RATE";
    public static final String FEDERAL_FUNDS_RATE = "FEDFUNDS";
    private static final Map<String, String> operations = new HashMap<>();

    static {
        operations.put("sp500", SP_500);
        operations.put("nasdaq", NASDAQ);
        operations.put("coreCpi", CORE_CPI);
        operations.put("stickyCpi", STICKY_CPI);
        operations.put("totalPublicDebt", TOTAL_PUBLIC_DEBT);
        operations.put("averageMortgageRate", AVERAGE_MORTGAGE_RATE);
        operations.put("averageHomeSalePrice", AVERAGE_HOME_SALE_PRICE);
        operations.put("debtToGdp", DEBT_TO_GDP);
        operations.put("oneYearTreasuryYield", ONE_YEAR_TREASURY_YIELD);
        operations.put("fiveYearTreasuryYield", FIVE_YEAR_TREASURY_YIELD);
        operations.put("tenYearTreasuryYield", TEN_YEAR_TREASURY_YIELD);
        operations.put("unemploymentRate", UNEMPLOYMENT_RATE);
        operations.put("federalFundsRate", FEDERAL_FUNDS_RATE);
    }

    public static String getSeriesId(String key) throws ResourceNotFoundException {
        final String seriesId = operations.getOrDefault(key, null);
        if (seriesId == null)
            throw new ResourceNotFoundException("Invalid series id. Available operations: " + operations.keySet());
        return seriesId;
    }
}
