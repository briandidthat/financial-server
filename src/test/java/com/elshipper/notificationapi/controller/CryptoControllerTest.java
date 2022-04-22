package com.elshipper.notificationapi.controller;

import com.elshipper.notificationapi.domain.Cryptocurrency;
import com.elshipper.notificationapi.domain.rest.BinanceTickerResponse;
import com.elshipper.notificationapi.domain.rest.DebankBalanceResponse;
import com.elshipper.notificationapi.service.CryptoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CryptoController.class)
class CryptoControllerTest {
    private final BinanceTickerResponse BTC_USDT = new BinanceTickerResponse(Cryptocurrency.BTC.getPair(), "40102.44");
    private final BinanceTickerResponse BNB_USDT = new BinanceTickerResponse(Cryptocurrency.BNB.getPair(), "389.22");
    private final BinanceTickerResponse ETH_USDT = new BinanceTickerResponse(Cryptocurrency.ETH.getPair(), "2900.24");
    private final DebankBalanceResponse GUPPY = new DebankBalanceResponse("21.234", new ArrayList<>());
    private final DebankBalanceResponse WHALE = new DebankBalanceResponse("3000000.23", new ArrayList<>());
    private final String guppyAddress = "0x344532524";
    private final String whaleAddress = "0x344532512";

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CryptoService service;

    @BeforeEach
    void setUp() {
    }


    @Test
    void getCryptoPrice() throws Exception {
        String outputJson = mapper.writeValueAsString(BTC_USDT);

        when(service.getTickerPrice(Cryptocurrency.BTC.getPair())).thenReturn(BTC_USDT);

        this.mockMvc.perform(get("/crypto/tickers?ticker=" + Cryptocurrency.BTC.getPair()))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void getMultipleCryptoPrices() throws Exception {
        List<BinanceTickerResponse> responses = List.of(BTC_USDT, BNB_USDT, ETH_USDT);
        List<String> tickers = List.of(Cryptocurrency.BTC.getPair(), Cryptocurrency.BNB.getPair(), Cryptocurrency.ETH.getPair());

        when(service.getTickerPricesAsync(tickers)).thenReturn(responses);

        String inputJson = mapper.writeValueAsString(tickers);
        String outputJson = mapper.writeValueAsString(responses);

        this.mockMvc.perform(get("/crypto/tickers/multiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void getAccountBalance() throws Exception {
        String outputJson = mapper.writeValueAsString(GUPPY);

        when(service.getAccountBalance(guppyAddress)).thenReturn(GUPPY);

        this.mockMvc.perform(get("/crypto/accounts/total-balance?address=" + guppyAddress))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void getMultipleAccountBalances() throws Exception {
        List<DebankBalanceResponse> responses = List.of(GUPPY, WHALE);
        List<String> addresses = List.of(guppyAddress, whaleAddress);

        when(service.getAccountBalances(addresses)).thenReturn(responses);

        String inputJson = mapper.writeValueAsString(addresses);
        String outputJson = mapper.writeValueAsString(responses);

        this.mockMvc.perform(get("/crypto/accounts/total-balance/multiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }
}