package com.briandidthat.financialserver.controller;

import com.briandidthat.financialserver.domain.coinbase.BatchRequest;
import com.briandidthat.financialserver.domain.coinbase.Request;
import com.briandidthat.financialserver.domain.coinbase.SpotPrice;
import com.briandidthat.financialserver.domain.coinbase.Statistic;
import com.briandidthat.financialserver.domain.exception.BackendClientException;
import com.briandidthat.financialserver.domain.exception.ResourceNotFoundException;
import com.briandidthat.financialserver.service.CoinbaseService;
import com.briandidthat.financialserver.util.TestingConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CoinbaseController.class)
class CoinbaseControllerTest {
    private final Statistic ETH_STATISTICS = new Statistic("ETH", "-1100.00", "-27.50", "730");

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CoinbaseService service;

    @Test
    void testGetSpotPrice() throws Exception {
        String inputJson = mapper.writeValueAsString(TestingConstants.BTC_SPOT_REQUEST);
        String outputJson = mapper.writeValueAsString(TestingConstants.BTC_SPOT);

        when(service.getSpotPrice(TestingConstants.BTC)).thenReturn(TestingConstants.BTC_SPOT);

        this.mockMvc.perform(post("/spot")
                        .header("caller", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
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

        when(service.getHistoricalSpotPrice(TestingConstants.ETH, TestingConstants.START_DATE)).thenReturn(TestingConstants.HISTORICAL_ETH);

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

        when(service.getPriceStatistics(TestingConstants.ETH, TestingConstants.START_DATE, TestingConstants.END_DATE)).thenReturn(ETH_STATISTICS);

        this.mockMvc.perform(post("/spot/statistics")
                        .header("caller", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    // Testing Error Handling Code

    // 400
    @Test
    void testGetSpotPriceShouldHandleBadRequestException() throws Exception {
        String inputJson = mapper.writeValueAsString(TestingConstants.BTC_SPOT_REQUEST);
        String expectedOutput = "Required request header 'caller' for method parameter type String is not present";

        // should throw 400 exception due to missing caller header
        this.mockMvc.perform(post("/spot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(expectedOutput)))
                .andDo(print());
    }


    // 404
    @Test
    void testGetSpotPriceShouldHandleResourceNotFoundException() throws Exception {
        String inputJson = mapper.writeValueAsString(new Request("ALABAMA"));
        String expectedOutput = "Invalid symbol: ALABAMA";

        when(service.getSpotPrice("ALABAMA")).thenThrow(new ResourceNotFoundException(expectedOutput));
        // should throw 400 exception due to invalid symbol
        this.mockMvc.perform(post("/spot")
                        .header("caller", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
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
        String inputJson = mapper.writeValueAsString(TestingConstants.ETH_SPOT_REQUEST);
        String expectedOutput = "SocketTimeoutException: Cannot connect";

        when(service.getSpotPrice(TestingConstants.ETH)).thenThrow(new BackendClientException(expectedOutput));
        // should throw 500 exception due to backend issue
        this.mockMvc.perform(post("/spot")
                        .header("caller", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString(expectedOutput)))
                .andDo(print());
    }

}