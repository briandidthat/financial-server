package com.toogroovy.notificationapi.controller;

import com.toogroovy.notificationapi.domain.Cryptocurrency;
import com.toogroovy.notificationapi.domain.rest.ApiResponse;
import com.toogroovy.notificationapi.domain.rest.DebankBalanceResponse;
import com.toogroovy.notificationapi.service.CryptoService;
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
    private final ApiResponse BTC_USD = new ApiResponse(Cryptocurrency.BTC, "40102.44", "coinbase");
    private final ApiResponse BNB_USD = new ApiResponse(Cryptocurrency.BNB, "389.22", "coinbase");
    private final ApiResponse ETH_USD = new ApiResponse(Cryptocurrency.ETH, "2900.24", "coinbase");
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
        String outputJson = mapper.writeValueAsString(BTC_USD);

        when(service.getTickerPrice(Cryptocurrency.BTC)).thenReturn(BTC_USD);

        this.mockMvc.perform(get("/crypto/tickers").param("ticker",Cryptocurrency.BTC))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void getMultipleCryptoPrices() throws Exception {
        List<ApiResponse> responses = List.of(BTC_USD, BNB_USD, ETH_USD);
        List<String> tickers = List.of(Cryptocurrency.BTC, Cryptocurrency.BNB, Cryptocurrency.ETH);

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

        this.mockMvc.perform(get("/crypto/accounts/total-balance").param("address", guppyAddress))
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
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }
}