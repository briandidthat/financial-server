package com.briandidthat.financialserver.controller;

import com.briandidthat.financialserver.domain.fred.FredResponse;
import com.briandidthat.financialserver.domain.fred.FredSeriesId;
import com.briandidthat.financialserver.service.FredService;
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

    @GetMapping("/observations/{operation}")
    public FredResponse getObservations(@PathVariable String operation, @RequestParam(required = false) LinkedHashMap<String, Object> params, @RequestHeader(required = false) String caller) {
        logger.info("Observation request by {}", caller);
        final String seriesId = FredSeriesId.getSeriesId(operation);
        return service.getObservations(seriesId, params);
    }
}
