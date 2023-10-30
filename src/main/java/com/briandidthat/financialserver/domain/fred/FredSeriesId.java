package com.briandidthat.financialserver.domain.fred;

import java.util.HashMap;

public final class FredSeriesId {
    public static final String CORE_CPI = "CPILFESL";
    public static final String STICKY_CPI = "CORESTICKM159SFRBATL";
    public static final String TOTAL_PUBLIC_DEBT = "GFDEBTN";
    public static final String AVERAGE_MORTGAGE_RATE = "MORTGAGE30US";
    public static final String DEBT_TO_GDP = "GFDEGDQ188S";
    public static final String ONE_YEAR_TREASURY_YIELD = "DGS1";
    public static final String FIVE_YEAR_TREASURY_YIELD = "DGS5";
    public static final String TEN_YEAR_TREASURY_YIELD = "DGS10";
    public static final String UNEMPLOYMENT_RATE = "U2RATE";
    public static final String FEDERAL_FUNDS_RATE = "FEDFUNDS";
    public static HashMap<String, String> operations;
}
