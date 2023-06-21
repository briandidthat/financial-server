package com.briandidthat.priceserver.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HealthCheckController.class)
class HealthCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        HealthCheckController.setAvailable(false);
    }

    @Test
    void isAvailable() throws Exception {
        HealthCheckController.setAvailable(true);

        this.mockMvc.perform(get("/healthz"))
                .andExpect(status().isOk())
                .andExpect(content().string("AVAILABLE"))
                .andDo(print());
    }

    @Test
    void isNotAvailable() throws Exception {
        this.mockMvc.perform(get("/healthz"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("UNAVAILABLE"))
                .andDo(print());
    }

    @Test
    void isReady() throws Exception {
        HealthCheckController.setAvailable(true);

        this.mockMvc.perform(get("/readyz"))
                .andExpect(status().isOk())
                .andExpect(content().string("READY"))
                .andDo(print());
    }

    @Test
    void isNotReady() throws Exception {
        this.mockMvc.perform(get("/readyz"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("UNAVAILABLE"))
                .andDo(print());
    }
}