package com.briandidthat.financialserver.service;

import com.briandidthat.financialserver.domain.exception.BadRequestException;
import com.briandidthat.financialserver.domain.twelve.TwelveResponse;
import com.briandidthat.financialserver.util.RequestUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TwelveService {
    private static final Logger logger = LoggerFactory.getLogger(TwelveService.class);

    @Value("${apis.twelve.baseUrl}")
    private String twelveBaseUrl;
    @Value("${apis.twelve.apiKey}")
    private String twelveApiKey;
    @Autowired
    private RestTemplate restTemplate;

    public TwelveResponse getStockPrice(Map<String, Object> params) {
        params.put("apiKey", twelveApiKey);
        try {
            logger.info("Fetching current price for {}", params.get("symbol"));
            final String url = RequestUtilities.formatQueryString(twelveBaseUrl + "/price", params);
            return restTemplate.getForObject(url, TwelveResponse.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }
}
