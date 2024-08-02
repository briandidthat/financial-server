package com.briandidthat.econserver.service;

import com.briandidthat.econserver.domain.exception.RetrievalException;
import com.briandidthat.econserver.domain.fred.FredResponse;
import com.briandidthat.econserver.domain.fred.Observation;
import com.briandidthat.econserver.util.RequestUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;

@Service
public class FredService {
    private static final Logger logger = LoggerFactory.getLogger("FredService");

    @Value("${apis.fred.baseUrl}")
    private String fredBaseUrl;
    @Autowired
    private RestTemplate restTemplate;

    public FredResponse getObservations(String apiKey, String seriesId, HashMap<String, Object> params) {
        LinkedHashMap<String, Object> linkedParams = new LinkedHashMap<>(params);
        linkedParams.put("series_id", seriesId);
        linkedParams.put("file_type", "json");
        linkedParams.put("sort_order", "desc");
        linkedParams.put("api_key", apiKey);
        final String url = RequestUtilities.formatQueryString(fredBaseUrl + "/series/observations", linkedParams);

        try {
            logger.debug("Fetching observations for {}", seriesId);
            return restTemplate.getForObject(url, FredResponse.class);
        } catch (RestClientException e) {
            logger.error(e.getMessage());
            throw new RetrievalException(e);
        }
    }

    public Observation getMostRecentObservation(String apiKey, String seriesId, HashMap<String, Object> params) {
        final FredResponse response = getObservations(apiKey, seriesId, params);
        if (response.getObservations() == null || response.getObservations().isEmpty())
            throw new RetrievalException("No observations returned");
        return response.getObservations().getFirst();
    }
}
