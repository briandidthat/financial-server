package com.briandidthat.econserver.controller;

import com.briandidthat.econserver.domain.fred.FredResponse;
import com.briandidthat.econserver.domain.fred.Observation;
import com.briandidthat.econserver.service.FredService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/fred")
public class FredController {
    private static final Logger logger = LoggerFactory.getLogger("FredController");

    @Autowired
    private FredService service;

    @GetMapping("/observations/{seriesId}")
    public FredResponse getObservations(@RequestHeader String apiKey, @RequestHeader String caller, @PathVariable String seriesId,
                                        @RequestParam(required = false) LinkedHashMap<String, Object> params) {
        logger.info("Observation request by {}", caller);
        return service.getObservations(apiKey, seriesId, params);
    }

    @GetMapping("/observations/{seriesId}/recent")
    public Observation getMostRecentObservation(@RequestHeader String apiKey, @RequestHeader String caller, @PathVariable String seriesId,
                                                @RequestParam(required = false) LinkedHashMap<String, Object> params) {
        logger.info("Observation request by {}", caller);
        return service.getMostRecentObservation(apiKey, seriesId, params);
    }
}
