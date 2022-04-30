package com.elshipper.notificationapi.controller;

import com.elshipper.notificationapi.domain.Cryptocurrency;
import com.elshipper.notificationapi.domain.Notification;
import com.elshipper.notificationapi.service.NotificationService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {
    private final Notification TO_SAVE = new Notification(Cryptocurrency.AVAX.getSymbol(), "54.00", "down",
            "everytime", false);
    private final Notification AVAX_NOTIFICATION = new Notification(1, Cryptocurrency.AVAX.getSymbol(), "54.00",
            "down", "everytime", false);
    private final Notification BTC_NOTIFICATION = new Notification(2, Cryptocurrency.BTC.getSymbol(), "40000.00",
            "down", "everytime", false);
    private final Notification BNB_NOTIFICATION = new Notification(3, Cryptocurrency.BNB.getSymbol(), "380.00",
            "down", "everytime", true);
    private final Notification BNB_UPDATED = new Notification(3, Cryptocurrency.BNB.getSymbol(), "380.00",
            "down", "once", true);

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NotificationService service;

    @BeforeEach
    void setUp() {
    }

    @Test
    void saveNotification() throws Exception {
        String inputJson = mapper.writeValueAsString(TO_SAVE);
        String outputJson = mapper.writeValueAsString(AVAX_NOTIFICATION);

        when(service.saveNotification(TO_SAVE)).thenReturn(AVAX_NOTIFICATION);

        this.mockMvc.perform(post("/notifications/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void getAllNotifications() throws Exception {
        List<Notification> notifications = List.of(AVAX_NOTIFICATION, BTC_NOTIFICATION, BNB_NOTIFICATION);
        String outputJson = mapper.writeValueAsString(notifications);

        when(service.findAllNotifications()).thenReturn(notifications);

        this.mockMvc.perform(get("/notifications"))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void updateNotification() throws Exception {
        Notification notification = BNB_NOTIFICATION;
        notification.setFrequency("once");
        String inputJson = mapper.writeValueAsString(notification);
        String outputJson = mapper.writeValueAsString(BNB_UPDATED);

        when(service.updateNotification(notification)).thenReturn(BNB_UPDATED);

        this.mockMvc.perform(put("/notifications/" + BNB_NOTIFICATION.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void deleteNotification() throws Exception{
        when(service.deleteNotification(BTC_NOTIFICATION.getId())).thenReturn(true);

        this.mockMvc.perform(delete("/notifications/" + BTC_NOTIFICATION.getId()))
                .andExpect(status().isNoContent())
                .andExpect(content().string("successful"))
                .andDo(print());
    }

    @Test
    void getTriggeredNotifications() throws Exception {
        List<Notification> notifications = List.of(BNB_NOTIFICATION);
        String outputJson = mapper.writeValueAsString(notifications);

        when(service.findTriggeredNotifications(BNB_NOTIFICATION.getAsset())).thenReturn(notifications);

        this.mockMvc.perform(get("/notifications/status")
                        .param("asset", BNB_NOTIFICATION.getAsset())
                        .param("triggered", String.valueOf(BNB_NOTIFICATION.isTriggered())))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void getUntriggeredNotifications() throws Exception {
        List<Notification> notifications = List.of(BTC_NOTIFICATION);
        String outputJson = mapper.writeValueAsString(notifications);

        when(service.findUntriggeredNotifications(BTC_NOTIFICATION.getAsset())).thenReturn(notifications);

        this.mockMvc.perform(get("/notifications/status")
                        .param("asset", BTC_NOTIFICATION.getAsset())
                        .param("triggered", String.valueOf(BTC_NOTIFICATION.isTriggered())))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }

    @Test
    void getNotificationsByAsset() throws Exception {
        List<Notification> notifications = List.of(AVAX_NOTIFICATION);
        String outputJson = mapper.writeValueAsString(notifications);

        when(service.findNotificationsByAsset(AVAX_NOTIFICATION.getAsset())).thenReturn(notifications);

        this.mockMvc.perform(get("/notifications/asset")
                        .param("asset", AVAX_NOTIFICATION.getAsset()))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson))
                .andDo(print());
    }
}