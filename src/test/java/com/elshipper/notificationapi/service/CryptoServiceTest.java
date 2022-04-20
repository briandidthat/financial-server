package com.elshipper.notificationapi.service;

import com.elshipper.notificationapi.domain.Cryptocurrency;
import com.elshipper.notificationapi.domain.rest.BinanceTickerResponse;
import com.elshipper.notificationapi.domain.rest.DebankBalanceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
class CryptoServiceTest {
    private final BinanceTickerResponse BTC_USDT = new BinanceTickerResponse(Cryptocurrency.BTC.getPair(), "40102.44");
    private final BinanceTickerResponse BNB_USDT = new BinanceTickerResponse(Cryptocurrency.BNB.getPair(), "389.22");
    private final BinanceTickerResponse ETH_USDT = new BinanceTickerResponse(Cryptocurrency.ETH.getPair(), "2900.24");
    private final DebankBalanceResponse GUPPY = new DebankBalanceResponse("21.234", new ArrayList<>());
    private final DebankBalanceResponse WHALE = new DebankBalanceResponse("3000000.23", new ArrayList<>());

    private final List<BinanceTickerResponse> PRICES = List.of(BTC_USDT, BNB_USDT, ETH_USDT);
    private final List<DebankBalanceResponse> BALANCES = List.of(GUPPY, WHALE);
    private final String binanceEndpoint = "https://api1.binance.com/api/v3/ticker/price?symbol=";
    private final String debankEndpoint =  "https://openapi.debank.com/v1/user/total_balance?id=";

    private final String guppyAddress = "0x344532524";
    private final String whaleAddress = "0x344532512";
    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private CryptoService cryptoService;


    @BeforeEach
    void setUp() {
        when(restTemplate.getForEntity(binanceEndpoint + Cryptocurrency.BTC.getPair(), BinanceTickerResponse.class)).thenReturn(ResponseEntity.ok(BTC_USDT));
        when(restTemplate.getForEntity(binanceEndpoint + Cryptocurrency.BNB.getPair(), BinanceTickerResponse.class)).thenReturn(ResponseEntity.ok(BNB_USDT));
        when(restTemplate.getForEntity(binanceEndpoint + Cryptocurrency.ETH.getPair(), BinanceTickerResponse.class)).thenReturn(ResponseEntity.ok(ETH_USDT));
        when(restTemplate.getForEntity(debankEndpoint + guppyAddress, DebankBalanceResponse.class)).thenReturn(ResponseEntity.ok(GUPPY));
        when(restTemplate.getForEntity(debankEndpoint + whaleAddress, DebankBalanceResponse.class)).thenReturn(ResponseEntity.ok(WHALE));
    }

    @Test
    void getTickerPrice() {
        BinanceTickerResponse tickerResponse = cryptoService.getTickerPrice(Cryptocurrency.BTC.getPair());

        assertEquals(BTC_USDT, tickerResponse);
    }

    @Test
    void getTickerPricesSync() {
        List<BinanceTickerResponse> responses = cryptoService.getTickerPricesSync(List.of(Cryptocurrency.BTC.getPair(),
                Cryptocurrency.BNB.getPair(), Cryptocurrency.ETH.getPair()));

        assertIterableEquals(PRICES, responses);
    }

    @Test
    void getTickerPricesAsync() {
        List<BinanceTickerResponse> responses = cryptoService.getTickerPricesAsync(List.of(Cryptocurrency.BTC.getPair(),
                Cryptocurrency.BNB.getPair(), Cryptocurrency.ETH.getPair()));

        assertIterableEquals(PRICES, responses);
    }

    @Test
    void getAccountBalance() {
        DebankBalanceResponse response = cryptoService.getAccountBalance(guppyAddress);

        assertEquals(GUPPY, response);
    }

    @Test
    void getAccountBalances() {
        List<DebankBalanceResponse> responses = cryptoService.getAccountBalances(List.of(guppyAddress, whaleAddress));

        assertIterableEquals(BALANCES, responses);
    }
}