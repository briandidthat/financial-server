package com.briandidthat.financialserver.service;

import com.briandidthat.financialserver.domain.exception.BadRequestException;
import com.briandidthat.financialserver.domain.fred.FredResponse;
import com.briandidthat.financialserver.util.RequestUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class FredService {
    private static final Logger logger = LoggerFactory.getLogger(FredService.class);

    @Value("${apis.fred.baseUrl}")
    private String fredBaseUrl;
    @Value("${apis.fred.apiKey}")
    private String fredApiKey;
    @Autowired
    private RestTemplate restTemplate;

    public FredResponse getObservations(String seriesId, LinkedHashMap<String, Object> params) {
        params.put("series_id", seriesId);
        params.put("file_type", "json");
        params.put("api_key", fredApiKey);
        final String url = RequestUtilities.formatQueryString(fredBaseUrl + "/series/observations", params);
        try {
            return restTemplate.getForObject(url, FredResponse.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }


}
