package com.briandidthat.econserver.controller;

import com.briandidthat.econserver.domain.exception.ResourceNotFoundException;
import com.briandidthat.econserver.domain.exception.RetrievalException;
import com.briandidthat.econserver.service.FredService;
import com.briandidthat.econserver.util.TestingConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FredController.class)
class FredControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FredService service;

    @Test
    void getObservations() throws Exception {
        String outputJson = mapper.writeValueAsString(TestingConstants.MORTGAGE_RATE_RESPONSE);

        when(service.getObservations(TestingConstants.TEST_API_KEY, TestingConstants.AVERAGE_MORTGAGE_RATE, new LinkedHashMap<>()))
                .thenReturn(TestingConstants.MORTGAGE_RATE_RESPONSE);

        this.mockMvc.perform(get("/fred/observations/{seriesId}",TestingConstants.AVERAGE_MORTGAGE_RATE)
                .header("apiKey", TestingConstants.TEST_API_KEY))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void getMostRecentObservation() throws Exception {
        String outputJson = mapper.writeValueAsString(TestingConstants.CURRENT_MORTGAGE_RATE);

        when(service.getMostRecentObservation(TestingConstants.TEST_API_KEY, TestingConstants.AVERAGE_MORTGAGE_RATE, new LinkedHashMap<>()))
                .thenReturn(TestingConstants.CURRENT_MORTGAGE_RATE);

        this.mockMvc.perform(get("/fred/observations/recent/{seriesId}",TestingConstants.AVERAGE_MORTGAGE_RATE)
                .header("apiKey", TestingConstants.TEST_API_KEY))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    // 400 error
    @Test
    void getObservationsShouldHandleOperationNotFound() throws Exception {
        final String expectedOutput = "Bad Request.  The series does not exist.";

        when(service.getObservations(TestingConstants.TEST_API_KEY,"randomOperation", new LinkedHashMap<>()))
                .thenThrow(new ResourceNotFoundException(expectedOutput));

        this.mockMvc.perform(get("/fred/observations/{seriesId}","randomOperation")
                .header("apiKey", TestingConstants.TEST_API_KEY))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(expectedOutput)))
                .andDo(print());
    }

    // 500
    @Test
    void getObservationsShouldHandleRetrievalException() throws Exception {
        final String expectedOutput = "Retrieval error.";

        when(service.getObservations(TestingConstants.TEST_API_KEY, TestingConstants.AVERAGE_MORTGAGE_RATE, new LinkedHashMap<>()))
                .thenThrow(new RetrievalException(expectedOutput));

        this.mockMvc.perform(get("/fred/observations/{seriesId}", TestingConstants.AVERAGE_MORTGAGE_RATE)
                .header("apiKey", TestingConstants.TEST_API_KEY))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString(expectedOutput)))
                .andDo(print());
    }

}