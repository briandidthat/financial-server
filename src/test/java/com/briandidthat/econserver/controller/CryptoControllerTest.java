package com.briandidthat.econserver.controller;

import com.briandidthat.econserver.domain.coinbase.BatchRequest;
import com.briandidthat.econserver.domain.coinbase.BatchResponse;
import com.briandidthat.econserver.domain.coinbase.SpotPrice;
import com.briandidthat.econserver.domain.exception.BackendClientException;
import com.briandidthat.econserver.domain.exception.BadRequestException;
import com.briandidthat.econserver.service.CoinbaseService;
import com.briandidthat.econserver.util.TestingConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CryptoController.class)
class CryptoControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CoinbaseService service;

    @Test
    void testGetSpotPrice() throws Exception {
        String outputJson = mapper.writeValueAsString(TestingConstants.BTC_SPOT);

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("symbol", "BTC");

        when(service.getSpotPrice(TestingConstants.BTC)).thenReturn(TestingConstants.BTC_SPOT);

        this.mockMvc.perform(get("/crypto/spot")
                .params(params)
                .header("caller", "test"))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void testGetMultipleSpotPrices() throws Exception {
        String symbolsString = String.join(",", TestingConstants.TOKENS);
        BatchResponse expectedResponse = TestingConstants.BATCH_SPOT_RESPONSE;

        String outputJson = mapper.writeValueAsString(expectedResponse);
        when(service.getSpotPrices(TestingConstants.TOKENS)).thenReturn(expectedResponse);

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("symbols", symbolsString);

        this.mockMvc.perform(get("/crypto/spot/batch")
                .params(params)
                .header("caller", "test"))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void testGetHistoricalSpotPrice() throws Exception {
        String outputJson = mapper.writeValueAsString(TestingConstants.HISTORICAL_ETH);
        when(service.getHistoricalSpotPrice(TestingConstants.ETH, TestingConstants.START_DATE)).thenReturn(TestingConstants.HISTORICAL_ETH);

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("symbol", TestingConstants.ETH);
        params.add("date", TestingConstants.START_DATE.toString());

        this.mockMvc.perform(get("/crypto/spot/historical")
                .params(params)
                .header("caller", "test"))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void testGetMultipleHistoricalSpotPrices() throws Exception {
        BatchRequest batchRequest = TestingConstants.HISTORICAL_BATCH;
        BatchResponse expectedResponse = TestingConstants.BATCH_HISTORICAL_SPOT_RESPONSE;

        when(service.getHistoricalSpotPrices(batchRequest)).thenReturn(expectedResponse);

        String inputJson = mapper.writeValueAsString(batchRequest);
        String outputJson = mapper.writeValueAsString(expectedResponse);

        this.mockMvc.perform(post("/crypto/spot/batch/historical")
                .header("caller", "test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void testGetPriceStatistics() throws Exception {
        String outputJson = mapper.writeValueAsString(TestingConstants.ETH_STATISTICS);

        when(service.getPriceStatistics(TestingConstants.ETH, TestingConstants.START_DATE, TestingConstants.END_DATE)).thenReturn(TestingConstants.ETH_STATISTICS);

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("symbol", TestingConstants.ETH);
        params.add("startDate", TestingConstants.START_DATE.toString());
        params.add("endDate", TestingConstants.END_DATE.toString());

        this.mockMvc.perform(get("/crypto/spot/statistics")
                .params(params)
                .header("caller", "test"))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    // Testing Error Handling Code

    // 400
    @Test
    void testGetSpotPriceShouldHandleBadRequestException() throws Exception {
        String expectedOutput = "Required request header 'caller' for method parameter type String is not present";

        // should throw 400 exception due to missing caller header
        this.mockMvc.perform(get("/crypto/spot")
                .param("symbol", TestingConstants.BTC))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(expectedOutput)))
                .andDo(print());
    }

    // 400
    @Test
    void testGetSpotPriceShouldHandleResourceNotFoundException() throws Exception {
        String expectedOutput = "Invalid symbol: ALABAMA";

        when(service.getSpotPrice("ALABAMA")).thenThrow(new BadRequestException(expectedOutput));
        // should throw 400 exception due to invalid symbol
        this.mockMvc.perform(get("/crypto/spot")
                .param("symbol", "ALABAMA")
                .header("caller", "test"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(expectedOutput)))
                .andDo(print());
    }

    // 422
    @Test
    void testBatchSpotPricesShouldHandleConstraintViolation() throws Exception {
        // should throw constraint violation due to exceeding max of 5 symbols per request
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("symbols", String.join(",", List.of(TestingConstants.BTC, TestingConstants.BNB, TestingConstants.ETH, "USDC", "CAKE", "APE")));

        this.mockMvc.perform(get("/crypto/spot/batch")
                .params(params)
                .header("caller", "test"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("symbols: size must be between 2 and 5")))
                .andDo(print());
    }

    // 500
    @Test
    void testGetSpotPriceShouldHandleBackendClientException() throws Exception {
        String expectedOutput = "SocketTimeoutException: Cannot connect";

        when(service.getSpotPrice(TestingConstants.ETH)).thenThrow(new BackendClientException(expectedOutput));
        // should throw 500 exception due to backend issue
        this.mockMvc.perform(get("/crypto/spot")
                .param("symbol", TestingConstants.ETH)
                .header("caller", "test"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString(expectedOutput)))
                .andDo(print());
    }

}