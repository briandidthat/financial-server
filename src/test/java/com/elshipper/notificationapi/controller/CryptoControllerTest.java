package com.elshipper.notificationapi.controller;

import com.elshipper.notificationapi.service.CryptoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

class CryptoControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CryptoService service;

    @Test
    void getAssetPrice() {
    }

    @Test
    void getAssetPricesAsync() {
    }

    @Test
    void getAssetPricesSync() {
    }

    @Test
    void getAccountBalance() {
    }
}