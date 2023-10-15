package com.briandidthat.priceserver.util;

import com.briandidthat.priceserver.domain.BatchRequest;
import com.briandidthat.priceserver.domain.Request;
import com.briandidthat.priceserver.domain.SpotPrice;
import com.briandidthat.priceserver.domain.Token;

import java.time.LocalDate;
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

    public static final Request HISTORICAL_ETH_REQUEST = new Request(ETH, START_DATE);
    public static final Request HISTORICAL_BTC_REQUEST = new Request(BTC, START_DATE);
    public static final Request HISTORICAL_BNB_REQUEST = new Request(BNB, START_DATE);

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

}
