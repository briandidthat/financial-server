package com.toogroovy.priceserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toogroovy.priceserver.domain.Cryptocurrency;
import com.toogroovy.priceserver.domain.SpotPrice;
import com.toogroovy.priceserver.service.CryptoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(Controller.class)
class ControllerTest {
    private final SpotPrice BTC_USD = new SpotPrice(Cryptocurrency.BTC, "40102.44", "coinbase");
    private final SpotPrice BNB_USD = new SpotPrice(Cryptocurrency.BNB, "389.22", "coinbase");
    private final SpotPrice ETH_USD = new SpotPrice(Cryptocurrency.ETH, "2900.24", "coinbase");

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
    void getSpotPrice() throws Exception {
        String outputJson = mapper.writeValueAsString(BTC_USD);

        when(service.getSpotPrice(Cryptocurrency.BTC)).thenReturn(BTC_USD);

        this.mockMvc.perform(get("/spot").param("symbol", Cryptocurrency.BTC))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void getHistoricalSpotPrice() throws Exception {
        String outputJson = mapper.writeValueAsString(BTC_USD);

        when(service.getSpotPrice(Cryptocurrency.BTC)).thenReturn(BTC_USD);

        this.mockMvc.perform(get("/spot").param("symbol", Cryptocurrency.BTC))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void getMultipleSpotPrices() throws Exception {
        List<SpotPrice> responses = List.of(BTC_USD, BNB_USD, ETH_USD);
        List<String> symbols = List.of(Cryptocurrency.BTC, Cryptocurrency.BNB, Cryptocurrency.ETH);

        when(service.getSpotPrices(symbols)).thenReturn(responses);

        String inputJson = mapper.writeValueAsString(symbols);
        String outputJson = mapper.writeValueAsString(responses);

        this.mockMvc.perform(get("/spot/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }
}