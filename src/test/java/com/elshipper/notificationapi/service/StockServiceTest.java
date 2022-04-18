package com.elshipper.notificationapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockServiceTest {
    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private StockService service;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getQuote() {
    }

    @Test
    void getQuoteAsync() {
    }

    @Test
    void getMultipleQuotes() {
    }
}