package com.elshipper.notificationapi.controller;

import com.elshipper.notificationapi.domain.Stock;
import com.elshipper.notificationapi.domain.rest.AlphaVantageQuoteResponse;
import com.elshipper.notificationapi.service.SchedulingService;
import com.elshipper.notificationapi.service.StockService;
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


@WebMvcTest(StockController.class)
@MockBean(SchedulingService.class)
class StockControllerTest {
    private final AlphaVantageQuoteResponse IBM_RESPONSE = new AlphaVantageQuoteResponse(new AlphaVantageQuoteResponse.Quote());
    private final AlphaVantageQuoteResponse VOO_RESPONSE = new AlphaVantageQuoteResponse(new AlphaVantageQuoteResponse.Quote());
    private final AlphaVantageQuoteResponse TSLA_RESPONSE = new AlphaVantageQuoteResponse(new AlphaVantageQuoteResponse.Quote());

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StockService service;

    @BeforeEach
    void setUp() {

    }

    @Test
    void getQuote() throws Exception {
        String outputJson = mapper.writeValueAsString(IBM_RESPONSE);

        when(service.getQuote(Stock.IBM.getSymbol())).thenReturn(IBM_RESPONSE);

        this.mockMvc.perform(get("/stocks/symbol").param("symbol", Stock.IBM.getSymbol()))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void getQuotes() throws Exception {
        List<String> symbols = List.of(Stock.IBM.getSymbol(), Stock.VOO.getSymbol(), Stock.TESLA.getSymbol());
        List<AlphaVantageQuoteResponse> responses = List.of(IBM_RESPONSE, VOO_RESPONSE, TSLA_RESPONSE);

        String inputJson = mapper.writeValueAsString(symbols);
        String outputJson = mapper.writeValueAsString(responses);

        when(service.getMultipleQuotes(symbols)).thenReturn(responses);

        this.mockMvc.perform(get("/stocks/symbol/multiple")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }
}