package com.briandidthat.econserver.util;

import com.briandidthat.econserver.domain.AssetPrice;
import com.briandidthat.econserver.domain.BatchRequest;
import com.briandidthat.econserver.domain.BatchResponse;
import com.briandidthat.econserver.domain.Request;
import com.briandidthat.econserver.domain.coinbase.*;
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

    public static final SpotPriceResponse BTC_SPOT = new SpotPriceResponse(BTC, "40102.44", END_DATE);
    public static final SpotPriceResponse BNB_SPOT = new SpotPriceResponse(BNB, "389.22", END_DATE);
    public static final SpotPriceResponse ETH_SPOT = new SpotPriceResponse(ETH, "2900.00", END_DATE);
    public static final SpotPriceResponse HISTORICAL_BTC = new SpotPriceResponse(BTC, "41000.00", START_DATE);
    public static final SpotPriceResponse HISTORICAL_BNB = new SpotPriceResponse(BNB, "520.00", START_DATE);
    public static final SpotPriceResponse HISTORICAL_ETH = new SpotPriceResponse(ETH, "4000.00", START_DATE);

    public static final AssetPrice BTC_PRICE = new AssetPrice(BTC_SPOT.getBase(), BTC_SPOT.getAmount(), BTC_SPOT.getDate());
    public static final AssetPrice ETH_PRICE = new AssetPrice(ETH_SPOT.getBase(), ETH_SPOT.getAmount(), ETH_SPOT.getDate());
    public static final AssetPrice BNB_PRICE = new AssetPrice(BNB_SPOT.getBase(), BNB_SPOT.getAmount(), BNB_SPOT.getDate());
    public static final AssetPrice HISTORICAL_BTC_PRICE = new AssetPrice(HISTORICAL_BTC.getBase(), HISTORICAL_BTC.getAmount(), HISTORICAL_BTC.getDate());
    public static final AssetPrice HISTORICAL_ETH_PRICE = new AssetPrice(HISTORICAL_ETH.getBase(), HISTORICAL_ETH.getAmount(), HISTORICAL_ETH.getDate());
    public static final AssetPrice HISTORICAL_BNB_PRICE = new AssetPrice(HISTORICAL_BNB.getBase(), HISTORICAL_BNB.getAmount(), HISTORICAL_BNB.getDate());
    public static final BatchResponse BATCH_SPOT_RESPONSE = new BatchResponse(List.of(BTC_PRICE, ETH_PRICE, BNB_PRICE));
    public static final BatchResponse BATCH_HISTORICAL_SPOT_RESPONSE = new BatchResponse(List.of(HISTORICAL_BTC_PRICE, HISTORICAL_BNB_PRICE, HISTORICAL_ETH_PRICE));
    public static final Statistic ETH_STATISTICS = new Statistic("ETH", HISTORICAL_ETH_PRICE.getPrice(), ETH_PRICE.getPrice(), "-1100.00", "-27.50", START_DATE, END_DATE, "2 Years");
    public static final BatchRequest HISTORICAL_BATCH_REQUEST = new BatchRequest(List.of(new Request(BTC, START_DATE), new Request(BNB, START_DATE), new Request(ETH, START_DATE)));
    public static final List<String> TOKENS = List.of(TestingConstants.BTC, TestingConstants.BNB, TestingConstants.ETH);
    public static final Map<String, Boolean> AVAILABLE_TOKENS = Map.of("BTC", true, "BNB", true, "ETH", true);

    // FRED CONSTANTS
    public static final String AVERAGE_MORTGAGE_RATE = "MORTGAGE30US";
    public static final FredResponse MORTGAGE_RATE_RESPONSE = new FredResponse("07/12/2023", "08/12/2023", 2, List.of(new Observation("07/12/2023", "07/13/2023", "07/12/2023", "6.76"), new Observation("07/12/2023", "07/14/2023", "07/13/2023", "6.78")));
    public static final Observation CURRENT_MORTGAGE_RATE = new Observation("07/12/2023", "07/13/2023", "07/12/2023", "6.76");

    // TWELVE DATA CONSTANTS

    public static final Map<String, Boolean> AVAILABLE_STOCKS = Map.of("AAPL", true, "BRKB", true, "GOOG", true);
    public static final StockPriceResponse APPLE_PRICE_RESPONSE = new StockPriceResponse("108.50");
    public static final StockPriceResponse GOOGLE_PRICE_RESPONSE = new StockPriceResponse("128.50");

    public static final AssetPrice APPLE_PRICE = new AssetPrice("AAPL", "108.50", LocalDate.now());
    public static final AssetPrice GOOGLE_PRICE = new AssetPrice("GOOG", "128.50", LocalDate.now());
    public static final BatchResponse BATCH_STOCK_RESPONSE = new BatchResponse(List.of(APPLE_PRICE, GOOGLE_PRICE));

    public static final String TEST_API_KEY = "ABCDEFG";
}
