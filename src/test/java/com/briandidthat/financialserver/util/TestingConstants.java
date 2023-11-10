package com.briandidthat.financialserver.util;

import com.briandidthat.financialserver.domain.coinbase.BatchRequest;
import com.briandidthat.financialserver.domain.coinbase.Request;
import com.briandidthat.financialserver.domain.coinbase.SpotPrice;
import com.briandidthat.financialserver.domain.coinbase.Token;
import com.briandidthat.financialserver.domain.fred.FredResponse;
import com.briandidthat.financialserver.domain.fred.Observation;
import com.briandidthat.financialserver.domain.twelve.StockDetails;
import com.briandidthat.financialserver.domain.twelve.TwelveResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class TestingConstants {
    public static final String BTC = "BTC";
    public static final String BNB = "BNB";
    public static final String ETH = "ETH";

    public static final LocalDate START_DATE = LocalDate.of(2021, 8, 1);
    public static final LocalDate END_DATE = LocalDate.of(2023, 8, 1); // 2 yrs in between (24 months)

    public static final SpotPrice BTC_SPOT = new SpotPrice(BTC, "USD","40102.44", LocalDate.now());
    public static final SpotPrice BNB_SPOT = new SpotPrice(BNB, "USD", "389.22", LocalDate.now());
    public static final SpotPrice ETH_SPOT = new SpotPrice(ETH, "USD", "2900.00", LocalDate.now());

    public static final Request ETH_SPOT_REQUEST = new Request(ETH);
    public static final Request BTC_SPOT_REQUEST = new Request(BTC);
    public static final Request BNB_SPOT_REQUEST = new Request(BNB);

    public static final Request HISTORICAL_ETH_REQUEST = new Request(ETH, START_DATE, END_DATE);
    public static final Request HISTORICAL_BTC_REQUEST = new Request(BTC, START_DATE, END_DATE);
    public static final Request HISTORICAL_BNB_REQUEST = new Request(BNB, START_DATE, END_DATE);

    public static final SpotPrice HISTORICAL_BTC = new SpotPrice(BTC, "USD", "41000.00", START_DATE);
    public static final SpotPrice HISTORICAL_BNB = new SpotPrice(BNB, "USD", "520.00", START_DATE);
    public static final SpotPrice HISTORICAL_ETH = new SpotPrice(ETH, "USD", "4000.00", START_DATE);

    public static final BatchRequest SPOT_BATCH = new BatchRequest(List.of(new Request(BTC), new Request(BNB), new Request(ETH)));
    public static final BatchRequest HISTORICAL_BATCH = new BatchRequest(List.of(new Request(BTC, START_DATE), new Request(BNB, START_DATE), new Request(ETH, START_DATE)));

    public static final List<SpotPrice> SPOT_RESPONSES = List.of(BTC_SPOT, BNB_SPOT, ETH_SPOT);
    public static final List<SpotPrice> HISTORICAL_SPOT_RESPONSES = List.of(HISTORICAL_BTC, HISTORICAL_BNB, HISTORICAL_ETH);

    public static final List<Token> AVAILABLE_TOKENS = List.of(
            new Token("BTC", "Bitcoin", "blue", 1, 8, "crypto", "dsfas", "23df"),
            new Token("BNB", "Binance Coin", "gold", 3, 8, "crypto", "dsfas", "23df"),
            new Token("ETH", "Ethereum", "orange", 2, 8, "crypto", "dsfas", "23df"));



    // FRED CONSTANTS

    public static final FredResponse MORTGAGE_RATE_RESPONSE = new FredResponse("07/12/2023", "08/12/2023", 2, List.of(
            new Observation("07/12/2023", "07/13/2023", "07/12/2023", "6.76"),
            new Observation("07/12/2023", "07/14/2023", "07/13/2023", "6.78")));



    // TWELVE CONSTANTS

    public static final List<StockDetails> AVAILABLE_STOCKS = List.of(
            new StockDetails("AAPL", "apple", "USD", "NASDAQ", "IDK", "USA", "Common Stock"),
            new StockDetails("BRKB", "berkshire hathaway b", "USD", "NASDAQ", "IDK", "USA", "Common Stock"));

    public static final TwelveResponse APPLE_PRICE_RESPONSE = new TwelveResponse("AAPL", "108.50");
}
