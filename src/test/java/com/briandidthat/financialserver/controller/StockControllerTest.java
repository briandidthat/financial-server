package com.briandidthat.financialserver.controller;

import com.briandidthat.financialserver.domain.exception.ResourceNotFoundException;
import com.briandidthat.financialserver.service.TwelveService;
import com.briandidthat.financialserver.util.TestingConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockController.class)
class StockControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private TwelveService service;

    @Test
    void getStockPrice() throws Exception {
        final String outputJson = mapper.writeValueAsString(TestingConstants.APPLE_PRICE_RESPONSE);

        when(service.getStockPrice("AAPL")).thenReturn(TestingConstants.APPLE_PRICE_RESPONSE);

        this.mockMvc.perform(get("/stocks")
                .param("symbol", "AAPL"))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void getBatchStockPrice() throws Exception {
        final String outputJson = mapper.writeValueAsString(TestingConstants.BATCH_STOCK_RESPONSE);

        when(service.getMultipleStockPrices(List.of("AAPL", "GOOG"))).thenReturn(TestingConstants.BATCH_STOCK_RESPONSE);

        this.mockMvc.perform(get("/stocks/batch")
                .param("symbols", "AAPL", "GOOG"))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }
    // 400
    @Test
    void testGetStockPriceShouldHandleResourceNotFoundException() throws Exception {
        final String expectedOutput = "Invalid symbol: ALABAMA";

        when(service.getStockPrice("ALABAMA")).thenThrow(new ResourceNotFoundException(expectedOutput));

        this.mockMvc.perform(get("/stocks")
                .param("symbol", "ALABAMA"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(expectedOutput)))
                .andDo(print());
    }

    // 422
    @Test
    void testGetBatchStockPriceShouldHandleConstraintViolationException() throws Exception {
        final String expectedOutput = "symbols: size must be between 1 and 5";

        this.mockMvc.perform(get("/stocks/batch")
                .param("symbols", "AAPL,GOOG,VOO,TSLA,NVDA,VIX"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(expectedOutput)))
                .andDo(print());
    }
}