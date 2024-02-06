package com.briandidthat.econserver.util;

import com.briandidthat.econserver.domain.coinbase.BatchRequest;
import com.briandidthat.econserver.domain.coinbase.Request;
import com.briandidthat.econserver.domain.coinbase.SpotPrice;
import com.briandidthat.econserver.domain.coinbase.Statistic;
import com.briandidthat.econserver.domain.fred.FredResponse;
import com.briandidthat.econserver.domain.fred.Observation;
import com.briandidthat.econserver.domain.twelve.StockPriceResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public final class TestingConstants {
    public static final String BTC = "BTC";
    public static final String BNB = "BNB";
    public static final String ETH = "ETH";

    public static final LocalDate START_DATE = LocalDate.of(2021, 8, 1);
    public static final LocalDate END_DATE = LocalDate.of(2023, 8, 1); // 2 yrs in between (24 months)

    public static final SpotPrice BTC_SPOT = new SpotPrice(BTC, "USD", "40102.44", LocalDate.now());
    public static final SpotPrice BNB_SPOT = new SpotPrice(BNB, "USD", "389.22", LocalDate.now());
    public static final SpotPrice ETH_SPOT = new SpotPrice(ETH, "USD", "2900.00", LocalDate.now());
    public static final List<SpotPrice> SPOT_RESPONSES = List.of(BTC_SPOT, BNB_SPOT, ETH_SPOT);
    public static final SpotPrice HISTORICAL_BTC = new SpotPrice(BTC, "USD", "41000.00", START_DATE);
    public static final SpotPrice HISTORICAL_BNB = new SpotPrice(BNB, "USD", "520.00", START_DATE);
    public static final SpotPrice HISTORICAL_ETH = new SpotPrice(ETH, "USD", "4000.00", START_DATE);
    public static final List<SpotPrice> HISTORICAL_SPOT_RESPONSES = List.of(HISTORICAL_BTC, HISTORICAL_BNB, HISTORICAL_ETH);
    public static final Statistic ETH_STATISTICS = new Statistic("ETH", HISTORICAL_ETH.getAmount(), ETH_SPOT.getAmount(),
            "-1100.00", "-27.50", START_DATE, END_DATE, "730");
    public static final BatchRequest HISTORICAL_BATCH = new BatchRequest(List.of(new Request(BTC, START_DATE),
            new Request(BNB, START_DATE), new Request(ETH, START_DATE)));
    public static final List<String> TOKENS = List.of(TestingConstants.BTC, TestingConstants.BNB, TestingConstants.ETH);
    public static final Map<String, Boolean> AVAILABLE_TOKENS = Map.of("BTC", true, "BNB", true, "ETH", true);

    // FRED CONSTANTS
    public static final String AVERAGE_MORTGAGE_RATE = "MORTGAGE30US";
    public static final String SP_500 = "SP500";

    public static final FredResponse MORTGAGE_RATE_RESPONSE = new FredResponse("07/12/2023", "08/12/2023", 2,
            List.of(new Observation("07/12/2023", "07/13/2023", "07/12/2023", "6.76"),
                    new Observation("07/12/2023", "07/14/2023", "07/13/2023", "6.78")));

    public static final Observation CURRENT_MORTGAGE_RATE = new Observation("07/12/2023", "07/13/2023", "07/12/2023", "6.76");

    // TWELVE DATA CONSTANTS

    public static final Map<String, Boolean> AVAILABLE_STOCKS = Map.of("AAPL", true, "BRKB", true, "GOOG", true);

    public static final StockPriceResponse APPLE_PRICE_RESPONSE = new StockPriceResponse("AAPL", "108.50");
    public static final StockPriceResponse GOOGLE_PRICE_RESPONSE = new StockPriceResponse("GOOG", "120.50");

    public static final List<StockPriceResponse> BATCH_STOCK_RESPONSE = List.of(APPLE_PRICE_RESPONSE, GOOGLE_PRICE_RESPONSE);

    public static final String TEST_API_KEY = "ABCDEFG";
}
