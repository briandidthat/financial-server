package com.toogroovy.priceserver.controller;

import com.toogroovy.priceserver.domain.Cryptocurrency;
import com.toogroovy.priceserver.domain.ApiResponse;
import com.toogroovy.priceserver.service.CryptoService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ApiResponse BTC_USD = new ApiResponse(Cryptocurrency.BTC, "40102.44", "coinbase");
    private final ApiResponse BNB_USD = new ApiResponse(Cryptocurrency.BNB, "389.22", "coinbase");
    private final ApiResponse ETH_USD = new ApiResponse(Cryptocurrency.ETH, "2900.24", "coinbase");

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

        when(service.getSpotPrice(Cryptocurrency.BTC)).thenReturn(BTC_USD);

        this.mockMvc.perform(get("/crypto/symbol").param("symbol",Cryptocurrency.BTC))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void getMultipleCryptoPrices() throws Exception {
        List<ApiResponse> responses = List.of(BTC_USD, BNB_USD, ETH_USD);
        List<String> symbols = List.of(Cryptocurrency.BTC, Cryptocurrency.BNB, Cryptocurrency.ETH);

        when(service.getSpotPrices(symbols)).thenReturn(responses);

        String inputJson = mapper.writeValueAsString(symbols);
        String outputJson = mapper.writeValueAsString(responses);

        this.mockMvc.perform(get("/crypto/symbols/multiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }
}