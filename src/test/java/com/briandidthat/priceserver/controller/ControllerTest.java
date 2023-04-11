package com.briandidthat.priceserver.controller;

import com.briandidthat.priceserver.domain.Cryptocurrency;
import com.briandidthat.priceserver.domain.SpotPrice;
import com.briandidthat.priceserver.domain.Statistic;
import com.briandidthat.priceserver.domain.exception.BackendClientException;
import com.briandidthat.priceserver.domain.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.briandidthat.priceserver.service.CryptoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(Controller.class)
class ControllerTest {
    private final SpotPrice BTC = new SpotPrice(Cryptocurrency.BTC, "40102.44", "coinbase", LocalDate.now());
    private final SpotPrice BNB = new SpotPrice(Cryptocurrency.BNB, "389.22", "coinbase", LocalDate.now());
    private final SpotPrice ETH = new SpotPrice(Cryptocurrency.ETH, "2900.24", "coinbase", LocalDate.now());
    private final LocalDate START_DATE = LocalDate.of(2021, 8, 1);
    private final LocalDate END_DATE = LocalDate.of(2023, 8, 1); // 730 days in between
    private final SpotPrice HISTORICAL_ETH = new SpotPrice(Cryptocurrency.ETH, "USD", "4000.00", START_DATE);
    private final SpotPrice HISTORICAL_BTC = new SpotPrice(Cryptocurrency.BTC, "USD", "41000.00", START_DATE);
    private final Statistic ETH_STATISTICS = new Statistic("ETH", "-1100.00", "-27.50", "730");

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
    void testGetSpotPrice() throws Exception {
        String outputJson = mapper.writeValueAsString(BTC);

        when(service.getSpotPrice(Cryptocurrency.BTC)).thenReturn(BTC);

        this.mockMvc.perform(get("/spot").param("symbol", Cryptocurrency.BTC))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void testGetMultipleSpotPrices() throws Exception {
        List<SpotPrice> responses = List.of(BTC, BNB, ETH);
        List<String> symbols = List.of(Cryptocurrency.BTC, Cryptocurrency.BNB, Cryptocurrency.ETH);
        Map<String, List<String>> body = Map.of("symbols", symbols);

        when(service.getSpotPrices(symbols)).thenReturn(responses);

        String inputJson = mapper.writeValueAsString(body);
        String outputJson = mapper.writeValueAsString(responses);

        this.mockMvc.perform(get("/spot/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void testGetHistoricalSpotPrice() throws Exception {
        String outputJson = mapper.writeValueAsString(BTC);

        when(service.getHistoricalSpotPrice(Cryptocurrency.BTC, START_DATE)).thenReturn(BTC);

        this.mockMvc.perform(get("/spot/historical")
                        .param("symbol", Cryptocurrency.BTC)
                        .param("date", START_DATE.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void testGetMultipleHistoricalSpotPrices() throws Exception {
        Map<String, LocalDate> symbols = new HashMap<>();
        symbols.put(Cryptocurrency.ETH, START_DATE);
        symbols.put(Cryptocurrency.BTC, START_DATE);
        List<SpotPrice> prices = List.of(HISTORICAL_ETH, HISTORICAL_BTC);
        when(service.getHistoricalSpotPrices(symbols)).thenReturn(prices);

        String inputJson = mapper.writeValueAsString(symbols);
        String outputJson = mapper.writeValueAsString(prices);

        this.mockMvc.perform(get("/spot/historical/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void testGetPriceStatistics() throws Exception {
        String outputJson = mapper.writeValueAsString(ETH_STATISTICS);
        when(service.getPriceStatistics(Cryptocurrency.ETH, START_DATE, END_DATE)).thenReturn(ETH_STATISTICS);

        this.mockMvc.perform(get("/spot/statistics")
                        .param("symbol", Cryptocurrency.ETH)
                        .param("startDate", START_DATE.toString())
                        .param("endDate", END_DATE.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    // Testing Error Handling Code

    // 404
    @Test
    void testGetSpotPriceShouldHandleResourceNotFoundException() throws Exception {
        String expectedOutput = "Invalid symbol: ALABAMA";

        when(service.getSpotPrice("ALABAMA")).thenThrow(new ResourceNotFoundException(expectedOutput));
        // should throw 400 exception due to invalid symbol
        this.mockMvc.perform(get("/spot").param("symbol", "ALABAMA"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(expectedOutput)))
                .andDo(print());
    }

    // 422
    @Test
    void testGetBatchSpotPricesShouldHandleConstraintViolation() throws Exception {
        // should throw constraint violation due to exceeding max of 5 symbols per request
        List<String> symbols = List.of(Cryptocurrency.BTC, Cryptocurrency.BNB, Cryptocurrency.ETH, "USDC", "CAKE", "APE");
        Map<String, List<String>> body = Map.of("symbols", symbols);
        String inputJson = mapper.writeValueAsString(body);

        this.mockMvc.perform(get("/spot/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("symbols: size must be between 2 and 5")))
                .andDo(print());
    }

    // 500
    @Test
    void testGetSpotPriceShouldHandleBackendClientException() throws Exception {
        String expectedOutput = "SocketTimeoutException: Cannot connect";

        when(service.getSpotPrice(Cryptocurrency.ETH)).thenThrow(new BackendClientException(expectedOutput));
        // should throw 500 exception due to backend issue
        this.mockMvc.perform(get("/spot").param("symbol", Cryptocurrency.ETH))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString(expectedOutput)))
                .andDo(print());
    }

}