package com.toogroovy.priceserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toogroovy.priceserver.domain.Cryptocurrency;
import com.toogroovy.priceserver.domain.SpotPrice;
import com.toogroovy.priceserver.domain.Statistic;
import com.toogroovy.priceserver.service.CryptoService;
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
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.contains;
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

    @Test
    void testGetBatchSpotPricesShouldThrowConstraintViolation() throws Exception {
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

    @Test
    void testGetSpotPriceShouldThrowValidationException() throws Exception {
        String expectedOutput = "Invalid symbol: ALABAMA";

        when(service.getSpotPrice("ALABAMA")).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, expectedOutput));
        // should throw 422 exception due to invalid symbol
        this.mockMvc.perform(get("/spot").param("symbol", "ALABAMA"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(expectedOutput)))
                .andDo(print());
    }
}