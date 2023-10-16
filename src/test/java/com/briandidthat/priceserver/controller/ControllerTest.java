package com.briandidthat.priceserver.controller;

import com.briandidthat.priceserver.domain.BatchRequest;
import com.briandidthat.priceserver.domain.Request;
import com.briandidthat.priceserver.domain.SpotPrice;
import com.briandidthat.priceserver.domain.Statistic;
import com.briandidthat.priceserver.domain.exception.BackendClientException;
import com.briandidthat.priceserver.domain.exception.ResourceNotFoundException;
import com.briandidthat.priceserver.service.CryptoService;
import com.briandidthat.priceserver.util.TestingConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(Controller.class)
class ControllerTest {

    private final LocalDate START_DATE = LocalDate.of(2021, 8, 1);
    private final LocalDate END_DATE = LocalDate.of(2023, 8, 1); // 730 days in between
    private final Statistic ETH_STATISTICS = new Statistic("ETH", "-1100.00", "-27.50", "730");

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CryptoService service;

    @Test
    void testGetSpotPrice() throws Exception {
        String outputJson = mapper.writeValueAsString(TestingConstants.BTC_SPOT);

        when(service.getSpotPrice(TestingConstants.BTC)).thenReturn(TestingConstants.BTC_SPOT);

        this.mockMvc.perform(get("/spot")
                        .header("caller", "test")
                        .param("symbol", TestingConstants.BTC))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void testGetMultipleSpotPrices() throws Exception {
        BatchRequest batchRequest = TestingConstants.SPOT_BATCH;
        List<SpotPrice> expectedResponse = TestingConstants.SPOT_RESPONSES;

        when(service.getSpotPrices(batchRequest)).thenReturn(expectedResponse);

        String inputJson = mapper.writeValueAsString(batchRequest);
        String outputJson = mapper.writeValueAsString(expectedResponse);

        this.mockMvc.perform(post("/spot/batch")
                        .header("caller", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void testGetHistoricalSpotPrice() throws Exception {
        final String inputJson = mapper.writeValueAsString(TestingConstants.HISTORICAL_ETH_REQUEST);
        String outputJson = mapper.writeValueAsString(TestingConstants.HISTORICAL_ETH);

        when(service.getHistoricalSpotPrice(TestingConstants.ETH, START_DATE)).thenReturn(TestingConstants.HISTORICAL_ETH);

        this.mockMvc.perform(post("/spot/historical")
                        .header("caller", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void testGetMultipleHistoricalSpotPrices() throws Exception {
        BatchRequest batchRequest = TestingConstants.HISTORICAL_BATCH;
        List<SpotPrice> expectedResponse = TestingConstants.HISTORICAL_SPOT_RESPONSES;

        when(service.getHistoricalSpotPrices(batchRequest)).thenReturn(expectedResponse);

        String inputJson = mapper.writeValueAsString(batchRequest);
        String outputJson = mapper.writeValueAsString(expectedResponse);

        this.mockMvc.perform(post("/spot/historical/batch")
                        .header("caller", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void testGetPriceStatistics() throws Exception {
        String inputJson = mapper.writeValueAsString(TestingConstants.HISTORICAL_ETH_REQUEST);
        String outputJson = mapper.writeValueAsString(ETH_STATISTICS);

        when(service.getPriceStatistics(TestingConstants.ETH, START_DATE, END_DATE)).thenReturn(ETH_STATISTICS);

        this.mockMvc.perform(post("/spot/statistics")
                        .header("caller", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
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
        this.mockMvc.perform(get("/spot")
                        .header("caller", "test")
                        .param("symbol", "ALABAMA"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(expectedOutput)))
                .andDo(print());
    }

    // 422
    @Test
    void testBatchSpotPricesShouldHandleConstraintViolation() throws Exception {
        // should throw constraint violation due to exceeding max of 5 symbols per request
        BatchRequest request = new BatchRequest(List.of(new Request(TestingConstants.BTC), new Request(TestingConstants.BNB),
                new Request(TestingConstants.ETH), new Request("USDC"), new Request("CAKE"), new Request("APE")));
        String inputJson = mapper.writeValueAsString(request);

        this.mockMvc.perform(post("/spot/batch")
                        .header("caller", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("requests: size must be between 2 and 5")))
                .andDo(print());
    }

    // 500
    @Test
    void testGetSpotPriceShouldHandleBackendClientException() throws Exception {
        String expectedOutput = "SocketTimeoutException: Cannot connect";

        when(service.getSpotPrice(TestingConstants.ETH)).thenThrow(new BackendClientException(expectedOutput));
        // should throw 500 exception due to backend issue
        this.mockMvc.perform(get("/spot")
                        .header("caller", "test")
                        .param("symbol", TestingConstants.ETH))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString(expectedOutput)))
                .andDo(print());
    }

}