package com.briandidthat.priceserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FredService {
    private static final Logger logger = LoggerFactory.getLogger(FredService.class);
    private final String FILE_TYPE = "json";

    @Value("${apis.fred.baseUrl}")
    private String fredBaseUrl;
    @Autowired
    private RestTemplate restTemplate;


}
