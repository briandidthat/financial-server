package com.elshipper.notificationapi.controller;

import com.elshipper.notificationapi.service.CryptoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(CryptoController.class)
class CryptoControllerTest {
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

    @Test
    void getAccountBalancesAsync() {
    }
}